package com.georgefitzpatrick.trading.crypto.ui.filter;

import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

import java.util.List;

/**
 * @author George Fitzpatrick
 */
public class IntegerKeyFilter implements EventHandler<KeyEvent> {

    /* ----- Fields ----- */

    private static final List<String> NUMBERS = List.of("1", "2", "3", "4", "5", "6", "7", "8", "9", "0");

    /* ----- Methods ----- */

    @Override
    public void handle(KeyEvent event) {
        if (!NUMBERS.contains(event.getCharacter())) {
            event.consume();
        }
    }

}
