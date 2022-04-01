package com.georgefitzpatrick.trading.crypto.data.repository;

import com.georgefitzpatrick.trading.crypto.data.entity.ExchangeCredentials;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.meta.ExchangeMetaData;
import org.knowm.xchange.exceptions.ExchangeException;
import org.knowm.xchange.service.account.AccountService;
import org.knowm.xchange.service.marketdata.MarketDataService;
import org.knowm.xchange.service.trade.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import si.mazi.rescu.SynchronizedValueFactory;

import java.util.List;

/**
 * @author George Fitzpatrick
 */
@SpringBootTest
@SuppressWarnings("ClassCanBeRecord")
public class ExchangeCredentialsRepositoryIntegrationTest {

    /* ----- Fields ----- */

    private static final String VALID_NAME = "name";
    private static final Class<? extends Exchange> VALID_EXCHANGE_CLASS = MockExchange.class;
    private static final boolean VALID_SANDBOX = true;

    private final ExchangeCredentialsRepository repository;

    /* ----- Constructors ----- */

    @Autowired
    public ExchangeCredentialsRepositoryIntegrationTest(ExchangeCredentialsRepository repository) {
        this.repository = repository;
    }

    /* ----- Methods ----- */

    @Test
    public void saveExchangeCredentials_nullName_throwsException() {
        ExchangeCredentials credentials = new ExchangeCredentials();
        credentials.setName(null);
        credentials.setExchangeClass(VALID_EXCHANGE_CLASS);
        credentials.setSandbox(VALID_SANDBOX);
        Assertions.assertThrows(Exception.class, () -> repository.save(credentials));
    }

    @Test
    public void saveExchangeCredentials_nullExchangeClass_throwsException() {
        ExchangeCredentials credentials = new ExchangeCredentials();
        credentials.setName(VALID_NAME);
        credentials.setExchangeClass(null);
        credentials.setSandbox(VALID_SANDBOX);
        Assertions.assertThrows(Exception.class, () -> repository.save(credentials));
    }

    @Test
    public void saveExchangeCredentials_requiredFields_doesNotThrowException() {
        ExchangeCredentials credentials = new ExchangeCredentials();
        credentials.setName(VALID_NAME);
        credentials.setExchangeClass(VALID_EXCHANGE_CLASS);
        credentials.setSandbox(VALID_SANDBOX);
        Assertions.assertDoesNotThrow(() -> repository.save(credentials));
    }

    /* ----- Methods ----- */

    private static class MockExchange implements Exchange {

        @Override
        public ExchangeSpecification getExchangeSpecification() {
            throw new UnsupportedOperationException();
        }

        @Override
        public ExchangeMetaData getExchangeMetaData() {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<CurrencyPair> getExchangeSymbols() {
            throw new UnsupportedOperationException();
        }

        @Override
        public SynchronizedValueFactory<Long> getNonceFactory() {
            throw new UnsupportedOperationException();
        }

        @Override
        public ExchangeSpecification getDefaultExchangeSpecification() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void applySpecification(ExchangeSpecification exchangeSpecification) {
            throw new UnsupportedOperationException();
        }

        @Override
        public MarketDataService getMarketDataService() {
            throw new UnsupportedOperationException();
        }

        @Override
        public TradeService getTradeService() {
            throw new UnsupportedOperationException();
        }

        @Override
        public AccountService getAccountService() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void remoteInit() throws ExchangeException {
            throw new UnsupportedOperationException();
        }

    }

}
