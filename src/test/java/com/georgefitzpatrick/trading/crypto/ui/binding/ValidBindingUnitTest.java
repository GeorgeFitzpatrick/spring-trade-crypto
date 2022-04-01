package com.georgefitzpatrick.trading.crypto.ui.binding;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * @author George Fitzpatrick
 */
public class ValidBindingUnitTest {

    @Test
    public void validate_validatorIsNull_valid() {
        StringProperty value = new SimpleStringProperty("");
        ObjectProperty<Predicate<String>> validator = new SimpleObjectProperty<>(null);
        ValidBinding<String> valid = new ValidBinding<>(value, validator);
        Assertions.assertTrue(valid.get());
    }

    @Test
    public void validate_valueMatchesValidator_valid() {
        StringProperty value = new SimpleStringProperty("");
        ObjectProperty<Predicate<String>> validator = new SimpleObjectProperty<>(Objects::nonNull);
        ValidBinding<String> valid = new ValidBinding<>(value, validator);
        Assertions.assertTrue(valid.get());
    }

    @Test
    public void validate_valueDoesNotMatchValidator_invalid() {
        StringProperty value = new SimpleStringProperty(null);
        ObjectProperty<Predicate<String>> validator = new SimpleObjectProperty<>(Objects::nonNull);
        ValidBinding<String> valid = new ValidBinding<>(value, validator);
        Assertions.assertFalse(valid.get());
    }

}
