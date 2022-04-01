package com.georgefitzpatrick.trading.crypto.ui.binding;

import javafx.beans.property.SimpleBooleanProperty;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author George Fitzpatrick
 */
public class AggregateBooleanBindingUnitTest {

    @Test
    public void test_noneTrue_false() {
        AggregateBooleanBinding binding = new AggregateBooleanBinding();
        binding.addDependencies(new SimpleBooleanProperty(false));
        Assertions.assertFalse(binding.get());
    }

    @Test
    public void test_someTrue_false() {
        AggregateBooleanBinding binding = new AggregateBooleanBinding();
        binding.addDependencies(new SimpleBooleanProperty(false), new SimpleBooleanProperty(true));
        Assertions.assertFalse(binding.get());
    }

    @Test
    public void test_allTrue_true() {
        AggregateBooleanBinding binding = new AggregateBooleanBinding();
        binding.addDependencies(new SimpleBooleanProperty(true));
        Assertions.assertTrue(binding.get());
    }

}
