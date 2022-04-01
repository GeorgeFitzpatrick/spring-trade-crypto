package com.georgefitzpatrick.trading.crypto.ui.control;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.css.PseudoClass;
import javafx.scene.control.Label;

/**
 * @author George Fitzpatrick
 */
public class HintLabel extends Label {

    /* ----- Fields ----- */

    private static final String DEFAULT_STYLE_CLASS = "hint-label";
    private static final PseudoClass HINTING_PSEUDO_CLASS = PseudoClass.getPseudoClass("hinting");

    private final BooleanProperty hinting;

    /* ----- Constructors ----- */

    public HintLabel() {
        this.hinting = new SimpleBooleanProperty(this, "hinting") {
            @Override
            protected void invalidated() {
                pseudoClassStateChanged(HINTING_PSEUDO_CLASS, get());
            }
        };
        getStyleClass().add(DEFAULT_STYLE_CLASS);
    }

    /* ----- Methods ----- */

    public boolean isHinting() {
        return hinting.get();
    }

    public void setHinting(boolean hinting) {
        this.hinting.set(hinting);
    }

    public BooleanProperty hintingProperty() {
        return hinting;
    }

}
