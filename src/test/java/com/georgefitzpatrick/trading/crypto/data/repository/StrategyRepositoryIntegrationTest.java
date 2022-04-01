package com.georgefitzpatrick.trading.crypto.data.repository;

import com.georgefitzpatrick.trading.crypto.data.entity.Strategy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.knowm.xchange.currency.Currency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.temporal.ChronoUnit;

/**
 * @author George Fitzpatrick
 */
@SpringBootTest
@SuppressWarnings("ClassCanBeRecord")
public class StrategyRepositoryIntegrationTest {

    /* ----- Fields ----- */

    private static final String VALID_NAME = "name";
    private static final Currency VALID_CURRENCY = Currency.BTC;
    private static final int VALID_TIMEFRAME = 1;
    private static final ChronoUnit VALID_TIMEFRAME_UNIT = ChronoUnit.DAYS;
    private static final Double VALID_RISK = 1.0;

    private final StrategyRepository repository;

    /* ----- Constructors ----- */

    @Autowired
    public StrategyRepositoryIntegrationTest(StrategyRepository repository) {
        this.repository = repository;
    }

    /* ----- Methods ----- */

    @Test
    public void saveStrategy_nullName_throwsException() {
        Strategy strategy = new Strategy();
        strategy.setName(null);
        strategy.setBaseCurrency(VALID_CURRENCY);
        strategy.setCounterCurrency(VALID_CURRENCY);
        strategy.setTimeframe(VALID_TIMEFRAME);
        strategy.setTimeframeUnit(VALID_TIMEFRAME_UNIT);
        strategy.setRisk(VALID_RISK);
        Assertions.assertThrows(Exception.class, () -> repository.save(strategy));
    }

    @Test
    public void saveStrategy_nullBaseCurrency_throwsException() {
        Strategy strategy = new Strategy();
        strategy.setName(VALID_NAME);
        strategy.setBaseCurrency(null);
        strategy.setCounterCurrency(VALID_CURRENCY);
        strategy.setTimeframe(VALID_TIMEFRAME);
        strategy.setTimeframeUnit(VALID_TIMEFRAME_UNIT);
        strategy.setRisk(VALID_RISK);
        Assertions.assertThrows(Exception.class, () -> repository.save(strategy));
    }

    @Test
    public void saveStrategy_nullCounterCurrency_throwsException() {
        Strategy strategy = new Strategy();
        strategy.setName(VALID_NAME);
        strategy.setBaseCurrency(VALID_CURRENCY);
        strategy.setCounterCurrency(null);
        strategy.setTimeframe(VALID_TIMEFRAME);
        strategy.setTimeframeUnit(VALID_TIMEFRAME_UNIT);
        strategy.setRisk(VALID_RISK);
        Assertions.assertThrows(Exception.class, () -> repository.save(strategy));
    }

    @Test
    public void saveStrategy_nullTimeframeUnit_throwsException() {
        Strategy strategy = new Strategy();
        strategy.setName(VALID_NAME);
        strategy.setBaseCurrency(VALID_CURRENCY);
        strategy.setCounterCurrency(VALID_CURRENCY);
        strategy.setTimeframe(VALID_TIMEFRAME);
        strategy.setTimeframeUnit(null);
        strategy.setRisk(VALID_RISK);
        Assertions.assertThrows(Exception.class, () -> repository.save(strategy));
    }

    @Test
    public void saveStrategy_requiredFields_doesNotThrowException() {
        Strategy strategy = new Strategy();
        strategy.setName(VALID_NAME);
        strategy.setBaseCurrency(VALID_CURRENCY);
        strategy.setCounterCurrency(VALID_CURRENCY);
        strategy.setTimeframe(VALID_TIMEFRAME);
        strategy.setTimeframeUnit(VALID_TIMEFRAME_UNIT);
        strategy.setRisk(VALID_RISK);
        Assertions.assertDoesNotThrow(() -> repository.save(strategy));
    }

}
