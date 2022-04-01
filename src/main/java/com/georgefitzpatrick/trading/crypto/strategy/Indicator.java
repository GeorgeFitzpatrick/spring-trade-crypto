package com.georgefitzpatrick.trading.crypto.strategy;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;

/**
 * @author George Fitzpatrick
 */
public enum Indicator {

    /* ----- Enumerations ----- */

    PRICE("price") {
        @Override
        public BigDecimal calculate(List<BigDecimal> prices, MathContext precision) {
            return prices.get(prices.size() - 1);
        }
    },

    SIMPLE_MOVING_AVERAGE("simple moving average") {
        @Override
        public BigDecimal calculate(List<BigDecimal> prices, MathContext precision) {
            BigDecimal size = BigDecimal.valueOf(prices.size());
            BigDecimal sum = ZERO;
            for (BigDecimal x : prices) {
                sum = sum.add(x);
            }
            return sum.divide(size, precision);
        }
    },

    STANDARD_DEVIATION("standard deviation") {
        @Override
        public BigDecimal calculate(List<BigDecimal> prices, MathContext precision) {
            BigDecimal size = BigDecimal.valueOf(prices.size());
            BigDecimal sum = ZERO, sumOfSquares = ZERO;
            for (BigDecimal x : prices) {
                sum = sum.add(x);
                BigDecimal squareOfX = x.pow(2, precision);
                sumOfSquares = sumOfSquares.add(squareOfX);
            }
            BigDecimal meanOfSquares = sumOfSquares.divide(size, precision);
            BigDecimal mean = sum.divide(size, precision);
            BigDecimal squareOfMean = mean.pow(2, precision);
            BigDecimal variance = meanOfSquares.subtract(squareOfMean);
            return variance.sqrt(precision);
        }
    },

    LOWER_BOLLINGER_BAND("lower bollinger band") {
        @Override
        public BigDecimal calculate(List<BigDecimal> prices, MathContext precision) {
            BigDecimal middle = SIMPLE_MOVING_AVERAGE.calculate(prices, precision);
            BigDecimal deviation = STANDARD_DEVIATION.calculate(prices, precision);
            return middle.subtract(deviation.multiply(TWO));
        }
    },

    UPPER_BOLLINGER_BAND("upper bollinger band") {
        @Override
        public BigDecimal calculate(List<BigDecimal> prices, MathContext precision) {
            BigDecimal middle = SIMPLE_MOVING_AVERAGE.calculate(prices, precision);
            BigDecimal deviation = STANDARD_DEVIATION.calculate(prices, precision);
            return middle.add(deviation.multiply(TWO));
        }
    };

    /* ----- Fields ----- */

    private static final BigDecimal TWO = new BigDecimal("2");

    private final String name;

    /* ----- Constructors ----- */

    Indicator(String name) {
        this.name = name;
    }

    /* ----- Methods ----- */

    public abstract BigDecimal calculate(List<BigDecimal> prices, MathContext precision);

    @Override
    public String toString() {
        return name;
    }

}
