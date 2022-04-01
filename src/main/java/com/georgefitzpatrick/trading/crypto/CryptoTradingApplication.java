package com.georgefitzpatrick.trading.crypto;

import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author George Fitzpatrick
 */
@SpringBootApplication // enables Spring auto-configuration and component scan on launch
public class CryptoTradingApplication {

    public static void main(String[] args) {
        Application.launch(CryptoTradingUi.class, args);
    }

}
