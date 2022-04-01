package com.georgefitzpatrick.trading.crypto.ui;

import com.georgefitzpatrick.trading.crypto.CryptoTradingUiProperties;
import javafx.beans.binding.Binding;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.TemporalAccessor;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import static java.util.ResourceBundle.getBundle;

/**
 * @author George Fitzpatrick
 */
@Component
public class I18n {

    /* ----- Fields ----- */

    public static final List<Locale> SUPPORTED_LOCALES = List.of(
            Locale.forLanguageTag("en"), Locale.forLanguageTag("es"), Locale.forLanguageTag("fr"));

    private final String resourceBundleBaseName;

    private ObjectProperty<Locale> locale;

    /* ----- Constructors ----- */

    @Autowired
    public I18n(CryptoTradingUiProperties properties) {
        this.resourceBundleBaseName = properties.getResourceBundleBaseName();
    }

    /* ----- Methods ----- */

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

    public Binding<String> string(String key) {
        return new StringBinding() {

            {
                this.bind(localeProperty());
            }

            @Override
            public void dispose() {
                this.unbind(localeProperty());
            }

            @Override
            public ObservableList<?> getDependencies() {
                return FXCollections.singletonObservableList(localeProperty());
            }

            @Override
            protected String computeValue() {
                Locale locale = getLocale();
                if (locale == null || !SUPPORTED_LOCALES.contains(locale)) {
                    return null;
                }
                ResourceBundle bundle = getBundle(resourceBundleBaseName, locale);
                return bundle.getString(key);
            }

        };
    }

    public Binding<String> time(TemporalAccessor time, DateTimeFormatter formatter) {
        return time(new SimpleObjectProperty<>(time), formatter);
    }

    public Binding<String> time(ObservableValue<? extends TemporalAccessor> time, DateTimeFormatter formatter) {
        return new StringBinding() {

            {
                this.bind(time, localeProperty());
            }

            @Override
            public void dispose() {
                unbind(time);
            }

            @Override
            public ObservableList<?> getDependencies() {
                return FXCollections.singletonObservableList(time);
            }

            @Override
            protected String computeValue() {
                Locale locale = getLocale();
                if (locale == null || !SUPPORTED_LOCALES.contains(locale)) {
                    return null;
                }
                DateTimeFormatter localisedFormatter = formatter.withLocale(locale);
                return time.getValue() == null ? null : localisedFormatter.format(time.getValue());
            }

        };
    }

}
