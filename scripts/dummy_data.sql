INSERT INTO exchange_credentials(id, name, exchange_class, username, password, secret_key, api_key, passphrase, sandbox)
values (0, 'Test', 'org.knowm.xchange.kraken.KrakenExchange', NULL, NULL, NULL, NULL, NULL, FALSE);

INSERT INTO strategy(id, name, base_currency, counter_currency, timeframe, timeframe_unit, risk, exchange_credentials_id)
VALUES (0, 'Bollinger Band Mean Reversion' , 'BTC', 'USDT', 1, '7', 1, 0);

INSERT INTO condition(action, base_period, base_indicator, relationship, counter_period, counter_indicator, strategy_id)
VALUES ('0', 2, '0', '3', 14, '4', 0);

INSERT INTO condition(action, base_period, base_indicator, relationship, counter_period, counter_indicator, strategy_id)
VALUES ('1', 2, '0', '4', 14, '5', 0);