package com.georgefitzpatrick.trading.crypto.ui.controller.form;

import com.georgefitzpatrick.trading.crypto.data.entity.ExchangeCredentials;
import com.georgefitzpatrick.trading.crypto.ui.Form;
import com.georgefitzpatrick.trading.crypto.ui.I18n;
import com.georgefitzpatrick.trading.crypto.ui.binding.AggregateBooleanBinding;
import com.georgefitzpatrick.trading.crypto.ui.control.HintLabel;
import com.georgefitzpatrick.trading.crypto.ui.control.ValidatedComboBox;
import com.georgefitzpatrick.trading.crypto.ui.control.ValidatedTextField;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.binance.BinanceExchange;
import org.knowm.xchange.bitfinex.BitfinexExchange;
import org.knowm.xchange.bitmex.BitmexExchange;
import org.knowm.xchange.bitstamp.BitstampExchange;
import org.knowm.xchange.coinbasepro.CoinbaseProExchange;
import org.knowm.xchange.hitbtc.v2.HitbtcExchange;
import org.knowm.xchange.huobi.HuobiExchange;
import org.knowm.xchange.kraken.KrakenExchange;
import org.knowm.xchange.kucoin.KucoinExchange;
import org.knowm.xchange.poloniex.PoloniexExchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * @author George Fitzpatrick
 */
@Component
public class ExchangeCredentialsFormController implements Form<ExchangeCredentials> {

    /* ----- Fields ----- */

    private static final List<Class<? extends Exchange>> SUPPORTED_EXCHANGES = List.of(
            BinanceExchange.class, BitfinexExchange.class, BitmexExchange.class,
            BitstampExchange.class, HitbtcExchange.class, HuobiExchange.class,
            CoinbaseProExchange.class, KucoinExchange.class, KrakenExchange.class, PoloniexExchange.class
    );

    private final AggregateBooleanBinding valid;
    private final I18n i18n;

    @FXML
    private HintLabel nameLabel, exchangeLabel;

    @FXML
    private Label apiKeyLabel, secretKeyLabel, usernameLabel, passwordLabel, passphraseLabel, sandboxLabel;

    @FXML
    private ValidatedTextField name;

    @FXML
    private ValidatedComboBox<Class<? extends Exchange>> exchange;

    @FXML
    private TextField apiKey, secretKey, username, password, passphrase;

    @FXML
    private CheckBox sandbox;

    private ExchangeCredentials editing;

    /* ----- Constructors ----- */

    @Autowired
    public ExchangeCredentialsFormController(I18n i18n) {
        this.valid = new AggregateBooleanBinding();
        this.i18n = i18n;
    }

    /* ----- Methods ----- */

    @FXML
    private void initialize() {
        // localise labels
        nameLabel.textProperty().bind(i18n.string("name"));
        exchangeLabel.textProperty().bind(i18n.string("exchange"));
        apiKeyLabel.textProperty().bind(i18n.string("apiKey"));
        secretKeyLabel.textProperty().bind(i18n.string("secretKey"));
        usernameLabel.textProperty().bind(i18n.string("username"));
        passwordLabel.textProperty().bind(i18n.string("password"));
        passphraseLabel.textProperty().bind(i18n.string("passphrase"));
        sandboxLabel.textProperty().bind(i18n.string("sandbox"));

        // customise combo boxes
        exchange.setButtonCell(new ExchangeClassCell());
        exchange.setCellFactory(listView -> new ExchangeClassCell());

        // populate combo boxes
        exchange.getItems().addAll(SUPPORTED_EXCHANGES);

        // add validators to input fields
        name.setValidator(name -> name != null && !name.isBlank());
        exchange.setValidator(Objects::nonNull);

        // add validation hints to input field labels
        nameLabel.hintingProperty().bind(name.validProperty().not());
        exchangeLabel.hintingProperty().bind(exchange.validProperty().not());

        // combine individual valid properties into one property for whole form
        valid.addDependencies(name.validProperty(), exchange.validProperty());
    }

    @Override
    public ExchangeCredentials getValue() {
        // if not editing create new value otherwise update existing
        ExchangeCredentials value = editing != null ? editing : new ExchangeCredentials();
        value.setName(name.getText());
        value.setExchangeClass(exchange.getValue());
        value.setUsername(username.getText());
        value.setPassphrase(password.getText());
        value.setApiKey(apiKey.getText());
        value.setSecretKey(secretKey.getText());
        value.setPassphrase(passphrase.getText());
        value.setSandbox(sandbox.isSelected());
        return value;
    }

    @Override
    public void setValue(ExchangeCredentials value) {
        if (value == null) {
            // set default values
            name.setText(null);
            exchange.getSelectionModel().selectFirst();
            username.setText(null);
            password.setText(null);
            apiKey.setText(null);
            secretKey.setText(null);
            passphrase.setText(null);
            sandbox.setSelected(false);
        } else {
            // update field values
            name.setText(value.getName());
            exchange.setValue(value.getExchangeClass());
            username.setText(value.getUsername());
            password.setText(value.getPassword());
            apiKey.setText(value.getApiKey());
            secretKey.setText(value.getSecretKey());
            passphrase.setText(value.getPassphrase());
            sandbox.setSelected(value.isSandbox());
        }
        editing = value;
    }

    @Override
    public BooleanBinding validBinding() {
        return valid;
    }

    /* ----- Classes ----- */

    private static final class ExchangeClassCell extends ListCell<Class<? extends Exchange>> {

        @Override
        protected void updateItem(Class<? extends Exchange> exchangeClass, boolean empty) {
            super.updateItem(exchangeClass, empty);
            if (empty) {
                setText(null);
            } else {
                // if name of org.knowm.xchange.Exchange implementation name contains substring "Exchange" remove it
                setText(exchangeClass.getSimpleName().replace("Exchange", ""));
            }
        }

    }

}
