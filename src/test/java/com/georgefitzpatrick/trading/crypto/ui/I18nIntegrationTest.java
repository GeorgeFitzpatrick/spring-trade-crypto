package com.georgefitzpatrick.trading.crypto.ui;

import com.georgefitzpatrick.trading.crypto.CryptoTradingUiProperties;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

/**
 * @author George Fitzpatrick
 */
@SuppressWarnings("ClassCanBeRecord")
@SpringBootTest
public class I18nIntegrationTest {

    /* ----- Fields ----- */

    private static final Locale ENGLISH = Locale.forLanguageTag("en");
    private static final Locale SPANISH = Locale.forLanguageTag("es");
    private static final Locale FRENCH = Locale.forLanguageTag("fr");
    private static final Instant TIMESTAMP = Instant.ofEpochMilli(1648371600000L);
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
            .withZone(ZoneId.systemDefault());

    private final CryptoTradingUiProperties properties;

    /* ----- Constructors ----- */

    @Autowired
    public I18nIntegrationTest(CryptoTradingUiProperties properties) {
        this.properties = properties;
    }

    /* ----- Methods ----- */

    @Test
    void localise_stringInvalidLocale_nullString() {
        I18n i18n = new I18n(properties);
        i18n.setLocale(null);
        Assertions.assertNull(i18n.string("name").getValue());
    }

    @Test
    void localise_stringEnglishLocale_englishString() {
        I18n i18n = new I18n(properties);
        i18n.setLocale(ENGLISH);
        Assertions.assertEquals("Name", i18n.string("name").getValue());
    }

    @Test
    void localise_stringSpanishLocale_spanishString() {
        I18n i18n = new I18n(properties);
        i18n.setLocale(SPANISH);
        Assertions.assertEquals("Nombre", i18n.string("name").getValue());
    }

    @Test
    void localise_stringFrenchLocale_frenchString() {
        I18n i18n = new I18n(properties);
        i18n.setLocale(FRENCH);
        Assertions.assertEquals("Nom", i18n.string("name").getValue());
    }

    @Test
    void localise_timeInvalidLocale_nullTime() {
        I18n i18n = new I18n(properties);
        i18n.setLocale(null);
        Assertions.assertNull(i18n.time(TIMESTAMP, DATE_TIME_FORMATTER).getValue());
    }

    @Test
    void localise_timeEnglishLocale_englishTime() {
        I18n i18n = new I18n(properties);
        i18n.setLocale(ENGLISH);
        Assertions.assertEquals("Mar 27, 2022, 10:00:00 AM", i18n.time(TIMESTAMP, DATE_TIME_FORMATTER).getValue());
    }

    @Test
    void localise_timeSpanishLocale_spanishTime() {
        I18n i18n = new I18n(properties);
        i18n.setLocale(SPANISH);
        Assertions.assertEquals("27 mar 2022 10:00:00", i18n.time(TIMESTAMP, DATE_TIME_FORMATTER).getValue());
    }

    @Test
    void localise_timeFrenchLocale_frenchTime() {
        I18n i18n = new I18n(properties);
        i18n.setLocale(FRENCH);
        Assertions.assertEquals("27 mars 2022, 10:00:00", i18n.time(TIMESTAMP, DATE_TIME_FORMATTER).getValue());
    }

}
