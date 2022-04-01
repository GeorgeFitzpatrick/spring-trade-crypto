package com.georgefitzpatrick.trading.crypto.strategy;

import com.georgefitzpatrick.trading.crypto.data.entity.Condition;
import com.georgefitzpatrick.trading.crypto.data.entity.ExchangeCredentials;
import com.georgefitzpatrick.trading.crypto.data.entity.Strategy;
import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.account.AccountInfo;
import org.knowm.xchange.dto.account.Wallet;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.dto.trade.MarketOrder;
import org.knowm.xchange.exceptions.ExchangeException;

import java.math.BigDecimal;
import java.math.MathContext;
import java.net.UnknownHostException;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.function.Predicate;

import static com.georgefitzpatrick.trading.crypto.data.entity.Condition.Action.BUY;
import static com.georgefitzpatrick.trading.crypto.data.entity.Condition.Action.SELL;
import static java.math.MathContext.DECIMAL128;
import static java.math.MathContext.DECIMAL32;
import static java.time.Duration.ZERO;
import static org.knowm.xchange.dto.Order.OrderType.ASK;
import static org.knowm.xchange.dto.Order.OrderType.BID;

/**
 * @author George Fitzpatrick
 */
public class ScheduledStrategyService extends ScheduledService<Void> {

    /* ----- Fields ----- */

    private static final MathContext PRECISION = DECIMAL128;

    private final Strategy strategy;
    private final ListProperty<Event> events;

    private final TimeSeries<BigDecimal> movingPrice;

    private Exchange exchange;
    private BigDecimal position;

    /* ----- Constructors ----- */

    public ScheduledStrategyService(Strategy strategy) {
        this.strategy = strategy;
        this.events = new SimpleListProperty<>(this, "orders", FXCollections.observableArrayList());
        this.movingPrice = new TimeSeries<>(ZERO);
        setPeriod(javafx.util.Duration.millis(500));
        setBackoffStrategy(ScheduledService::getPeriod);
    }

    /* ----- Methods ----- */

    public Strategy strategy() {
        return strategy;
    }

    public ReadOnlyListProperty<Event> eventsProperty() {
        return events;
    }

    @Override
    protected Task<Void> createTask() {
        return new StrategyTask();
    }

    @Override
    public void reset() {
        events.clear();
        movingPrice.clear();
        exchange = null;
        position = null;
        super.reset();
    }

    /* ----- Classes ----- */

    public record Event(Instant timestamp, String localizedMessageKey) {

    }

    private class StrategyTask extends Task<Void> {

        /* ----- Fields ----- */

        private final CurrencyPair instrument;
        private final Duration timeframe;
        private final ExchangeSpecification specification;
        private final int maxPeriod;
        private final BigDecimal risk;
        private final List<Condition> buyConditions;
        private final List<Condition> sellConditions;

        /* ----- Constructors ----- */

        public StrategyTask() {
            this.instrument = new CurrencyPair(strategy.getBaseCurrency(), strategy.getCounterCurrency());
            this.timeframe = Duration.of(strategy.getTimeframe(), strategy.getTimeframeUnit());

            // convert ExchangeCredentials entity into org.knowm.xchange.ExchangeSpecification
            ExchangeCredentials credentials = strategy.getExchangeCredentials();
            this.specification = new ExchangeSpecification(credentials.getExchangeClass());
            this.specification.setUserName(credentials.getUsername());
            this.specification.setPassword(credentials.getPassword());
            this.specification.setApiKey(credentials.getApiKey());
            this.specification.setSecretKey(credentials.getSecretKey());
            this.specification.setExchangeSpecificParametersItem("Use_Sandbox", credentials.isSandbox());
            this.specification.setExchangeSpecificParametersItem("passphrase", credentials.getPassphrase());

            // remove default rate limit handling by set fresh specification
            this.specification.setResilience(new ExchangeSpecification.ResilienceSpecification());

            this.maxPeriod = strategy.getConditions().stream()
                    .map(condition -> Integer.max(condition.getBasePeriod(), condition.getCounterPeriod()))
                    .max(Integer::compareTo).orElse(2);

            this.risk = BigDecimal.valueOf(strategy.getRisk());
            this.buyConditions = strategy.getConditions().stream().filter(c -> c.getAction() == BUY).toList();
            this.sellConditions = strategy.getConditions().stream().filter(c -> c.getAction() == SELL).toList();
        }

        /* ----- Methods ----- */

        @Override
        protected Void call() throws Exception {
            // create and authenticate with exchange on first run
            if (exchange == null) {
                exchange = ExchangeFactory.INSTANCE.createExchange(specification);
            }

            // get quantity of counter currency held in trading wallet on exchange
            AccountInfo account = exchange.getAccountService().getAccountInfo();
            Wallet wallet = account.getWallet("trade");
            BigDecimal balance = wallet.getBalance(instrument.counter).getAvailable();

            // get the last trading price for the base currency in counter currency units
            Ticker ticker = exchange.getMarketDataService().getTicker(instrument);
            Instant timestamp = ticker.getTimestamp().toInstant();
            BigDecimal price = ticker.getLast();
            movingPrice.setMaxAge(timeframe.multipliedBy(maxPeriod));
            movingPrice.put(timestamp, price);

            Predicate<Condition> matcher = condition -> {
                // ensure service has been running enough to collect sufficient data
                int maxPeriod = Integer.max(condition.getBasePeriod(), condition.getCounterPeriod());
                if (!movingPrice.availableFor(timeframe, maxPeriod + 1)) {
                    return false;
                }

                Indicator baseIndicator = condition.getBaseIndicator();
                Indicator counterIndicator = condition.getCounterIndicator();

                List<BigDecimal> previousBaseBars = movingPrice.getAll(timeframe, condition.getBasePeriod() + 1, 1);
                BigDecimal previousBase = baseIndicator.calculate(previousBaseBars, PRECISION);

                List<BigDecimal> currentBaseBars = movingPrice.getAll(timeframe, condition.getBasePeriod(), 0);
                BigDecimal currentBase = baseIndicator.calculate(currentBaseBars, PRECISION);

                List<BigDecimal> previousCounterBars = movingPrice.getAll(timeframe, condition.getCounterPeriod() + 1, 1);
                BigDecimal previousCounter = counterIndicator.calculate(previousCounterBars, PRECISION);

                List<BigDecimal> currentCounterBars = movingPrice.getAll(timeframe, condition.getCounterPeriod(), 0);
                BigDecimal currentCounter = counterIndicator.calculate(currentCounterBars, PRECISION);

                // test if data matches the relationship required for the condition to be met
                return condition.getRelationship().test(previousBase, currentBase, previousCounter, currentCounter);
            };

            if (position == null && buyConditions.stream().allMatch(matcher)) {
                // calculate amount of counter currency to risk
                BigDecimal balanceInCounterUnits = balance.divide(price, PRECISION);
                BigDecimal decimalRisk = risk.divide(new BigDecimal("100"), PRECISION);
                position = balanceInCounterUnits.multiply(decimalRisk, DECIMAL32);

                // buy at market price
                MarketOrder order = new MarketOrder(BID, position, instrument);
                exchange.getTradeService().placeMarketOrder(order);

                // publish localised "buying" event to user interface
                Event event = new Event(Instant.now(), "buying");
                Platform.runLater(() -> events.add(event));
            } else if (position != null && sellConditions.stream().allMatch(matcher)) {
                // sell previously bought amount at market price
                MarketOrder order = new MarketOrder(ASK, position, instrument);
                exchange.getTradeService().placeMarketOrder(order);

                // clear position
                position = null;

                // publish localised "selling" event to user interface
                Event event = new Event(Instant.now(), "selling");
                Platform.runLater(() -> events.add(event));
            }

            return null;
        }

        @Override
        protected void failed() {
            Throwable exception = getException() instanceof ExchangeException ? getException().getCause() : getException();
            String message = exception instanceof UnknownHostException ? "networkConnectionError" : "unexpectedError";
            events.add(new Event(Instant.now(), message));
        }

    }

}
