/*
 * Copyright (c) 2016, Ing. Michael J. Stallinger and/or his affiliates. All rights reserved.
 * This source code is subject to license terms, see the LICENSE file for details.
 */
package at.mjst.lib.java.geometry.entities;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import at.mjst.lib.java.geometry.defines.Axis;

/**
 * A simple unit-test for {@link KdPoint}.
 *
 * @author Ing. Michael J. Stallinger (projects@mjst.at)
 * @since 2016-06-01
 */
public class KdPointTest
{
    @Rule
    public final ExpectedException exception = ExpectedException.none();
    private final int[] TEST_VALUES = {14, 0, 7};
    private KdPoint point = new KdPoint(TEST_VALUES.length);
    private KdPoint newPoint;

    @Before
    public void setUp() throws Exception
    {
        setValues();
    }

    private void setValues()
    {
        for (int i = 0; i < TEST_VALUES.length; i++) {
            point.set(i, TEST_VALUES[i]);
        }
    }

    /**
     * Tests if the object correctly raises an exception, if dimension count is <= 0
     */
    @Test
    public void invalidConstructor() throws Exception
    {
        exception.expect(IllegalArgumentException.class);
        KdPoint pointE = new KdPoint(0);
        pointE.set(Axis.X, 1);
    }

    @Test
    public void enclosedConstructor() throws Exception
    {
        KdPoint pointE = new KdPoint.As3D(); // create 3D-Point
        Assert.assertEquals(point.getDimensionCount(), pointE.getDimensionCount());
    }

    @Test
    public void compareAxis() throws Exception
    {
        Assert.assertEquals(0, Axis.X);
        Assert.assertEquals(1, Axis.Y);
        Assert.assertEquals(2, Axis.Z);
    }

    @Test
    public void equality() throws Exception
    {
        assign();
        Assert.assertEquals(point, newPoint);
    }

    @Test
    public void assign() throws Exception
    {
        newPoint = new KdPoint(point); // assign/clone
        for (int i = 0; i < point.getDimensionCount(); i++) {
            Assert.assertEquals(point.get(i), newPoint.get(i));
        }
    }

    @Test
    public void get() throws Exception
    {
        for (int i = 0; i < TEST_VALUES.length; i++) {
            Assert.assertEquals(TEST_VALUES[i], point.get(i));
        }
    }

    @Test
    public void set() throws Exception
    {
        setValues(); // ok, if no exception is thrown
    }

    /**
     * Tests if the object correctly raises an exception, if higher dimension than defined is addressed.
     */
    @Test
    public void setOutOfBounds() throws Exception
    {
        exception.expect(IndexOutOfBoundsException.class);
        point.set(TEST_VALUES.length, 47);
    }

    @Test
    public void getDimensionCount() throws Exception
    {
        Assert.assertEquals(point.getDimensionCount(), 3);
    }
//    @Test
//    public void iterator() throws Exception
//    {
//        int i = 0;
//        for (Integer value : point) {
//            Assert.assertEquals(TEST_VALUES[i], value.intValue());
//            i++;
//        }
//    }
}
