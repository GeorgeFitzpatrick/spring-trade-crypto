package com.georgefitzpatrick.trading.crypto.ui.binding;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.function.Predicate;

/**
 * @author George Fitzpatrick
 */
public class ValidBinding<T> extends BooleanBinding {

    /* ----- Fields ----- */

    private final ObservableValue<T> value;
    private final ObservableValue<Predicate<T>> validator;

    /* ----- Constructors ----- */

    public ValidBinding(ObservableValue<T> value, ObservableValue<Predicate<T>> validator) {
        this.value = value;
        this.validator = validator;
        bind(value, validator);
    }

    /* ----- Methods ----- */

    public void dispose() {
        unbind(value, validator);
    }

    public ObservableList<ObservableValue<?>> getDependencies() {
        return FXCollections.observableArrayList(value, validator);
    }

    @Override
    protected boolean computeValue() {
        return validator.getValue() == null || validator.getValue().test(value.getValue());
    }

}
