package com.georgefitzpatrick.trading.crypto.strategy;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;

/**
 * @author George Fitzpatrick
 */
public class TimeSeriesUnitTest {

    /* ----- Fields ----- */

    private static final Duration ONE_DAY = Duration.of(1, DAYS);
    private static final Duration TWO_DAYS = Duration.of(2, DAYS);
    private static final Duration THREE_DAYS = Duration.of(3, DAYS);
    private static final Duration FOUR_DAYS = Duration.of(4, DAYS);
    private static final Duration FIVE_DAYS = Duration.of(5, DAYS);
    private static final Duration SIX_DAYS = Duration.of(6, DAYS);
    private static final Duration ONE_WEEK = Duration.of(7, DAYS);

    /* ----- Constructors ----- */

    public TimeSeriesUnitTest() {

    }

    /* ----- Methods ----- */

    @Test
    public void testGet() {
        Instant now = Instant.now();

        TimeSeries<Integer> series = new TimeSeries<>(ONE_WEEK);
        series.put(now.minus(SIX_DAYS), 1);
        series.put(now.minus(FIVE_DAYS), 2);
        series.put(now.minus(FOUR_DAYS), 3);
        series.put(now.minus(THREE_DAYS), 4);
        series.put(now.minus(TWO_DAYS), 5);
        series.put(now.minus(ONE_DAY), 6);
        series.put(now, 7);

        Assertions.assertEquals(7, series.get(ONE_DAY, 0));
        Assertions.assertEquals(6, series.get(ONE_DAY, 1));
        Assertions.assertEquals(5, series.get(ONE_DAY, 2));
        Assertions.assertEquals(4, series.get(ONE_DAY, 3));
        Assertions.assertEquals(3, series.get(ONE_DAY, 4));
        Assertions.assertEquals(2, series.get(ONE_DAY, 5));
        Assertions.assertEquals(1, series.get(ONE_DAY, 6));
    }

    @Test
    public void testGetAll() {
        Instant now = Instant.now();

        TimeSeries<Integer> series = new TimeSeries<>(ONE_WEEK);
        series.put(now.minus(SIX_DAYS), 1);
        series.put(now.minus(FIVE_DAYS), 2);
        series.put(now.minus(FOUR_DAYS), 3);
        series.put(now.minus(THREE_DAYS), 4);
        series.put(now.minus(TWO_DAYS), 5);
        series.put(now.minus(ONE_DAY), 6);
        series.put(now, 7);

        Assertions.assertEquals(List.of(1, 2, 3, 4, 5, 6, 7), series.getAll(ONE_DAY, 6));
    }

    @Test
    public void testAvailableFor() {
        Instant now = Instant.now();

        TimeSeries<Integer> series = new TimeSeries<>(ONE_WEEK);
        series.put(now.minus(SIX_DAYS), 1);
        series.put(now.minus(FIVE_DAYS), 2);
        series.put(now.minus(FOUR_DAYS), 3);
        series.put(now.minus(THREE_DAYS), 4);
        series.put(now.minus(TWO_DAYS), 5);
        series.put(now.minus(ONE_DAY), 6);
        series.put(now, 7);

        Assertions.assertTrue(series.availableFor(ONE_DAY, 6));
        Assertions.assertFalse(series.availableFor(ONE_DAY, 7));
    }

}
