package com.georgefitzpatrick.trading.crypto.ui;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.css.PseudoClass;

import java.util.function.Predicate;

/**
 * @author George Fitzpatrick
 */
public interface Validated<T> {

    PseudoClass INVALID_PSEUDO_CLASS = PseudoClass.getPseudoClass("invalid");

    default Predicate<T> getValidator() {
        return validatorProperty().get();
    }

    default void setValidator(Predicate<T> validator) {
        validatorProperty().set(validator);
    }

    ObjectProperty<Predicate<T>> validatorProperty();

    default boolean isValid() {
        return validProperty().get();
    }

    ReadOnlyBooleanProperty validProperty();

}
