package com.georgefitzpatrick.trading.crypto.ui.factory;

import com.georgefitzpatrick.trading.crypto.data.entity.Condition;
import com.georgefitzpatrick.trading.crypto.data.entity.Strategy;
import com.georgefitzpatrick.trading.crypto.data.service.ConditionService;
import com.georgefitzpatrick.trading.crypto.data.service.StrategyService;
import com.georgefitzpatrick.trading.crypto.ui.FormInitializer;
import com.georgefitzpatrick.trading.crypto.ui.I18n;
import com.georgefitzpatrick.trading.crypto.ui.binding.AggregateStringBinding;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.util.Callback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

/**
 * @author George Fitzpatrick
 */
@Component
public class ConditionCellFactory implements Callback<ListView<Condition>, ListCell<Condition>> {

    /* ----- Fields ----- */

    private final I18n i18n;
    private final FormInitializer formInitializer;
    private final ConditionService conditionService;
    private final StrategyService strategyService;

    /* ----- Constructors ----- */

    @Autowired
    public ConditionCellFactory(I18n i18n, FormInitializer formInitializer,
                                ConditionService conditionService, StrategyService strategyService) {
        this.i18n = i18n;
        this.formInitializer = formInitializer;
        this.conditionService = conditionService;
        this.strategyService = strategyService;
    }

    /* ----- Methods ----- */

    @Override
    public ListCell<Condition> call(ListView<Condition> conditionListView) {
        return new ConditionCell();
    }

    /* ----- Classes ----- */

    private class ConditionCell extends ListCell<Condition> {

        /* ----- Fields ----- */

        private final ContextMenu menu;

        /* ----- Constructors ----- */

        public ConditionCell() {
            MenuItem edit = new MenuItem();
            edit.textProperty().bind(i18n.string("edit"));
            edit.setOnAction(event -> edit());

            MenuItem delete = new MenuItem();
            delete.textProperty().bind(i18n.string("delete"));
            delete.setOnAction(event -> delete());

            menu = new ContextMenu(edit, delete);
        }

        /* ----- Methods ----- */

        private void edit() {
            try {
                Resource form = new ClassPathResource("view/form/ConditionFormView.fxml");
                Optional<Condition> result = formInitializer.showAndWait(form, getItem());
                result.ifPresent(conditionService::save);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private void delete() {
            Condition condition = getItem();
            Strategy strategy = condition.getStrategy();
            strategy.removeCondition(condition);
            strategyService.save(strategy);
            conditionService.delete(condition);
            getListView().getItems().remove(condition);
        }

        @Override
        protected void updateItem(Condition condition, boolean empty) {
            super.updateItem(condition, empty);
            if (empty) {
                textProperty().unbind();
                setText(null);
                setContextMenu(null);
            } else {
                AggregateStringBinding description = new AggregateStringBinding();
                description.addDependency(condition.basePeriodProperty().asString());
                description.addDependency(i18n.string("period"));
                description.addDependency(condition.baseIndicatorProperty().asString());
                description.addDependency(condition.relationshipProperty().asString());
                description.addDependency(condition.counterPeriodProperty().asString());
                description.addDependency(i18n.string("period"));
                description.addDependency(condition.counterIndicatorProperty().asString());
                textProperty().bind(description);
                setContextMenu(menu);
            }
        }

    }

}
