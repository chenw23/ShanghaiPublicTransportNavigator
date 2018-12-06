package com.fudan.sw.dsa.project2.bean;

import java.util.ArrayList;

/**
 * This is an auxiliary data structure that maintains a minimum heap.
 * The heap is used in the dijkstra algorithm in the process of searching
 * the path.
 *
 * @author Wang, Chen
 */
class MinHeap {

    /**
     * The objects of the heap is stored as an array list
     */
    private ArrayList<Vertex> heap = new ArrayList<>();

    /**
     * Constructor that keeps a copy of the original array list in the class variable
     * and builds the heap after initialized the array list
     *
     * @param elements Here the vertices in the graph
     */
    MinHeap(ArrayList<Vertex> elements) {
        heap.addAll(elements);
        buildMinHeap();
    }

    /**
     * The elements in the subarray
     * {@code heap.subList(heap.size() / 2 + 1, heap.size() - 1)}
     * are are all leaves of the tree, and so each is a 1-element heap to begin with.
     * The procedure {@code buildMinHeap} goes through the remaining nodes of the tree
     * and runs {@code minHeapify} on each one.
     */
    void buildMinHeap() {
        for (int i = heap.size() / 2 + 1; i >= 0; i--)
            minHeapify(i);
    }

    /**
     * The core method of the heap that maintains the array list a valid minimum heap,
     * it is a recursive method that makes the requested node's left child and right child
     * in order. After the modification, it also recursively calls itself on the smallest
     * child so that the sub tree can get maintained.
     */
    private void minHeapify(int i) {
        int l = 2 * i + 1;
        int r = 2 * i + 2;
        int smallest = i;
        if (l < heap.size() && heap.get(l).getDistance() < heap.get(smallest).getDistance())
            smallest = l;
        if (r < heap.size() && heap.get(r).getDistance() < heap.get(smallest).getDistance())
            smallest = r;
        if (smallest != i) {
            Vertex temp = heap.get(i);
            heap.set(i, heap.get(smallest));
            heap.set(smallest, temp);
            minHeapify(smallest);
        }
    }

    /**
     * This method gets and removes the head element from the heap.
     * After extraction, it moves the last element to the top of the heap
     * and heapifies the entire heap.
     */
    Vertex extractMin() {
        if (heap.size() < 1)
            throw new Error("heap underflow");
        Vertex min = heap.remove(0);
        if (heap.size() > 0)
            heap.add(0, heap.remove(heap.size() - 1));
        if (heap.size() > 1)
            minHeapify(0);
        return min;
    }

    /**
     * The status of the heap is identical to the array list
     *
     * @return true if the array list is not empty or false if the
     * array list is empty
     */
    boolean isEmpty() {
        return heap.isEmpty();
    }
}
