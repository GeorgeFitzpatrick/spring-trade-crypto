package com.georgefitzpatrick.trading.crypto.ui.binding;

import javafx.beans.binding.StringBinding;
import javafx.beans.value.ObservableStringValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.StringJoiner;

/**
 * @author George Fitzpatrick
 */
public class AggregateStringBinding extends StringBinding {

    /* ----- Fields ----- */

    private final ObservableList<ObservableValue<String>> dependencies;

    /* ----- Constructors ----- */

    public AggregateStringBinding() {
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
    protected String computeValue() {
        StringJoiner joiner = new StringJoiner(" ");
        for (ObservableValue<String> dependency : dependencies) {
            joiner.add(dependency.getValue());
        }
        return joiner.toString();
    }

    public void addDependency(ObservableValue<String> dependency) {
        dependencies.add(dependency);
        this.bind(dependency);
    }

    public void removeDependency(ObservableValue<String> dependency) {
        dependencies.remove(dependency);
        super.unbind(dependency);
    }

}
