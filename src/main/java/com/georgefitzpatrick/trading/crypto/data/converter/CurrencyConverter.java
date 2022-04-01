package com.georgefitzpatrick.trading.crypto.data.converter;

import org.knowm.xchange.currency.Currency;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * @author George Fitzpatrick
 */
@Converter
public class CurrencyConverter implements AttributeConverter<Currency, String> {

    @Override
    public String convertToDatabaseColumn(Currency currency) {
        return currency == null ? null : currency.getCurrencyCode();
    }

    @Override
    public Currency convertToEntityAttribute(String s) {
        return s == null ? null : Currency.getInstance(s);
    }

}
