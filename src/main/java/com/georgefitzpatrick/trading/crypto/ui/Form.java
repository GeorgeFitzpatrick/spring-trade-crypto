package com.georgefitzpatrick.trading.crypto.ui;

import javafx.beans.binding.BooleanBinding;

/**
 * @author George Fitzpatrick
 */
public interface Form<R> {

    R getValue();

    void setValue(R value);

    BooleanBinding validBinding();

}
