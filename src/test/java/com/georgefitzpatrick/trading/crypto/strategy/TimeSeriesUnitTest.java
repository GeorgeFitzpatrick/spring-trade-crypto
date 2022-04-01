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

    /* ----- Constructors ----- */

    public TimeSeriesUnitTest() {

    }

    /* ----- Methods ----- */

    @Test
    public void testGet_normalPeriod() {
        Instant now = Instant.now();

        TimeSeries<Integer> series = new TimeSeries<>(THREE_DAYS);
        series.put(now, 0);
        series.put(now.minus(ONE_DAY), 1);
        series.put(now.minus(TWO_DAYS), 2);

        Assertions.assertEquals(1, series.get(ONE_DAY, 1));
    }

    @Test
    public void testGet_boundaryPeriod() {
        Instant now = Instant.now();

        TimeSeries<Integer> series = new TimeSeries<>(THREE_DAYS);
        series.put(now, 0);
        series.put(now.minus(ONE_DAY), 1);
        series.put(now.minus(TWO_DAYS), 2);

        Assertions.assertNull(series.get(TWO_DAYS, 2));
    }

    @Test
    public void testGet_invalidPeriod() {
        Instant now = Instant.now();

        TimeSeries<Integer> series = new TimeSeries<>(THREE_DAYS);
        series.put(now, 0);
        series.put(now.minus(ONE_DAY), 1);
        series.put(now.minus(TWO_DAYS), 2);

        Assertions.assertNull(series.get(ONE_DAY, 3));
    }

    @Test
    public void testGetAll_normalPeriod() {
        Instant now = Instant.now();

        TimeSeries<Integer> series = new TimeSeries<>(THREE_DAYS);
        series.put(now, 0);
        series.put(now.minus(ONE_DAY), 1);
        series.put(now.minus(TWO_DAYS), 2);

        Assertions.assertEquals(List.of(2, 1, 0), series.getAll(ONE_DAY, 2));
    }

    @Test
    public void testGetAll_invalidPeriod() {
        Instant now = Instant.now();

        TimeSeries<Integer> series = new TimeSeries<>(THREE_DAYS);
        series.put(now, 0);
        series.put(now.minus(ONE_DAY), 1);
        series.put(now.minus(TWO_DAYS), 2);

        Assertions.assertNull(series.getAll(ONE_DAY, 3).get(0));
    }

}
