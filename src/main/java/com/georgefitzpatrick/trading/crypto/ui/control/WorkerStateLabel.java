package com.georgefitzpatrick.trading.crypto.ui.control;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Worker;
import javafx.css.PseudoClass;
import javafx.scene.control.Label;

import java.util.Map;

import static javafx.concurrent.Worker.State.*;

/**
 * @author George Fitzpatrick
 */
public class WorkerStateLabel extends Label {

    /* ----- Fields ----- */

    private static final String DEFAULT_STYLE_CLASS = "worker-state-label";
    private static final Map<Worker.State, PseudoClass> STATE_PSEUDO_CLASSES = Map.of(
            READY, PseudoClass.getPseudoClass("ready"),
            SCHEDULED, PseudoClass.getPseudoClass("scheduled"),
            RUNNING, PseudoClass.getPseudoClass("running"),
            SUCCEEDED, PseudoClass.getPseudoClass("succeeded"),
            CANCELLED, PseudoClass.getPseudoClass("cancelled"),
            FAILED, PseudoClass.getPseudoClass("failed")
    );

    private final ObjectProperty<Worker.State> state;

    /* ----- Constructors ----- */

    public WorkerStateLabel() {
        this.state = new SimpleObjectProperty<>(this, "state") {
            @Override
            protected void invalidated() {
                for (Worker.State state : STATE_PSEUDO_CLASSES.keySet()) {
                    pseudoClassStateChanged(STATE_PSEUDO_CLASSES.get(state), state.equals(get()));
                }
            }
        };
        getStyleClass().add(DEFAULT_STYLE_CLASS);
    }

    /* ----- Methods ----- */

    public Worker.State getState() {
        return state.get();
    }

    public void setState(Worker.State state) {
        this.state.set(state);
    }

    public ObjectProperty<Worker.State> stateProperty() {
        return state;
    }

}
