CREATE TABLE IF NOT EXISTS exchange_credentials
(
    id             LONG AUTO_INCREMENT PRIMARY KEY NOT NULL,
    name           VARCHAR                         NOT NULL,
    exchange_class VARCHAR                         NOT NULL,
    username       VARCHAR,
    password       VARCHAR,
    secret_key     VARCHAR,
    api_key        VARCHAR,
    passphrase     VARCHAR,
    sandbox        BOOLEAN                         NOT NULL
);

CREATE TABLE IF NOT EXISTS strategy
(
    id                      LONG AUTO_INCREMENT PRIMARY KEY NOT NULL,
    name                    VARCHAR                         NOT NULL,
    base_currency           VARCHAR                         NOT NULL,
    counter_currency        VARCHAR                         NOT NULL,
    timeframe               INTEGER                         NOT NULL,
    timeframe_unit          VARCHAR                         NOT NULL,
    risk                    DECIMAL                         NOT NULL,
    exchange_credentials_id LONG
);

CREATE TABLE IF NOT EXISTS condition
(
    id                LONG AUTO_INCREMENT PRIMARY KEY NOT NULL,
    action            VARCHAR                         NOT NULL,
    base_period       INTEGER                         NOT NULL,
    base_indicator    VARCHAR                         NOT NULL,
    relationship      VARCHAR                         NOT NULL,
    counter_period    INTEGER                         NOT NULL,
    counter_indicator VARCHAR                         NOT NULL,
    strategy_id       LONG
);

CREATE TABLE IF NOT EXISTS accessibility
(
    id     LONG AUTO_INCREMENT PRIMARY KEY NOT NULL,
    locale VARCHAR                         NOT NULL
);