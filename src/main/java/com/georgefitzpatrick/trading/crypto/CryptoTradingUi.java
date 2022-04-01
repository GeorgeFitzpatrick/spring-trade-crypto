package com.georgefitzpatrick.trading.crypto;

import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author George Fitzpatrick
 */
public class CryptoTradingUi extends Application {

    /* ----- Fields ----- */

    private ConfigurableApplicationContext context;

    /* ----- Methods ----- */

    @Override
    public void init() {
        context = new SpringApplicationBuilder(CryptoTradingApplication.class).run();
    }

    @Override
    public void start(Stage stage) {
        context.publishEvent(stage);
    }

    @Override
    public void stop() {
        context.close();
    }

}
