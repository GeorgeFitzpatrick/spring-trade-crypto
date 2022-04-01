package com.georgefitzpatrick.trading.crypto.ui.controller.form;

import com.georgefitzpatrick.trading.crypto.data.entity.Condition;
import com.georgefitzpatrick.trading.crypto.strategy.Indicator;
import com.georgefitzpatrick.trading.crypto.strategy.IndicatorRelationship;
import com.georgefitzpatrick.trading.crypto.ui.Form;
import com.georgefitzpatrick.trading.crypto.ui.I18n;
import com.georgefitzpatrick.trading.crypto.ui.binding.AggregateBooleanBinding;
import com.georgefitzpatrick.trading.crypto.ui.control.HintLabel;
import com.georgefitzpatrick.trading.crypto.ui.control.ValidatedComboBox;
import com.georgefitzpatrick.trading.crypto.ui.control.ValidatedSpinner;
import com.georgefitzpatrick.trading.crypto.ui.filter.IntegerKeyFilter;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import static javafx.scene.input.KeyEvent.KEY_TYPED;

/**
 * @author George Fitzpatrick
 */
@Component
public class ConditionFormController implements Form<Condition> {

    /* ----- Fields ----- */

    private final AggregateBooleanBinding valid;
    private final I18n i18n;

    @FXML
    private HintLabel actionLabel, basePeriodLabel, baseIndicatorLabel,
            relationshipLabel, counterPeriodLabel, counterIndicatorLabel;

    @FXML
    private ValidatedComboBox<Condition.Action> action;

    @FXML
    private ValidatedComboBox<Indicator> baseIndicator, counterIndicator;

    @FXML
    private ValidatedSpinner<Integer> basePeriod, counterPeriod;

    @FXML
    private ValidatedComboBox<IndicatorRelationship> relationship;

    private Condition editing;

    /* ----- Constructors ----- */

    @Autowired
    public ConditionFormController(I18n i18n) {
        this.valid = new AggregateBooleanBinding();
        this.i18n = i18n;
    }

    /* ----- Methods ----- */

    @FXML
    private void initialize() {
        // localise labels
        actionLabel.textProperty().bind(i18n.string("action"));
        basePeriodLabel.textProperty().bind(i18n.string("basePeriod"));
        baseIndicatorLabel.textProperty().bind(i18n.string("baseIndicator"));
        relationshipLabel.textProperty().bind(i18n.string("relationship"));
        counterPeriodLabel.textProperty().bind(i18n.string("counterPeriod"));
        counterIndicatorLabel.textProperty().bind(i18n.string("counterIndicator"));

        // configure spinner limits and initial values
        basePeriod.setValueFactory(new IntegerSpinnerValueFactory(2, Integer.MAX_VALUE));
        counterPeriod.setValueFactory(new IntegerSpinnerValueFactory(2, Integer.MAX_VALUE));

        // configure spinner input filters to only allow integers
        basePeriod.addEventFilter(KEY_TYPED, new IntegerKeyFilter());
        counterPeriod.addEventFilter(KEY_TYPED, new IntegerKeyFilter());

        // populate combo boxes
        action.getItems().addAll(Condition.Action.values());
        baseIndicator.getItems().addAll(Indicator.values());
        relationship.getItems().addAll(IndicatorRelationship.values());
        counterIndicator.getItems().addAll(Indicator.values());

        // add validators to input fields
        action.setValidator(Objects::nonNull);
        baseIndicator.setValidator(Objects::nonNull);
        basePeriod.setValidator(Objects::nonNull);
        relationship.setValidator(Objects::nonNull);
        counterIndicator.setValidator(Objects::nonNull);
        counterPeriod.setValidator(Objects::nonNull);

        // add validation hints to input field labels
        actionLabel.hintingProperty().bind(action.validProperty().not());
        baseIndicatorLabel.hintingProperty().bind(baseIndicator.validProperty().not());
        basePeriodLabel.hintingProperty().bind(basePeriod.validProperty().not());
        relationshipLabel.hintingProperty().bind(relationship.validProperty().not());
        counterIndicatorLabel.hintingProperty().bind(counterIndicator.validProperty().not());
        counterPeriodLabel.hintingProperty().bind(counterPeriod.validProperty().not());

        // combine individual valid properties into one property for whole form
        valid.addDependencies(action.validProperty(), baseIndicator.validProperty(), basePeriod.validProperty(),
                relationship.validProperty(), counterIndicator.validProperty(), counterPeriod.validProperty());
    }

    @Override
    public Condition getValue() {
        // if not editing create new value otherwise update existing
        Condition value = editing != null ? editing : new Condition();
        value.setAction(action.getValue());
        value.setBasePeriod(basePeriod.getValue());
        value.setBaseIndicator(baseIndicator.getValue());
        value.setRelationship(relationship.getValue());
        value.setCounterPeriod(counterPeriod.getValue());
        value.setCounterIndicator(counterIndicator.getValue());
        return value;
    }

    @Override
    public void setValue(Condition value) {
        if (value == null) {
            // set default values
            action.getSelectionModel().selectFirst();
            basePeriod.getValueFactory().setValue(2);
            baseIndicator.getSelectionModel().selectFirst();
            relationship.getSelectionModel().selectFirst();
            counterPeriod.getValueFactory().setValue(2);
            counterIndicator.getSelectionModel().selectFirst();
        } else {
            // update field values
            action.setValue(value.getAction());
            basePeriod.getValueFactory().setValue(value.getBasePeriod());
            baseIndicator.setValue(value.getBaseIndicator());
            relationship.setValue(value.getRelationship());
            counterPeriod.getValueFactory().setValue(value.getCounterPeriod());
            counterIndicator.setValue(value.getCounterIndicator());
        }
        editing = value;
    }

    @Override
    public BooleanBinding validBinding() {
        return valid;
    }

}
