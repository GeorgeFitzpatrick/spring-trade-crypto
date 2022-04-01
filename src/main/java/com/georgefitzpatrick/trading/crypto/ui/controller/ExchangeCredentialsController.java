package com.georgefitzpatrick.trading.crypto.ui.controller;

import com.georgefitzpatrick.trading.crypto.data.entity.ExchangeCredentials;
import com.georgefitzpatrick.trading.crypto.data.service.ExchangeCredentialsService;
import com.georgefitzpatrick.trading.crypto.ui.FormInitializer;
import com.georgefitzpatrick.trading.crypto.ui.I18n;
import com.georgefitzpatrick.trading.crypto.ui.factory.ExchangeCredentialsRowFactory;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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
public class ExchangeCredentialsController {

    /* ----- Fields ----- */

    private final I18n i18n;
    private final ExchangeCredentialsRowFactory exchangeCredentialsRowFactory;
    private final FormInitializer formInitializer;
    private final ExchangeCredentialsService exchangeCredentialsService;

    @FXML
    private TextField filterTextField;

    @FXML
    private TableView<ExchangeCredentials> tableView;

    @FXML
    private TableColumn<ExchangeCredentials, String> nameColumn, exchangeColumn;

    @FXML
    public TableColumn<ExchangeCredentials, Boolean> sandboxColumn;

    private ObservableList<ExchangeCredentials> credentials;
    private FilteredList<ExchangeCredentials> filteredCredentials;

    /* ----- Constructors ----- */

    @Autowired
    public ExchangeCredentialsController(I18n i18n, ExchangeCredentialsRowFactory exchangeCredentialsRowFactory,
                                         FormInitializer formInitializer,
                                         ExchangeCredentialsService exchangeCredentialsService) {
        this.i18n = i18n;
        this.exchangeCredentialsRowFactory = exchangeCredentialsRowFactory;
        this.formInitializer = formInitializer;
        this.exchangeCredentialsService = exchangeCredentialsService;
    }

    /* ----- Methods ----- */

    @FXML
    private void initialize() {
        // localise text
        filterTextField.promptTextProperty().bind(i18n.string("filter"));
        nameColumn.textProperty().bind(i18n.string("name"));
        exchangeColumn.textProperty().bind(i18n.string("exchange"));
        sandboxColumn.textProperty().bind(i18n.string("sandbox"));

        // map exchange credentials to property associated with column
        nameColumn.setCellValueFactory(row -> row.getValue().nameProperty());
        exchangeColumn.setCellValueFactory(row -> Bindings.createStringBinding(
                () -> row.getValue().getExchangeClass().getSimpleName().replace("Exchange", ""),
                row.getValue().exchangeClassProperty()));
        sandboxColumn.setCellValueFactory(row -> row.getValue().sandboxProperty());

        // customise using row factory
        tableView.setRowFactory(exchangeCredentialsRowFactory);

        // add all exchange credentials from database after wrapping in filterable list
        credentials = FXCollections.observableList(
                exchangeCredentialsService.findAll(),
                credentials -> new Observable[]{credentials.nameProperty()});
        filteredCredentials = new FilteredList<>(credentials);
        tableView.setItems(filteredCredentials);
    }

    @FXML
    private void filterChanged() {
        String filter = filterTextField.getText().toLowerCase();
        filteredCredentials.setPredicate(credentials -> credentials.getName().toLowerCase().contains(filter));
    }

    @FXML
    private void newExchangeCredentials() throws IOException {
        Resource form = new ClassPathResource("view/form/ExchangeCredentialsFormView.fxml");
        Optional<ExchangeCredentials> result = formInitializer.showAndWait(form);
        if (result.isPresent()) {
            exchangeCredentialsService.save(result.get());
            credentials.add(result.get());
        }
    }

}
