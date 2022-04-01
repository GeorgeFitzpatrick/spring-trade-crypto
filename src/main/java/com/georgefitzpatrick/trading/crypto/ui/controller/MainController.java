package com.georgefitzpatrick.trading.crypto.ui.controller;

import com.georgefitzpatrick.trading.crypto.data.entity.Accessibility;
import com.georgefitzpatrick.trading.crypto.data.service.AccessibilityService;
import com.georgefitzpatrick.trading.crypto.ui.I18n;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Optional;

/**
 * @author George Fitzpatrick
 */
@Component
public class MainController {

    /* ----- Fields ----- */

    private final I18n i18n;
    private final AccessibilityService accessibilityService;

    @FXML
    private Tab strategiesTab, exchangesTab;

    @FXML
    private ComboBox<Locale> localeComboBox;

    /* ----- Constructors ----- */

    @Autowired
    public MainController(I18n i18n, AccessibilityService accessibilityService) {
        this.i18n = i18n;
        this.accessibilityService = accessibilityService;
    }

    /* ----- Methods ----- */

    @FXML
    private void initialize() {
        // localise text
        strategiesTab.textProperty().bind(i18n.string("strategies"));
        exchangesTab.textProperty().bind(i18n.string("exchangeCredentials"));

        // add supported locales to "localeCombobox"
        localeComboBox.getItems().addAll(I18n.SUPPORTED_LOCALES);

        // register listener to save new locale selection when it changes
        localeComboBox.valueProperty().addListener(observable -> saveLocale());

        // set locale from database
        accessibilityService.find().ifPresent(accessibility -> localeComboBox.setValue(accessibility.getLocale()));
    }

    private void saveLocale() {
        // get locale selection
        Locale locale = localeComboBox.getValue();

        // save new locale value to database
        Accessibility accessibility = new Accessibility();
        accessibility.setLocale(locale);
        accessibilityService.save(accessibility);

        // update locale for localised string bindings
        i18n.setLocale(locale);
    }

}
