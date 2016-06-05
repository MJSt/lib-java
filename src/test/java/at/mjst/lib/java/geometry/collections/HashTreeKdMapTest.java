/*
 * Copyright (c) 2016, Ing. Michael J. Stallinger and/or his affiliates. All rights reserved.
 * This source code is subject to license terms, see the LICENSE file for details.
 */
package at.mjst.lib.java.geometry.collections;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import at.mjst.lib.java.geometry.entities.KdPoint;

/**
 * A simple unit-test for {@link HashTreeKdMap}.
 *
 * @author Ing. Michael J. Stallinger (projects@mjst.at)
 * @since 2016-06-03
 */
public class HashTreeKdMapTest
{
    @Rule
    public final ExpectedException exception = ExpectedException.none();
    private final int[][] TEST_VALUES = {{14, 0, 7}, {23, 54, 22}, {0, 0, 0}};
    private final int[][] MISSING_VALUES = {{14, 4, 7}, {23, 54, 8}, {1, 0, 0}};
    private KdMap<MyDataObject> map;

    @Before
    public void setUp() throws Exception
    {
        map = new HashTreeKdMap<>(3);
        addOnce();
    }

    private void add() throws Exception
    {
        for (int[] TEST_VALUE : TEST_VALUES) {
            map.add(new KdPoint(TEST_VALUE), new MyDataObject(new KdPoint(TEST_VALUE)));
        }
    }

    @Test
    public void addOnce() throws Exception
    {
        map.clear();
        add();
        Assert.assertEquals(TEST_VALUES.length, map.getSize());
    }

    @Test
    public void containsKey() throws Exception
    {
        addOnce();
        for (int[] TEST_VALUE : TEST_VALUES) {
            Assert.assertTrue(map.containsKey(new KdPoint(TEST_VALUE)));
        }
        for (int[] TEST_VALUE : MISSING_VALUES) {
            Assert.assertFalse(map.containsKey(new KdPoint(TEST_VALUE)));
        }
    }

    @Test
    public void get() throws Exception
    {
        addOnce();
        for (int[] TEST_VALUE : TEST_VALUES) {
            KdPoint testPoint = new KdPoint(TEST_VALUE);
            Assert.assertNotNull(map.get(testPoint));
            Assert.assertEquals(testPoint, map.get(testPoint).point);
        }
        for (int[] TEST_VALUE : MISSING_VALUES) {
            Assert.assertNull(map.get(new KdPoint(TEST_VALUE)));
        }
    }

    @Test
    public void addTwice() throws Exception
    {
        addOnce();
        exception.expect(RuntimeException.class);
        add();
    }

    private class MyDataObject
    {
        KdPoint point;

        MyDataObject(KdPoint point)
        {
            this.point = point;
        }
    }
}
