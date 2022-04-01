package com.georgefitzpatrick.trading.crypto.strategy;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;

/**
 * @author George Fitzpatrick
 */
public class IndicatorUnitTest {

    /* ----- Fields ----- */

    private static final BigDecimal TWO = new BigDecimal("2");
    private static final BigDecimal FOUR = new BigDecimal("4");
    private static final BigDecimal SIX = new BigDecimal("6");

    private static final MathContext PRECISION = MathContext.DECIMAL64;

    /* ----- Constructors ----- */

    public IndicatorUnitTest() {

    }

    /* ----- Methods ----- */

    @Test
    public void calculateIndicator_price() {
        List<BigDecimal> prices = List.of(TWO, FOUR, SIX);
        BigDecimal actual = Indicator.PRICE.calculate(prices, PRECISION);
        Assertions.assertEquals(SIX, actual);
    }

    // x̄ = Σx / n
    @Test
    public void calculateIndicator_simpleMovingAverage() {
        List<BigDecimal> prices = List.of(TWO, FOUR, SIX);
        BigDecimal actual = Indicator.SIMPLE_MOVING_AVERAGE.calculate(prices, PRECISION);
        Assertions.assertEquals(FOUR, actual);
    }

    // σ = √((Σx² / n) - (Σx / n)²)
    @Test
    public void calculateIndicator_standardDeviation() {
        List<BigDecimal> prices = List.of(TWO, FOUR, SIX);
        BigDecimal actual = Indicator.STANDARD_DEVIATION.calculate(prices, PRECISION);
        Assertions.assertEquals(new BigDecimal("1.632993161855453"), actual);
    }

    // lower bollinger band = x̄ - 2σ
    @Test
    public void calculateIndicator_lowerBollingerBand() {
        List<BigDecimal> prices = List.of(TWO, FOUR, SIX);
        BigDecimal actual = Indicator.LOWER_BOLLINGER_BAND.calculate(prices, PRECISION);
        Assertions.assertEquals(new BigDecimal("0.734013676289094"), actual);
    }

    // upper bollinger band = x̄ + 2σ
    @Test
    public void calculateIndicator_upperBollingerBand() {
        List<BigDecimal> prices = List.of(TWO, FOUR, SIX);
        BigDecimal actual = Indicator.UPPER_BOLLINGER_BAND.calculate(prices, PRECISION);
        Assertions.assertEquals(new BigDecimal("7.265986323710906"), actual);
    }

}
