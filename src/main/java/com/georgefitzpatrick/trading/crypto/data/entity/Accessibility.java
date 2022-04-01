package com.georgefitzpatrick.trading.crypto.data.entity;

import com.georgefitzpatrick.trading.crypto.data.IdentifiableEntity;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import javax.persistence.*;
import java.util.Locale;

/**
 * @author George Fitzpatrick
 */
@Entity
@Access(AccessType.PROPERTY)
@Table(name = "accessibility")
public class Accessibility extends IdentifiableEntity {

    /* ----- Fields ----- */

    private ObjectProperty<Locale> locale;

    /* ----- Methods ----- */

    @Column(name = "locale", nullable = false)
    public Locale getLocale() {
        return locale == null ? null : locale.get();
    }

    public void setLocale(Locale locale) {
        localeProperty().set(locale);
    }

    public ObjectProperty<Locale> localeProperty() {
        if (locale == null) {
            locale = new SimpleObjectProperty<>(this, "locale");
        }
        return locale;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Accessibility && super.equals(obj);
    }

}
