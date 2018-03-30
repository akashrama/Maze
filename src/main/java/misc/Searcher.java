package misc;

import java.util.Iterator;

import datastructures.concrete.ArrayHeap;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import datastructures.interfaces.IPriorityQueue;

public class Searcher {
    /**
     * This method takes the input list and returns the top k elements
     * in sorted order.
     *
     * So, the first element in the output list should be the "smallest"
     * element; the last element should be the "biggest".
     *
     * If the input list contains fewer then 'k' elements, return
     * a list containing all input.length elements in sorted order.
     *
     * This method must not modify the input list.
     *
     * @throws IllegalArgumentException  if k < 0
     */
    public static <T extends Comparable<T>> IList<T> topKSort(int k, IList<T> input) {
        IPriorityQueue<T> heap = new ArrayHeap<>();
        Iterator<T> iterate = input.iterator();
        IList<T> topK = new DoubleLinkedList<>();
        if (k == 0) {
            return topK;
        }
        if (k < 0) {
            throw new IllegalArgumentException();
        }
       //1)insert k elements from array into heap
       for (int i = 0; i < k; i++) {
            if (iterate.hasNext()) {
                heap.insert(iterate.next());
            }
        }
        
        //2) compare elements one by one to min of heap
        
        //3) if less than min of heap throw away
        
        //4) else remove min of heap and insert the element 
        while (iterate.hasNext()) {
            T current = iterate.next();
            if (current.compareTo(heap.peekMin()) >= 0) {
                heap.removeMin();
                heap.insert(current);
            }
        }
        while (heap.size() != 0) {
            topK.add(heap.removeMin());
        }
        return topK;
    }
 
}
