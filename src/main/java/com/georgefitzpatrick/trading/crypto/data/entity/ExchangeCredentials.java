package com.georgefitzpatrick.trading.crypto.data.entity;

import com.georgefitzpatrick.trading.crypto.data.IdentifiableEntity;
import javafx.beans.property.*;
import org.hibernate.annotations.LazyCollection;
import org.knowm.xchange.Exchange;

import javax.persistence.*;
import java.util.List;

import static java.util.Collections.emptyList;
import static javafx.collections.FXCollections.observableArrayList;
import static org.hibernate.annotations.LazyCollectionOption.FALSE;

/**
 * @author George Fitzpatrick
 */
@Entity
@Access(AccessType.PROPERTY)
@Table(name = "exchange_credentials")
public class ExchangeCredentials extends IdentifiableEntity {

    /* ----- Fields ----- */

    private StringProperty name;
    private ObjectProperty<Class<? extends Exchange>> exchangeClass;
    private StringProperty username;
    private StringProperty password;
    private StringProperty apiKey;
    private StringProperty secretKey;
    private StringProperty passphrase;
    private BooleanProperty sandbox;
    private ListProperty<Strategy> strategies;

    /* ----- Methods ----- */

    @Column(name = "name", nullable = false)
    public String getName() {
        return name == null ? null : name.get();
    }

    public void setName(String name) {
        nameProperty().set(name);
    }

    public StringProperty nameProperty() {
        if (name == null) {
            name = new SimpleStringProperty(this, "name");
        }
        return name;
    }

    @Column(name = "exchange_class", nullable = false)
    public Class<? extends Exchange> getExchangeClass() {
        return exchangeClass == null ? null : exchangeClass.get();
    }

    public void setExchangeClass(Class<? extends Exchange> exchangeClass) {
        exchangeClassProperty().set(exchangeClass);
    }

    public ObjectProperty<Class<? extends Exchange>> exchangeClassProperty() {
        if (exchangeClass == null) {
            exchangeClass = new SimpleObjectProperty<>(this, "exchangeClass");
        }
        return exchangeClass;
    }

    @Column(name = "username")
    public String getUsername() {
        return username == null ? null : username.get();
    }

    public void setUsername(String userName) {
        usernameProperty().set(userName);
    }

    public StringProperty usernameProperty() {
        if (username == null) {
            username = new SimpleStringProperty(this, "userName");
        }
        return username;
    }

    @Column(name = "password")
    public String getPassword() {
        return password == null ? null : password.get();
    }

    public void setPassword(String password) {
        passwordProperty().set(password);
    }

    public StringProperty passwordProperty() {
        if (password == null) {
            password = new SimpleStringProperty(this, "password");
        }
        return password;
    }

    @Column(name = "api_key")
    public String getApiKey() {
        return apiKey == null ? null : apiKey.get();
    }

    public void setApiKey(String apiKey) {
        apiKeyProperty().set(apiKey);
    }

    public StringProperty apiKeyProperty() {
        if (apiKey == null) {
            apiKey = new SimpleStringProperty(this, "apiKey");
        }
        return apiKey;
    }

    @Column(name = "secret_key")
    public String getSecretKey() {
        return secretKey == null ? null : secretKey.get();
    }

    public void setSecretKey(String secretKey) {
        secretKeyProperty().set(secretKey);
    }

    public StringProperty secretKeyProperty() {
        if (secretKey == null) {
            secretKey = new SimpleStringProperty(this, "secretKey");
        }
        return secretKey;
    }

    @Column(name = "passphrase")
    public String getPassphrase() {
        return passphrase == null ? null : passphrase.get();
    }

    public void setPassphrase(String passphrase) {
        passphraseProperty().set(passphrase);
    }

    public StringProperty passphraseProperty() {
        if (passphrase == null) {
            passphrase = new SimpleStringProperty(this, "passphrase");
        }
        return passphrase;
    }

    @Column(name = "sandbox", nullable = false)
    public boolean isSandbox() {
        return sandbox != null && sandbox.get();
    }

    public void setSandbox(boolean sandbox) {
        sandboxProperty().set(sandbox);
    }

    public BooleanProperty sandboxProperty() {
        if (sandbox == null) {
            sandbox = new SimpleBooleanProperty(this, "sandbox", false);
        }
        return sandbox;
    }

    @OneToMany
    @JoinColumn(name = "exchange_credentials_id")
    @LazyCollection(FALSE)
    public List<Strategy> getStrategies() {
        return strategies == null ? emptyList() : strategies.get();
    }

    public void setStrategies(List<Strategy> strategies) {
        strategiesProperty().set(strategies != null ? observableArrayList(strategies) : observableArrayList());
    }

    public void addStrategy(Strategy strategy) {
        strategiesProperty().add(strategy);
        strategy.setExchangeCredentials(this);
    }

    public void removeStrategy(Strategy strategy) {
        strategiesProperty().remove(strategy);
        strategy.setExchangeCredentials(null);
    }

    public ListProperty<Strategy> strategiesProperty() {
        if (strategies == null) {
            strategies = new SimpleListProperty<>(this, "strategies", observableArrayList());
        }
        return strategies;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ExchangeCredentials && super.equals(obj);
    }

}
