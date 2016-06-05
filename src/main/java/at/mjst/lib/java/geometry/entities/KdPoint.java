/*
 * Copyright (c) 2016, Ing. Michael J. Stallinger and/or his affiliates. All rights reserved.
 * This source code is subject to license terms, see the LICENSE file for details.
 */
package at.mjst.lib.java.geometry.entities;

import java.util.Arrays;
import java.util.HashMap;

import at.mjst.lib.java.geometry.defines.ExceptionText;
import at.mjst.lib.java.math.Random;

/**
 * k-dimensional Point. Implements a point in k-dimensional coordinate space.
 *
 * @author Ing. Michael J. Stallinger (projects@mjst.at)
 * @since 2016-06-01
 */
public class KdPoint
{
    private final int dimensionCount;
    private int[] offsets;

    public KdPoint(int dimensionCount)
    {
        if (dimensionCount <= 0) {
            throw new IllegalArgumentException(ExceptionText.DIMENSION_COUNT_GREATER_ZERO_REQUIRED);
        }
        this.dimensionCount = dimensionCount;
        offsets = generateNewOffsetArray();
    }

    public KdPoint(int[] offsets)
    {
        this(offsets.length);
        System.arraycopy(offsets, 0, this.offsets, 0, dimensionCount);
    }

    public KdPoint(KdPoint point)
    {
        this(point.getDimensionCount());
        assign(point);
    }

    private int[] generateNewOffsetArray()
    {
        return new int[dimensionCount];
    }

    private void validateDimension(int dimension)
    {
        if ((dimension < 0) || (dimension > (dimensionCount - 1))) {
            throw new IndexOutOfBoundsException(String.format("axis %d out of bounds", dimension));
        }
    }

    public void assign(KdPoint point)
    {
        if (getDimensionCount() == point.getDimensionCount()) {
            offsets = point.getOffsets(); // since the offsets are already copied, we can directly assign them here
        } else {
            throw new IllegalArgumentException(String.format("%d-dimensional point required", getDimensionCount()));
        }
    }

    private int[] getOffsets()
    {
        int[] copiedOffsets = generateNewOffsetArray();
        System.arraycopy(offsets, 0, copiedOffsets, 0, getDimensionCount());
        return copiedOffsets;
    }

    public int get(int axis)
    {
        validateDimension(axis);
        return offsets[axis];
    }

    public void set(int axis, int offset)
    {
        validateDimension(axis);
        offsets[axis] = offset;
    }

    /**
     * Generates random coordinates for this point.
     *
     * @param origin Minimum value.
     * @param bound  Maximum value, greater than origin.
     */
    public void setRandom(int origin, int bound)
    {
        for (int i = 0; i < offsets.length; i++) {
            set(i, Random.randInt(origin, bound));
        }
    }

    public int getDimensionCount()
    {
        return dimensionCount;
    }

    /**
     * Indicates whether some other KdPoint is "equal to" this one.
     *
     * @param obj the reference object with which to compare.
     * @return {@code true} if this object is the same as the obj argument; {@code false} otherwise.
     * @see #hashCode()
     * @see HashMap
     */
    @Override
    public boolean equals(Object obj)
    {
        return (super.equals(obj) || (obj instanceof KdPoint) && (dimensionCount == ((KdPoint) obj).getDimensionCount()
                && Arrays.equals(offsets, ((KdPoint) obj).getOffsets())));
    }

    /**
     * Returns a hash code value for the object.
     *
     * @return a hash code value for this object.
     * @see Object#equals(Object)
     * @see System#identityHashCode
     */
    @Override
    public int hashCode()
    {
        return Arrays.hashCode(offsets);
    }

    /**
     * Returns a string representation of the object. In general, this are all offsets for defined dimensions.
     *
     * @return a string representation of the object.
     */
    @Override
    public String toString()
    {
        return Arrays.toString(offsets);
    }

    /**
     * Representation of a 2D-Point
     */
    public static class As2D extends KdPoint
    {
        As2D()
        {
            super(2);
        }
    }

    /**
     * Representation of a 3D-Point
     */
    public static class As3D extends KdPoint
    {
        As3D()
        {
            super(3);
        }
    }
}
