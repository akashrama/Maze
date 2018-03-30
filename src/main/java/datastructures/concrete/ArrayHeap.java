// creates a 4-child min heap
package datastructures.concrete;

import datastructures.interfaces.IPriorityQueue;
import misc.exceptions.EmptyContainerException;

public class ArrayHeap<T extends Comparable<T>> implements IPriorityQueue<T> {
    private static final int NUM_CHILDREN = 4;

    private T[] heap;
            private int size; 
            
    public ArrayHeap() {
            size = 0;
            heap = makeArrayOfT(NUM_CHILDREN);
    }

    @SuppressWarnings("unchecked")
    private T[] makeArrayOfT(int length) {
        return (T[]) (new Comparable[length]);
    }

    @Override
    public T removeMin() {
        if (size == 0) {
            throw new EmptyContainerException(); 
        }
            size--;
            T lastItem = heap[size];
            T item = heap[0];
            heap[0] = lastItem;
            percolateDown();
            return item;
    }
    
    private void percolateDown() {
            int parent = 0;
            while (NUM_CHILDREN  * parent + 1 < size) {
                int minChild = findMin(NUM_CHILDREN  * parent);
                if (heap[minChild].compareTo(heap[parent]) < 0) {
                    T temp = heap[parent];
                    heap[parent] = heap[minChild];
                    heap[minChild] = temp;
                    parent = minChild;
                } else {
                    return;
                }
            }
    }
    
    private int findMin(int location) {
            int minLocation = location + 1;
            T minValue = heap[minLocation];
            for (int i = 2; i <= NUM_CHILDREN && (location + i) < size; i++) {
                if (heap[location + i].compareTo(minValue) < 0) {
                    minValue = heap[location + i];
                    minLocation = location + i;
                }
            }
            return minLocation;
    }

    @Override
    public T peekMin() {
            if (size == 0) {
                throw new EmptyContainerException(); 
            } else {
                return heap[0];
            }
    }

    @Override
    public void insert(T item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        if (size >= heap.length) {
                T[] temp = makeArrayOfT(2 * size);
                for (int i = 0; i < size; i++) {
                    temp[i] = heap[i];
                }
                heap = temp;
        }
        heap[size]= item;
        size++;
        percolateUp();
    }
    
    private void percolateUp() {
        int child = size - 1;
        int parent = (child - 1) / NUM_CHILDREN;
        while (heap[child].compareTo(heap[parent]) < 0) {
            T temp = heap[child];
            heap[child] = heap[parent];
            heap[parent] = temp;
            child = parent;
            parent = (child - 1) / NUM_CHILDREN;
        }
    }

    @Override
    public int size() {
        return size;
    }
}