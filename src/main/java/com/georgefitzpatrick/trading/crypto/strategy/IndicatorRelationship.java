package com.georgefitzpatrick.trading.crypto.strategy;

import java.math.BigDecimal;

/**
 * @author George Fitzpatrick
 */
public enum IndicatorRelationship {

    /* ----- Enumerations ----- */

    EQUAL_TO("equal to") {
        @Override
        public boolean test(BigDecimal previousBase, BigDecimal currentBase,
                            BigDecimal previousCounter, BigDecimal currentCounter) {
            return currentBase.compareTo(currentCounter) == 0;
        }
    },

    GREATER_THAN("greater than") {
        @Override
        public boolean test(BigDecimal previousBase, BigDecimal currentBase,
                            BigDecimal previousCounter, BigDecimal currentCounter) {
            return currentBase.compareTo(currentCounter) > 0;
        }
    },

    LESS_THAN("less than") {
        @Override
        public boolean test(BigDecimal previousBase, BigDecimal currentBase,
                            BigDecimal previousCounter, BigDecimal currentCounter) {
            return currentBase.compareTo(currentCounter) < 0;
        }
    },

    CROSSES_OVER("crosses over") {
        @Override
        public boolean test(BigDecimal previousBase, BigDecimal currentBase,
                            BigDecimal previousCounter, BigDecimal currentCounter) {
            return previousBase.compareTo(previousCounter) <= 0 && currentBase.compareTo(currentCounter) > 0;
        }
    },

    CROSSES_UNDER("crosses under") {
        @Override
        public boolean test(BigDecimal previousBase, BigDecimal currentBase,
                            BigDecimal previousCounter, BigDecimal currentCounter) {
            return previousBase.compareTo(previousCounter) >= 0 && currentBase.compareTo(currentCounter) < 0;
        }
    };

    /* ----- Fields ----- */

    private final String name;

    /* ----- Constructors ----- */

    IndicatorRelationship(String name) {
        this.name = name;
    }

    /* ----- Methods ----- */

    public abstract boolean test(BigDecimal previousBase, BigDecimal currentBase,
                                 BigDecimal previousCounter, BigDecimal currentCounter);

    @Override
    public String toString() {
        return name;
    }

}
