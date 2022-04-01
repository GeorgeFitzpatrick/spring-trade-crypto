package com.georgefitzpatrick.trading.crypto.ui.binding;

import com.georgefitzpatrick.trading.crypto.data.entity.Condition;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

import static com.georgefitzpatrick.trading.crypto.data.entity.Condition.Action;

/**
 * @author George Fitzpatrick
 */
public class RequiredActionsBinding extends BooleanBinding {

    /* ----- Fields ----- */

    private final ObservableList<Condition> conditions;
    private final List<Action> actions;

    /* ----- Constructors ----- */

    public RequiredActionsBinding(ObservableList<Condition> conditions, Action... actions) {
        this.conditions = conditions;
        this.actions = List.of(actions);
        bind(conditions);
    }

    /* ----- Methods ----- */

    @Override
    public void dispose() {
        unbind(conditions);
    }

    @Override
    public ObservableList<?> getDependencies() {
        return FXCollections.singletonObservableList(conditions);
    }

    @Override
    protected boolean computeValue() {
        List<Action> provided = conditions.stream().map(Condition::getAction).distinct().toList();
        return provided.containsAll(actions);
    }

}
