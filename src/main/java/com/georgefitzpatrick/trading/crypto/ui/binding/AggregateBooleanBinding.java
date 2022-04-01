package com.georgefitzpatrick.trading.crypto.ui.binding;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * @author George Fitzpatrick
 */
public class AggregateBooleanBinding extends BooleanBinding {

    /* ----- Fields ----- */

    private final ObservableList<ObservableValue<Boolean>> dependencies;

    /* ----- Constructors ----- */

    public AggregateBooleanBinding() {
        this.dependencies = FXCollections.observableArrayList();
        bind(dependencies);
    }

    /* ----- Methods ----- */

    @Override
    public void dispose() {
        unbind(dependencies);
    }

    @Override
    public ObservableList<?> getDependencies() {
        return dependencies;
    }

    @Override
    protected boolean computeValue() {
        return dependencies.stream().allMatch(dependency -> dependency == null || dependency.getValue());
    }

    public void addDependencies(ObservableValue<Boolean>... dependencies) {
        this.dependencies.addAll(dependencies);
        this.bind(dependencies);
    }

    public void removeDependencies(ObservableValue<Boolean>... dependencies) {
        this.dependencies.removeAll(dependencies);
        this.unbind(dependencies);
    }

}
