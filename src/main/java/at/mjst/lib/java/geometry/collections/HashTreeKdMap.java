/*
 * Copyright (c) 2016, Ing. Michael J. Stallinger and/or his affiliates. All rights reserved.
 * This source code is subject to license terms, see the LICENSE file for details.
 */
package at.mjst.lib.java.geometry.collections;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import at.mjst.lib.java.geometry.defines.ExceptionText;
import at.mjst.lib.java.geometry.entities.KdPoint;

/**
 * Stores objects of type {@code DataType} identified by KdPoint {@link KdPoint}. Designed to speed up finding these
 * objects by their corresponding coordinates using a tree managed by a HashMap for each coordinate-axis. The method
 * {@link #add(KdPoint, Object)} may be a bit slow. {@link KdPoint#getDimensionCount()} must fit {@link #dimensionCount}
 * specified for this map!
 *
 * @author Ing. Michael J. Stallinger (projects@mjst.at)
 * @since 2016-05-31
 */
class HashTreeKdMap<DataType> implements KdMap<DataType>
{
    private int dimensionCount;
    private Node root;
    private List<Container> index;

    HashTreeKdMap(int dimensionCount)
    {
        if (dimensionCount <= 0) {
            throw new IllegalArgumentException(ExceptionText.DIMENSION_COUNT_GREATER_ZERO_REQUIRED);
        }
        this.dimensionCount = dimensionCount;
        clear(); // clears the structure by recreating the root-node
    }

    @Override
    public int getSize()
    {
        return index.size();
    }

    private Node locateNode(KdPoint key, boolean doCreate)
    {
        if (key.getDimensionCount() != dimensionCount) {
            throw new IllegalArgumentException(String.format("dimension count must be %d", dimensionCount));
        }
        Node currentNode = root;
        int axis = 0;
        do {
            currentNode = currentNode.getSubNode(key.get(axis), doCreate);
            if (currentNode == null) {
                return null; // add: did not work; get: nothing found
            }
            axis++;
        } while (axis < dimensionCount);
        return currentNode;
    }

    @Override
    public void add(KdPoint key, DataType data)
    {
        Node node = locateNode(key, true);
        if (node != null) {
            if (node.hasLeaf()) {
                throw new RuntimeException("data already set for this key"); // added, but there's already an element
            } else {
                node.setLeaf(new Container(data)); // successful add()
            }
        } else {
            throw new RuntimeException("error building node-tree");
        }
        index.add(node.getLeaf());
    }

    @Override
    public boolean containsKey(KdPoint key)
    {
        return (locateNode(key, false) != null);
    }

    @Override
    public DataType get(KdPoint key)
    {
        Node node = locateNode(key, false);
        if (node != null) {
            if (node.hasLeaf()) {
                return node.getLeaf().getData(); // successful get() - return, whatever the container contains ;)
            } else {
                throw new RuntimeException("leaf not found"); // nothing to get, although Map already created
            }
        } else {
            return null; // element not found
        }
    }

    @Override
    public void clear()
    {
        root = new Node();
        index = new ArrayList<>();
    }

    /**
     * Returns an iterator over elements of type {@code DataType}.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<DataType> iterator()
    {
        return new MapIterator();
    }

    private class Node
    {
        private Map<Integer, Node> map;
        private Container leaf;

        Node getSubNode(int offset, boolean doCreate)
        {
            if (map == null) {
                if (!doCreate) {
                    return null; // no need to create map, if elements are only read...
                }
                map = new HashMap<>();
            }
            if (map.containsKey(offset)) {
                return map.get(offset);
            } else if (doCreate) {
                Node node = new Node();
                map.put(offset, node);
                return node;
            }
            return null; // not found or not created
        }

        boolean hasLeaf()
        {
            return leaf != null;
        }

        Container getLeaf()
        {
            return this.leaf;
        }

        void setLeaf(Container container)
        {
            this.leaf = container;
        }
    }

    private class Container
    {
        private DataType data;

        Container(DataType data)
        {
            setData(data);
        }

        DataType getData()
        {
            return data;
        }

        void setData(DataType data)
        {
            this.data = data;
        }
    }

    private class MapIterator implements Iterator<DataType>
    {
        private int current = 0;

        @Override
        public boolean hasNext()
        {
            return (current < index.size());
        }

        @Override
        public DataType next()
        {
            current++;
            return index.get(current - 1).getData();
        }
    }
}
