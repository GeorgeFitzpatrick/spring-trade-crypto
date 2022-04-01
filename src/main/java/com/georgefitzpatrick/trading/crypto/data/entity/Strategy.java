package com.georgefitzpatrick.trading.crypto.data.entity;

import com.georgefitzpatrick.trading.crypto.data.IdentifiableEntity;
import com.georgefitzpatrick.trading.crypto.data.converter.CurrencyConverter;
import javafx.beans.property.*;
import org.hibernate.annotations.LazyCollection;
import org.knowm.xchange.currency.Currency;

import javax.persistence.*;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static java.util.Collections.emptyList;
import static javafx.collections.FXCollections.observableArrayList;
import static org.hibernate.annotations.LazyCollectionOption.FALSE;

/**
 * @author George Fitzpatrick
 */
@Entity
@Access(AccessType.PROPERTY)
@Table(name = "strategy")
public class Strategy extends IdentifiableEntity {

    /* ----- Fields ----- */

    private StringProperty name;
    private ObjectProperty<ExchangeCredentials> exchangeCredentials;
    private ObjectProperty<Currency> baseCurrency;
    private ObjectProperty<Currency> counterCurrency;
    private IntegerProperty timeframe;
    private ObjectProperty<ChronoUnit> timeframeUnit;
    private DoubleProperty risk;
    private ListProperty<Condition> conditions;

    /* ----- Methods ----- */

    @PreRemove
    private void removeFromExchangeCredentials() {
        ExchangeCredentials exchangeCredentials = getExchangeCredentials();
        if (exchangeCredentials != null) {
            exchangeCredentials.removeStrategy(this);
        }
    }

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

    @ManyToOne
    public ExchangeCredentials getExchangeCredentials() {
        return exchangeCredentials == null ? null : exchangeCredentials.get();
    }

    public void setExchangeCredentials(ExchangeCredentials exchangeCredentials) {
        exchangeProperty().set(exchangeCredentials);
    }

    public ObjectProperty<ExchangeCredentials> exchangeProperty() {
        if (exchangeCredentials == null) {
            exchangeCredentials = new SimpleObjectProperty<>(this, "exchange");
        }
        return exchangeCredentials;
    }

    @Convert(converter = CurrencyConverter.class)
    @Column(name = "base_currency", nullable = false)
    public Currency getBaseCurrency() {
        return baseCurrency == null ? null : baseCurrency.get();
    }

    public void setBaseCurrency(Currency baseCurrency) {
        baseCurrencyProperty().set(baseCurrency);
    }

    public ObjectProperty<Currency> baseCurrencyProperty() {
        if (baseCurrency == null) {
            baseCurrency = new SimpleObjectProperty<>(this, "baseCurrency");
        }
        return baseCurrency;
    }

    @Convert(converter = CurrencyConverter.class)
    @Column(name = "counter_currency", nullable = false)
    public Currency getCounterCurrency() {
        return counterCurrency == null ? null : counterCurrency.get();
    }

    public void setCounterCurrency(Currency counterCurrency) {
        counterCurrencyProperty().set(counterCurrency);
    }

    public ObjectProperty<Currency> counterCurrencyProperty() {
        if (counterCurrency == null) {
            counterCurrency = new SimpleObjectProperty<>(this, "counterCurrency");
        }
        return counterCurrency;
    }

    @Column(name = "timeframe", nullable = false)
    public Integer getTimeframe() {
        return timeframe == null ? null : timeframe.get();
    }

    public void setTimeframe(Integer timeframe) {
        timeframeProperty().set(timeframe);
    }

    public IntegerProperty timeframeProperty() {
        if (timeframe == null) {
            timeframe = new SimpleIntegerProperty(this, "timeframe");
        }
        return timeframe;
    }

    @Column(name = "timeframe_unit", nullable = false)
    public ChronoUnit getTimeframeUnit() {
        return timeframeUnit == null ? null : timeframeUnit.get();
    }

    public void setTimeframeUnit(ChronoUnit timeframeUnit) {
        timeframeUnitProperty().set(timeframeUnit);
    }

    public ObjectProperty<ChronoUnit> timeframeUnitProperty() {
        if (timeframeUnit == null) {
            timeframeUnit = new SimpleObjectProperty<>(this, "timeframeUnit");
        }
        return timeframeUnit;
    }

    @Column(name = "risk", nullable = false)
    public Double getRisk() {
        return risk == null ? null : risk.get();
    }

    public void setRisk(Double risk) {
        riskProperty().set(risk);
    }

    public DoubleProperty riskProperty() {
        if (risk == null) {
            risk = new SimpleDoubleProperty(this, "risk");
        }
        return risk;
    }

    @OneToMany
    @JoinColumn(name = "strategy_id")
    @LazyCollection(FALSE)
    public List<Condition> getConditions() {
        return conditions == null ? emptyList() : conditions.get();
    }

    public void setConditions(List<Condition> conditions) {
        conditionsProperty().set(conditions != null ? observableArrayList(conditions) : observableArrayList());
    }

    public void addCondition(Condition condition) {
        conditionsProperty().add(condition);
        condition.setStrategy(this);
    }

    public void removeCondition(Condition condition) {
        conditionsProperty().remove(condition);
        condition.setStrategy(null);
    }

    public ListProperty<Condition> conditionsProperty() {
        if (conditions == null) {
            conditions = new SimpleListProperty<>(this, "conditions", observableArrayList());
        }
        return conditions;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Strategy && super.equals(obj);
    }

}
