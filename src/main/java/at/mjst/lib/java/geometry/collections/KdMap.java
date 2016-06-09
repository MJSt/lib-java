/*
 * Copyright (c) 2016, Ing. Michael J. Stallinger and/or his affiliates. All rights reserved.
 * This source code is subject to license terms, see the LICENSE file for details.
 */
package at.mjst.lib.java.geometry.collections;

import at.mjst.lib.java.geometry.entities.KdPoint;

/**
 * Stores objects of type {@code DataType} identified by {@link KdPoint}.
 *
 * @author Ing. Michael J. Stallinger (projects@mjst.at)
 * @since 2016-06-04
 */
public interface KdMap<DataType> extends Iterable<DataType>
{
    /**
     * Adds an item to the map. A key with same offsets cannot be added twice!
     *
     * @param key  {@link KdPoint} to identify the new entry.
     * @param data Object of Type {@code DataType}.
     */
    void add(KdPoint key, DataType data);

    /**
     * Checks, if a specific {@link KdPoint} does exist.
     *
     * @param key The {@link KdPoint} being asked for.
     * @return True, if stored in map, false otherwise.
     */
    boolean containsKey(KdPoint key);

    /**
     * Returns an object of Type {@code DataType} identified by key.
     *
     * @param key The {@link KdPoint} being asked for.
     * @return Object formerly added by method {@link #add(KdPoint, Object)}.
     */
    DataType get(KdPoint key);

    /**
     * @return current count of entries within this map.
     */
    int getSize();

    /**
     * Destroys all entries within this map by recreating nodes and indices.
     */
    void clear();
}
