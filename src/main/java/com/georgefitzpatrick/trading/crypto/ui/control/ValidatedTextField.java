package com.georgefitzpatrick.trading.crypto.ui.control;

import com.georgefitzpatrick.trading.crypto.ui.Validated;
import com.georgefitzpatrick.trading.crypto.ui.binding.ValidBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.TextField;

import java.util.function.Predicate;

/**
 * @author George Fitzpatrick
 */
public class ValidatedTextField extends TextField implements Validated<String> {

    /* ----- Fields ----- */

    private static final String DEFAULT_STYLE_CLASS = "validated-text-field";

    private final ObjectProperty<Predicate<String>> validator;
    private final ReadOnlyBooleanWrapper valid;

    /* ----- Constructors ----- */

    public ValidatedTextField() {
        validator = new SimpleObjectProperty<>(this, "validator");
        valid = new ReadOnlyBooleanWrapper(this, "valid") {
            @Override
            protected void invalidated() {
                pseudoClassStateChanged(INVALID_PSEUDO_CLASS, !get());
            }
        };
        valid.bind(new ValidBinding<>(textProperty(), this.validator));
        getStyleClass().add(DEFAULT_STYLE_CLASS);
    }

    /* ----- Methods ----- */

    @Override
    public ObjectProperty<Predicate<String>> validatorProperty() {
        return validator;
    }

    @Override
    public ReadOnlyBooleanProperty validProperty() {
        return valid.getReadOnlyProperty();
    }

}
