package com.georgefitzpatrick.trading.crypto.ui.factory;

import com.georgefitzpatrick.trading.crypto.data.entity.ExchangeCredentials;
import com.georgefitzpatrick.trading.crypto.data.service.ExchangeCredentialsService;
import com.georgefitzpatrick.trading.crypto.ui.FormInitializer;
import com.georgefitzpatrick.trading.crypto.ui.I18n;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
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
public class ExchangeCredentialsRowFactory implements Callback<TableView<ExchangeCredentials>, TableRow<ExchangeCredentials>> {

    /* ----- Fields ----- */

    private final I18n i18n;
    private final FormInitializer formInitializer;
    private final ExchangeCredentialsService exchangeCredentialsService;

    /* ----- Constructors ----- */

    @Autowired
    public ExchangeCredentialsRowFactory(I18n i18n, FormInitializer formInitializer,
                                         ExchangeCredentialsService exchangeCredentialsService) {
        this.i18n = i18n;
        this.formInitializer = formInitializer;
        this.exchangeCredentialsService = exchangeCredentialsService;
    }

    /* ----- Methods ----- */

    @Override
    public TableRow<ExchangeCredentials> call(TableView<ExchangeCredentials> exchangeTableView) {
        return new ExchangeCredentialsRow();
    }

    /* ----- Classes ----- */

    private class ExchangeCredentialsRow extends TableRow<ExchangeCredentials> {

        /* ----- Fields ----- */

        private final ContextMenu menu;

        /* ----- Constructors ----- */

        public ExchangeCredentialsRow() {
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
                Resource form = new ClassPathResource("view/form/ExchangeCredentialsFormView.fxml");
                Optional<ExchangeCredentials> result = formInitializer.showAndWait(form, getItem());
                result.ifPresent(exchangeCredentialsService::save);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @SuppressWarnings("unchecked")
        private void delete() {
            ExchangeCredentials exchangeCredentials = getItem();
            exchangeCredentialsService.delete(exchangeCredentials);
            FilteredList<ExchangeCredentials> items =
                    (FilteredList<ExchangeCredentials>) getTableView().getItems();
            ObservableList<ExchangeCredentials> source =
                    (ObservableList<ExchangeCredentials>) items.getSource();
            source.remove(exchangeCredentials);
        }

        @Override
        protected void updateItem(ExchangeCredentials exchangeCredentials, boolean empty) {
            super.updateItem(exchangeCredentials, empty);
            if (empty) {
                setContextMenu(null);
            } else {
                setContextMenu(menu);
            }
        }

    }

}
