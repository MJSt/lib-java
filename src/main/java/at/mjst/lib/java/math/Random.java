/*
 * Copyright (c) 2016, Ing. Michael J. Stallinger and/or his affiliates. All rights reserved.
 * This source code is subject to license terms, see the LICENSE file for details.
 */
package at.mjst.lib.java.math;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Support methods for generating randomized data.
 *
 * @author Ing. Michael J. Stallinger (projects@mjst.at)
 * @since 2016-06-04
 */
public class Random
{
    /**
     * Returns a pseudo-random number between origin and bound, inclusive. The difference between origin and bound can
     * be at most <code>Integer.MAX_VALUE - 1</code>.
     *
     * @param origin Minimum value.
     * @param bound  Maximum value. Must be greater than origin.
     * @return Integer between origin and bound, inclusive.
     * @see java.util.Random#nextInt(int)
     */
    public static int randInt(int origin, int bound)
    {
        // nextInt is normally exclusive of the top value, so add 1 to make it inclusive...
        return ThreadLocalRandom.current().nextInt(origin, bound + 1);
    }
}
