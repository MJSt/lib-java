package at.mjst.lib.java.geometry.collections;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import at.mjst.lib.java.geometry.entities.KdPoint;

/**
 * Tests performance of {@link HashTreeKdMap} against a simple implementation of {@link HashMap}.
 *
 * @author Ing. Michael J. Stallinger (projects@mjst.at)
 * @since 2016-06-03
 */
public class HashTreeKdMapPerfTest
{
    private static final int TRIALS = 500000;
    private static long[] lastDelta = new long[2];

    @Before
    public void setUp() throws Exception
    {
    }

    @After
    public void tearDown() throws Exception
    {
        if ((lastDelta[0] > 0) && (lastDelta[0] > 0)) {
            System.out.println("Multiplier: " + (double) lastDelta[0] / (double) lastDelta[1]);
        }
    }

    private void fill(KdMap<PerfTestObj> map)
    {
        KdPoint testPoint;
        int i = 0;
        while (i < TRIALS) {
            testPoint = new KdPoint(3);
            testPoint.setRandom(0, 99);
            if (!map.containsKey(testPoint)) {
                map.add(testPoint, new PerfTestObj());
                i++;
            }
        }
    }

    private long testContainsKey(KdMap<PerfTestObj> map) throws Exception
    {
        KdPoint testPoint;
        int found = 0;
        int notFound = 0;
        long startTime = System.nanoTime();
        for (int i = 0; i < TRIALS; i++) {
            testPoint = new KdPoint(3);
            testPoint.setRandom(0, 99);
            if (map.containsKey(testPoint)) {
                found++;
            } else {
                notFound++;
            }
        }
        long endTime = System.nanoTime();
        long delta = (endTime - startTime);
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        formatter.setDecimalFormatSymbols(symbols);
        System.out.println("Cost="
                + formatter.format(delta)
                + " Results: "
                + found
                + "/"
                + notFound
                + " for "
                + map.getClass().getName());
        return delta;
    }

    @Test
    public void testPseudoMap() throws Exception
    {
        KdMap<PerfTestObj> map = new PseudoCompareMap<>(3);
        fill(map);
        lastDelta[0] = testContainsKey(map);
    }

    @Test
    public void testHashTreeMap() throws Exception
    {
        KdMap<PerfTestObj> map = new HashTreeKdMap<>(3);
        fill(map);
        lastDelta[1] = testContainsKey(map);
    }

    private class PerfTestObj
    {
    }

    private class PseudoCompareMap<DataType> implements KdMap<DataType>
    {
        int dimensionCount;
        Map<KdPoint, DataType> index;

        PseudoCompareMap(int dimensionCount)
        {
            this.dimensionCount = dimensionCount;
            clear();
        }

        /**
         * Adds an item to the map. A key with same offsets cannot be added twice!
         *
         * @param key  {@link KdPoint} to identify the new entry.
         * @param data Object of Type {@code DataType}.
         */
        @Override
        public void add(KdPoint key, DataType data)
        {
            if (!containsKey(key)) {
                index.put(key, data);
            } else {
                throw new RuntimeException("Already added!");
            }
        }

        /**
         * Checks, if a specific {@link KdPoint} does exist.
         *
         * @param key The {@link KdPoint} being asked for.
         * @return True, if stored in map, false otherwise.
         */
        @Override
        public boolean containsKey(KdPoint key)
        {
            return index.containsKey(key);
        }

        /**
         * Returns an object of Type {@code DataType} identified by key.
         *
         * @param key The {@link KdPoint} being asked for.
         * @return Object formerly added by method {@link #add(KdPoint, Object)}.
         */
        @Override
        public DataType get(KdPoint key)
        {
            return index.get(key);
        }

        /**
         * @return current count of entries within this map.
         */
        @Override
        public int getSize()
        {
            return index.size();
        }

        /**
         * Destroys all entries within this map by recreating nodes and indices.
         */
        @Override
        public void clear()
        {
            index = new HashMap<>();
        }

        /**
         * Returns an iterator over elements of type {@code T}.
         *
         * @return an Iterator.
         */
        @Override
        public Iterator<DataType> iterator()
        {
            return null;
        }
    }
}
