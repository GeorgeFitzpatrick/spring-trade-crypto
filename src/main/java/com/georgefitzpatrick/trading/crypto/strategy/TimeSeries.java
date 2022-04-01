package com.georgefitzpatrick.trading.crypto.strategy;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

/**
 * @author George Fitzpatrick
 */
public class TimeSeries<V> extends AbstractMap<Instant, V> {

    /* ----- Fields ----- */

    private final TreeMap<Instant, V> delegate;

    private Duration maxAge;

    /* ----- Constructors ----- */

    public TimeSeries(Duration maxAge) {
        this.delegate = new TreeMap<>();
        this.maxAge = maxAge;
    }

    /* ----- Methods ----- */

    public Duration getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(Duration maxAge) {
        this.maxAge = maxAge;
    }

    @Override
    public V put(Instant key, V value) {
        evict();
        return delegate.put(key, value);
    }

    @Override
    public Set<Entry<Instant, V>> entrySet() {
        evict();
        return delegate.entrySet();
    }

    private void evict() {
        // calculate the oldest valid key for this.maxAge
        Instant threshold = Instant.now().minus(maxAge);

        // find the youngest key that is older than the threshold
        Instant floor = delegate.floorKey(threshold);

        // remove all entries with keys older than the threshold
        if (floor != null) {
            delegate.headMap(floor).clear();
        }
    }

    public V get(Duration timeframe, int period) {
        return get(timeframe, period, Instant.now());
    }

    private V get(Duration timeframe, int period, Instant from) {
        evict();

        // calculate the youngest key valid for the given timeframe and period
        Instant threshold = from.minus(timeframe.multipliedBy(period));

        // find the youngest entry older than threshold
        Entry<Instant, V> entry = delegate.floorEntry(threshold);

        // if entry exists return its value otherwise return null
        return entry != null ? entry.getValue() : null;
    }

    public List<V> getAll(Duration timeframe, int period) {
        return getAll(timeframe, period, 0);
    }

    public List<V> getAll(Duration timeframe, int fromPeriod, int toPeriod) {
        Instant now = Instant.now();
        List<V> values = new ArrayList<>();
        if (fromPeriod < toPeriod) {
            // add values from lower period to higher period
            for (int i = fromPeriod; i <= toPeriod; i++) {
                values.add(get(timeframe, i, now));
            }
        } else {
            // add values from higher period to lower period
            for (int i = fromPeriod; i >= toPeriod; i--) {
                values.add(get(timeframe, i, now));
            }
        }
        return values;
    }

    public boolean availableFor(Duration timeframe, int period) {
        return get(timeframe, period) != null;
    }

}
