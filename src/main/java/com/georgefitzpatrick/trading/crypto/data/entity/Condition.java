package com.georgefitzpatrick.trading.crypto.data.entity;

import com.georgefitzpatrick.trading.crypto.data.IdentifiableEntity;
import com.georgefitzpatrick.trading.crypto.strategy.Indicator;
import com.georgefitzpatrick.trading.crypto.strategy.IndicatorRelationship;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

import javax.persistence.*;

/**
 * @author George Fitzpatrick
 */
@Entity
@Access(AccessType.PROPERTY)
@Table(name = "condition")
public class Condition extends IdentifiableEntity {

    /* ----- Fields ----- */

    private ObjectProperty<Action> action;
    private IntegerProperty basePeriod;
    private ObjectProperty<Indicator> baseIndicator;
    private ObjectProperty<IndicatorRelationship> relationship;
    private IntegerProperty counterPeriod;
    private ObjectProperty<Indicator> counterIndicator;
    private ObjectProperty<Strategy> strategy;

    /* ----- Methods ----- */

    @PreRemove
    private void removeFromStrategy() {
        Strategy strategy = getStrategy();
        if (strategy != null) {
            strategy.removeCondition(this);
        }
    }

    @ManyToOne
    public Strategy getStrategy() {
        return strategy == null ? null : strategy.get();
    }

    public void setStrategy(Strategy strategy) {
        exchangeProperty().set(strategy);
    }

    public ObjectProperty<Strategy> exchangeProperty() {
        if (strategy == null) {
            strategy = new SimpleObjectProperty<>(this, "strategy");
        }
        return strategy;
    }

    @Column(name = "action", nullable = false)
    public Action getAction() {
        return action == null ? null : action.get();
    }

    public void setAction(Action action) {
        actionProperty().set(action);
    }

    public ObjectProperty<Action> actionProperty() {
        if (action == null) {
            action = new SimpleObjectProperty<>(this, "type");
        }
        return action;
    }

    @Column(name = "base_period", nullable = false)
    public Integer getBasePeriod() {
        return basePeriod == null ? null : basePeriod.get();
    }

    public void setBasePeriod(Integer basePeriod) {
        basePeriodProperty().set(basePeriod);
    }

    public IntegerProperty basePeriodProperty() {
        if (basePeriod == null) {
            basePeriod = new SimpleIntegerProperty(this, "basePeriod");
        }
        return basePeriod;
    }

    @Column(name = "base_indicator", nullable = false)
    public Indicator getBaseIndicator() {
        return baseIndicator == null ? null : baseIndicator.get();
    }

    public void setBaseIndicator(Indicator base) {
        baseIndicatorProperty().set(base);
    }

    public ObjectProperty<Indicator> baseIndicatorProperty() {
        if (baseIndicator == null) {
            baseIndicator = new SimpleObjectProperty<>(this, "base");
        }
        return baseIndicator;
    }

    @Column(name = "relationship", nullable = false)
    public IndicatorRelationship getRelationship() {
        return relationship == null ? null : relationship.get();
    }

    public void setRelationship(IndicatorRelationship relationship) {
        relationshipProperty().set(relationship);
    }

    public ObjectProperty<IndicatorRelationship> relationshipProperty() {
        if (relationship == null) {
            relationship = new SimpleObjectProperty<>(this, "comparator");
        }
        return relationship;
    }

    @Column(name = "counter_period", nullable = false)
    public Integer getCounterPeriod() {
        return counterPeriod == null ? null : counterPeriod.get();
    }

    public void setCounterPeriod(Integer counterPeriod) {
        counterPeriodProperty().set(counterPeriod);
    }

    public IntegerProperty counterPeriodProperty() {
        if (counterPeriod == null) {
            counterPeriod = new SimpleIntegerProperty(this, "counterPeriod");
        }
        return counterPeriod;
    }

    @Column(name = "counter_indicator", nullable = false)
    public Indicator getCounterIndicator() {
        return counterIndicator == null ? null : counterIndicator.get();
    }

    public void setCounterIndicator(Indicator counter) {
        counterIndicatorProperty().set(counter);
    }

    public ObjectProperty<Indicator> counterIndicatorProperty() {
        if (counterIndicator == null) {
            counterIndicator = new SimpleObjectProperty<>(this, "counter");
        }
        return counterIndicator;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Condition && super.equals(obj);
    }

    /* ----- Classes ----- */

    public enum Action {

        BUY("Buy"),
        SELL("Sell");

        private final String name;

        Action(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }

    }

}
