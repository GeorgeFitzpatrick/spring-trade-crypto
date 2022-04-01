package com.georgefitzpatrick.trading.crypto.data.converter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.knowm.xchange.currency.Currency;

/**
 * @author George Fitzpatrick
 */
public class CurrencyConverterUnitTest {

    /* ----- Fields ----- */

    private static final CurrencyConverter CONVERTER = new CurrencyConverter();

    /* ----- Methods ----- */

    @Test
    public void mapCurrency_null_null() {
        String intermediate = CONVERTER.convertToDatabaseColumn(null);
        Currency actual = CONVERTER.convertToEntityAttribute(intermediate);
        Assertions.assertNull(actual);
    }

    @Test
    public void mapCurrency_notNull_equalsOriginal() {
        Currency expected = Currency.BTC;
        String intermediate = CONVERTER.convertToDatabaseColumn(expected);
        Currency actual = CONVERTER.convertToEntityAttribute(intermediate);
        Assertions.assertEquals(expected, actual);
    }

}
