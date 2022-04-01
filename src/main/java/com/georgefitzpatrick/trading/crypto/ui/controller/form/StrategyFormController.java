package com.georgefitzpatrick.trading.crypto.ui.controller.form;

import com.georgefitzpatrick.trading.crypto.data.entity.ExchangeCredentials;
import com.georgefitzpatrick.trading.crypto.data.entity.Strategy;
import com.georgefitzpatrick.trading.crypto.data.service.ExchangeCredentialsService;
import com.georgefitzpatrick.trading.crypto.ui.Form;
import com.georgefitzpatrick.trading.crypto.ui.I18n;
import com.georgefitzpatrick.trading.crypto.ui.binding.AggregateBooleanBinding;
import com.georgefitzpatrick.trading.crypto.ui.control.HintLabel;
import com.georgefitzpatrick.trading.crypto.ui.control.ValidatedComboBox;
import com.georgefitzpatrick.trading.crypto.ui.control.ValidatedSpinner;
import com.georgefitzpatrick.trading.crypto.ui.control.ValidatedTextField;
import com.georgefitzpatrick.trading.crypto.ui.filter.DoubleKeyFilter;
import com.georgefitzpatrick.trading.crypto.ui.filter.IntegerKeyFilter;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import org.knowm.xchange.currency.Currency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

import static java.time.temporal.ChronoUnit.DAYS;
import static javafx.scene.control.SpinnerValueFactory.DoubleSpinnerValueFactory;
import static javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import static javafx.scene.input.KeyEvent.KEY_TYPED;
import static org.knowm.xchange.currency.Currency.*;

/**
 * @author George Fitzpatrick
 */
@Component
public class StrategyFormController implements Form<Strategy> {

    /* ----- Fields ----- */

    private static final List<Currency> SUPPORTED_CURRENCIES = List.of(
            ADA, BTC, ETH, EUR, GBP, Currency.getInstance("KCS"), LUNA, USD, USDT, XRP
    );

    private final AggregateBooleanBinding valid;
    private final I18n i18n;
    private final ExchangeCredentialsService exchangeCredentialsService;

    @FXML
    private HintLabel nameLabel, baseCurrencyLabel, counterCurrencyLabel,
            timeframeLabel, timeframeUnitLabel, exchangeCredentialsLabel, riskLabel;

    @FXML
    private ValidatedTextField name;

    @FXML
    private ValidatedComboBox<ExchangeCredentials> exchangeCredentials;

    @FXML
    private ValidatedComboBox<Currency> baseCurrency, counterCurrency;

    @FXML
    private ValidatedSpinner<Integer> timeframe;

    @FXML
    private ValidatedComboBox<ChronoUnit> timeframeUnit;

    @FXML
    private ValidatedSpinner<Double> risk;

    private Strategy editing;

    /* ----- Constructors ----- */

    @Autowired
    public StrategyFormController(I18n i18n, ExchangeCredentialsService exchangeCredentialsService) {
        this.valid = new AggregateBooleanBinding();
        this.i18n = i18n;
        this.exchangeCredentialsService = exchangeCredentialsService;
    }

    /* ----- Methods ----- */

    @FXML
    private void initialize() {
        // localise labels
        nameLabel.textProperty().bind(i18n.string("name"));
        exchangeCredentialsLabel.textProperty().bind(i18n.string("exchangeCredentials"));
        baseCurrencyLabel.textProperty().bind(i18n.string("baseCurrency"));
        counterCurrencyLabel.textProperty().bind(i18n.string("counterCurrency"));
        timeframeLabel.textProperty().bind(i18n.string("timeframe"));
        timeframeUnitLabel.textProperty().bind(i18n.string("timeframeUnit"));
        riskLabel.textProperty().bind(i18n.string("risk"));

        // configure spinner limits and initial values
        timeframe.setValueFactory(new IntegerSpinnerValueFactory(1, Integer.MAX_VALUE));
        risk.setValueFactory(new DoubleSpinnerValueFactory(Double.MIN_VALUE, 100.0, 1.0));

        // configure spinner input filters to only allow integers
        timeframe.addEventFilter(KEY_TYPED, new IntegerKeyFilter());
        risk.addEventFilter(KEY_TYPED, new DoubleKeyFilter());

        // customise combo boxes
        exchangeCredentials.setButtonCell(new ExchangeCredentialsCell());
        exchangeCredentials.setCellFactory(listView -> new ExchangeCredentialsCell());
        baseCurrency.setButtonCell(new CurrencyCell());
        baseCurrency.setCellFactory(listView -> new CurrencyCell());
        counterCurrency.setButtonCell(new CurrencyCell());
        counterCurrency.setCellFactory(listView -> new CurrencyCell());

        // populate combo boxes
        baseCurrency.getItems().addAll(SUPPORTED_CURRENCIES);
        counterCurrency.getItems().addAll(SUPPORTED_CURRENCIES);
        timeframeUnit.getItems().addAll(ChronoUnit.values());
        exchangeCredentials.getItems().addAll(List.of(/* exchange credentials... */));

        // add validators to input fields
        name.setValidator(name -> name != null && !name.isBlank());
        exchangeCredentials.setValidator(Objects::nonNull);
        baseCurrency.setValidator(currency -> currency != null && !currency.equals(counterCurrency.getValue()));
        counterCurrency.setValidator(currency -> currency != null && !currency.equals(baseCurrency.getValue()));
        timeframe.setValidator(Objects::nonNull);
        timeframeUnit.setValidator(Objects::nonNull);
        risk.setValidator(Objects::nonNull);

        // add validation hints to input field labels
        nameLabel.hintingProperty().bind(name.validProperty().not());
        exchangeCredentialsLabel.hintingProperty().bind(exchangeCredentials.validProperty().not());
        baseCurrencyLabel.hintingProperty().bind(baseCurrency.validProperty().not());
        counterCurrencyLabel.hintingProperty().bind(counterCurrency.validProperty().not());
        timeframeLabel.hintingProperty().bind(timeframe.validProperty().not());
        timeframeUnitLabel.hintingProperty().bind(timeframeUnit.validProperty().not());
        riskLabel.hintingProperty().bind(risk.validProperty().not());

        // combine individual valid properties into one property for whole form
        valid.addDependencies(name.validProperty(), exchangeCredentials.validProperty(),
                baseCurrency.validProperty(), counterCurrency.validProperty(),
                timeframe.validProperty(), timeframeUnit.validProperty(), risk.validProperty());
    }

    @Override
    public Strategy getValue() {
        // if not editing create new value otherwise update existing
        Strategy value = editing != null ? editing : new Strategy();
        value.setName(name.getText());
        value.setExchangeCredentials(exchangeCredentials.getValue());
        value.setBaseCurrency(baseCurrency.getValue());
        value.setCounterCurrency(counterCurrency.getValue());
        value.setTimeframe(timeframe.getValue());
        value.setTimeframeUnit(timeframeUnit.getValue());
        value.setRisk(risk.getValue());
        return value;
    }

    @Override
    public void setValue(Strategy value) {
        exchangeCredentials.getItems().setAll(exchangeCredentialsService.findAll());
        if (value == null) {
            // set default values
            name.setText(null);
            exchangeCredentials.getSelectionModel().selectFirst();
            baseCurrency.getSelectionModel().select(BTC);
            counterCurrency.getSelectionModel().select(GBP);
            timeframe.getValueFactory().setValue(1);
            timeframeUnit.getSelectionModel().select(DAYS);
            risk.getValueFactory().setValue(1.0);
        } else {
            // update field values
            name.setText(value.getName());
            exchangeCredentials.setValue(value.getExchangeCredentials());
            baseCurrency.setValue(value.getBaseCurrency());
            counterCurrency.setValue(value.getCounterCurrency());
            timeframe.getValueFactory().setValue(value.getTimeframe());
            timeframeUnit.setValue(value.getTimeframeUnit());
            risk.getValueFactory().setValue(value.getRisk());
        }
        editing = value;
    }

    @Override
    public BooleanBinding validBinding() {
        return valid;
    }

    /* ----- Classes ----- */

    private static class CurrencyCell extends ListCell<Currency> {

        @Override
        protected void updateItem(Currency currency, boolean empty) {
            super.updateItem(currency, empty);
            if (empty) {
                setText(null);
            } else {
                setText(currency.getCurrencyCode());
            }
        }

    }

    private static class ExchangeCredentialsCell extends ListCell<ExchangeCredentials> {

        @Override
        protected void updateItem(ExchangeCredentials exchangeCredentials, boolean empty) {
            super.updateItem(exchangeCredentials, empty);
            if (empty) {
                textProperty().unbind();
                setText(null);
            } else {
                textProperty().bind(exchangeCredentials.nameProperty());
            }
        }

    }

}
