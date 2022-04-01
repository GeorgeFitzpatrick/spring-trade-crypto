package com.georgefitzpatrick.trading.crypto.ui.controller;

import com.georgefitzpatrick.trading.crypto.data.entity.Condition;
import com.georgefitzpatrick.trading.crypto.data.entity.Strategy;
import com.georgefitzpatrick.trading.crypto.data.service.ConditionService;
import com.georgefitzpatrick.trading.crypto.data.service.StrategyService;
import com.georgefitzpatrick.trading.crypto.strategy.ScheduledStrategyService;
import com.georgefitzpatrick.trading.crypto.ui.FormInitializer;
import com.georgefitzpatrick.trading.crypto.ui.I18n;
import com.georgefitzpatrick.trading.crypto.ui.control.WorkerStateLabel;
import com.georgefitzpatrick.trading.crypto.ui.factory.ConditionCellFactory;
import com.georgefitzpatrick.trading.crypto.ui.factory.ScheduledStrategyServiceCellFactory;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.georgefitzpatrick.trading.crypto.data.entity.Condition.Action.BUY;
import static com.georgefitzpatrick.trading.crypto.data.entity.Condition.Action.SELL;

/**
 * @author George Fitzpatrick
 */
@Component
public class StrategiesController {

    /* ----- Fields ----- */

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
            .withZone(ZoneId.systemDefault());

    private final I18n i18n;
    private final ScheduledStrategyServiceCellFactory scheduledStrategyServiceCellFactory;
    private final ConditionCellFactory conditionCellFactory;
    private final FormInitializer formInitializer;
    private final ConditionService conditionService;
    private final StrategyService strategyService;

    @FXML
    private TextField filterTextField;

    @FXML
    private ListView<ScheduledStrategyService> servicesListView;

    @FXML
    private Node detail;

    @FXML
    private Label nameLabel, buyConditionsLabel, sellConditionsLabel, eventsLabel;

    @FXML
    private WorkerStateLabel statusLabel;

    @FXML
    private Button newBuyConditionButton, newSellConditionButton;

    @FXML
    private ListView<Condition> buyConditionsListView, sellConditionsListView;

    @FXML
    private TableView<ScheduledStrategyService.Event> eventsTableView;

    @FXML
    private TableColumn<ScheduledStrategyService.Event, String> timestampColumn, messageColumn;

    private ObservableList<ScheduledStrategyService> services;
    private FilteredList<ScheduledStrategyService> filteredServices;

    /* ----- Constructors ----- */

    @Autowired
    public StrategiesController(I18n i18n, ScheduledStrategyServiceCellFactory scheduledStrategyServiceCellFactory,
                                ConditionCellFactory conditionCellFactory, FormInitializer formInitializer,
                                ConditionService conditionService, StrategyService strategyService) {
        this.i18n = i18n;
        this.scheduledStrategyServiceCellFactory = scheduledStrategyServiceCellFactory;
        this.conditionCellFactory = conditionCellFactory;
        this.formInitializer = formInitializer;
        this.conditionService = conditionService;
        this.strategyService = strategyService;
    }

    /* ----- Methods ----- */

    @FXML
    private void initialize() {
        // localise text
        filterTextField.promptTextProperty().bind(i18n.string("filter"));
        eventsLabel.textProperty().bind(i18n.string("events"));
        timestampColumn.textProperty().bind(i18n.string("timestamp"));
        messageColumn.textProperty().bind(i18n.string("message"));
        buyConditionsLabel.textProperty().bind(i18n.string("buyConditions"));
        sellConditionsLabel.textProperty().bind(i18n.string("sellConditions"));

        // customise using cell factories
        servicesListView.setCellFactory(scheduledStrategyServiceCellFactory);
        buyConditionsListView.setCellFactory(conditionCellFactory);
        sellConditionsListView.setCellFactory(conditionCellFactory);

        // map strategy event to property associated with column
        timestampColumn.setCellValueFactory(row -> i18n.time(row.getValue().timestamp(), DATE_TIME_FORMATTER));
        messageColumn.setCellValueFactory(row -> i18n.string(row.getValue().localizedMessageKey()));

        // hide service detail if no service selected
        detail.visibleProperty().bind(servicesListView.getSelectionModel().selectedItemProperty().isNotNull());

        // update service detail when new service selected
        servicesListView.getSelectionModel().selectedItemProperty().addListener(observable -> updateDetail());

        // add all exchange credentials from database as services after wrapping in filterable list
        List<Strategy> strategies = strategyService.findAll();
        services = FXCollections.observableList(
                strategies.stream().map(ScheduledStrategyService::new).collect(Collectors.toList()),
                service -> new Observable[]{service.strategy().nameProperty()});
        filteredServices = new FilteredList<>(services);
        servicesListView.setItems(filteredServices);
    }

    @FXML
    private void filterChanged() {
        String filter = filterTextField.getText().toLowerCase();
        filteredServices.setPredicate(service -> service.strategy().getName().toLowerCase().contains(filter));
    }

    @FXML
    private void newStrategy() throws IOException {
        Resource form = new ClassPathResource("view/form/StrategyFormView.fxml");
        Optional<Strategy> result = formInitializer.showAndWait(form);
        if (result.isPresent()) {
            strategyService.save(result.get());
            services.add(new ScheduledStrategyService(result.get()));
        }
    }

    private void updateDetail() {
        ScheduledStrategyService service = servicesListView.getSelectionModel().getSelectedItem();
        if (service == null) {
            // destroy strategy bindings if no strategy selected
            nameLabel.textProperty().unbind();
            statusLabel.textProperty().unbind();
            statusLabel.textFillProperty().unbind();
            buyConditionsListView.setItems(FXCollections.emptyObservableList());
            sellConditionsListView.setItems(FXCollections.emptyObservableList());
            eventsTableView.setItems(FXCollections.emptyObservableList());
        } else {
            // update strategy bindings if new strategy selected
            nameLabel.textProperty().bind(service.strategy().nameProperty());
            statusLabel.textProperty().bind(service.stateProperty().asString());
            statusLabel.stateProperty().bind(service.stateProperty());

            newBuyConditionButton.disableProperty().bind(service.runningProperty());
            newSellConditionButton.disableProperty().bind(service.runningProperty());

            FilteredList<Condition> buyConditions = new FilteredList<>(service.strategy().conditionsProperty());
            buyConditions.setPredicate(condition -> condition.getAction() == BUY);
            buyConditionsListView.setItems(buyConditions);

            FilteredList<Condition> sellConditions = new FilteredList<>(service.strategy().conditionsProperty());
            sellConditions.setPredicate(condition -> condition.getAction() == SELL);
            sellConditionsListView.setItems(sellConditions);

            SortedList<ScheduledStrategyService.Event> events = new SortedList<>(service.eventsProperty());
            events.setComparator(Comparator.comparing(ScheduledStrategyService.Event::timestamp).reversed());
            eventsTableView.setItems(events);
        }
    }

    @FXML
    private void newCondition() throws IOException {
        Strategy strategy = servicesListView.getSelectionModel().getSelectedItem().strategy();
        Resource form = new ClassPathResource("view/form/ConditionFormView.fxml");
        Optional<Condition> result = formInitializer.showAndWait(form);
        if (result.isPresent()) {
            strategy.addCondition(result.get());
            conditionService.save(result.get());
            strategyService.save(strategy);
        }
    }

}
