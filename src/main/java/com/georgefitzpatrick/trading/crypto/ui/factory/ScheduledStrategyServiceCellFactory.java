package com.georgefitzpatrick.trading.crypto.ui.factory;

import com.georgefitzpatrick.trading.crypto.data.entity.Condition;
import com.georgefitzpatrick.trading.crypto.data.entity.Strategy;
import com.georgefitzpatrick.trading.crypto.data.service.StrategyService;
import com.georgefitzpatrick.trading.crypto.strategy.ScheduledStrategyService;
import com.georgefitzpatrick.trading.crypto.ui.FormInitializer;
import com.georgefitzpatrick.trading.crypto.ui.I18n;
import com.georgefitzpatrick.trading.crypto.ui.binding.RequiredActionsBinding;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Worker;
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

import static com.georgefitzpatrick.trading.crypto.data.entity.Condition.Action.BUY;
import static com.georgefitzpatrick.trading.crypto.data.entity.Condition.Action.SELL;

/**
 * @author George Fitzpatrick
 */
@Component
public class ScheduledStrategyServiceCellFactory implements Callback<ListView<ScheduledStrategyService>, ListCell<ScheduledStrategyService>> {

    /* ----- Fields ----- */

    private final I18n i18n;
    private final FormInitializer formInitializer;
    private final StrategyService strategyService;

    /* ----- Constructors ----- */

    @Autowired
    public ScheduledStrategyServiceCellFactory(I18n i18n,
                                               FormInitializer formInitializer, StrategyService strategyService) {
        this.i18n = i18n;
        this.formInitializer = formInitializer;
        this.strategyService = strategyService;
    }

    /* ----- Methods ----- */

    @Override
    public ListCell<ScheduledStrategyService> call(ListView<ScheduledStrategyService> conditionListView) {
        return new ScheduledStrategyServiceCell();
    }

    /* ----- Classes ----- */

    private class ScheduledStrategyServiceCell extends ListCell<ScheduledStrategyService> {

        /* ----- Fields ----- */

        private final MenuItem start, pause, edit, delete;
        private final ContextMenu menu;

        /* ----- Constructors ----- */

        public ScheduledStrategyServiceCell() {
            start = new MenuItem();
            start.textProperty().bind(i18n.string("start"));
            start.setOnAction(event -> start());

            pause = new MenuItem();
            pause.textProperty().bind(i18n.string("pause"));
            pause.setOnAction(event -> pause());

            edit = new MenuItem();
            edit.textProperty().bind(i18n.string("edit"));
            edit.setOnAction(event -> edit());

            delete = new MenuItem();
            delete.textProperty().bind(i18n.string("delete"));
            delete.setOnAction(event -> delete());

            menu = new ContextMenu(start, pause, edit, delete);
        }

        /* ----- Methods ----- */

        private void start() {
            // if service is not running start it
            ScheduledStrategyService service = getItem();
            if (service.getState() != Worker.State.RUNNING && service.getState() != Worker.State.SCHEDULED) {
                service.reset();
                service.start();
            }
        }

        private void pause() {
            // if service is running cancel it
            ScheduledStrategyService service = getItem();
            if (service.getState() == Worker.State.RUNNING || service.getState() == Worker.State.SCHEDULED) {
                service.cancel();
            }
        }

        private void edit() {
            try {
                Resource form = new ClassPathResource("view/form/StrategyFormView.fxml");
                Optional<Strategy> result = formInitializer.showAndWait(form, getItem().strategy());
                result.ifPresent(strategyService::save);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @SuppressWarnings("unchecked")
        private void delete() {
            ScheduledStrategyService service = getItem();
            // todo delete orphaned conditions and events
            strategyService.delete(service.strategy());
            FilteredList<ScheduledStrategyService> items =
                    (FilteredList<ScheduledStrategyService>) getListView().getItems();
            ObservableList<ScheduledStrategyService> source =
                    (ObservableList<ScheduledStrategyService>) items.getSource();
            source.remove(service);
        }

        @Override
        protected void updateItem(ScheduledStrategyService service, boolean empty) {
            super.updateItem(service, empty);
            if (empty) {
                textProperty().unbind();
                setText(null);
                setContextMenu(null);
            } else {
                // enabled/disable context actions based on state of service
                ListProperty<Condition> conditions = service.strategy().conditionsProperty();
                BooleanBinding requiredActions = new RequiredActionsBinding(conditions, BUY, SELL);
                start.disableProperty().bind(requiredActions.not().or(service.runningProperty()));
                pause.disableProperty().bind(service.runningProperty().not());
                edit.disableProperty().bind(service.runningProperty());
                delete.disableProperty().bind(service.runningProperty());
                textProperty().bind(service.strategy().nameProperty());
                setContextMenu(menu);
            }
        }

    }

}
