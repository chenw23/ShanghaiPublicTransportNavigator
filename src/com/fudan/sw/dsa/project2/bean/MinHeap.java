package com.fudan.sw.dsa.project2.bean;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;

class MinHeap {
    private ArrayList<Vertex> heap = new ArrayList<>();

    MinHeap(ArrayList<Vertex> elements) {
        heap.addAll(elements);
        buildMinHeap();
    }

    @Contract(pure = true)
    private int left(int i) {
        return 2 * i + 1;
    }

    @Contract(pure = true)
    private int right(int i) {
        return 2 * i + 2;
    }

    void buildMinHeap() {
        for (int i = heap.size() / 2 + 1; i >= 0; i--)
            minHeapify(i);
    }

    private void minHeapify(int i) {
        int l = left(i);
        int r = right(i);
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

    boolean isEmpty() {
        return heap.isEmpty();
    }
}
