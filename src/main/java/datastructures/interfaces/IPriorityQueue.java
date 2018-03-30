package datastructures.interfaces;

import misc.exceptions.EmptyContainerException;
import java.util.NoSuchElementException;

/**
 * Represents a queue where the elements are ordered such that the
 * front element is always the "smallest", as defined by the
 * element's compareTo method.
 */
public interface IPriorityQueue<T extends Comparable<T>> {
    /**
     * Removes and return the smallest element in the queue.
     *
     * If two elements within the queue are considered "equal"
     * according to their compareTo method, this method may break
     * the tie arbitrarily and return either one.
     *
     * @throws EmptyContainerException  if the queue is empty
     */
    T removeMin();

    /**
     * Returns, but does not remove, the smallest element in the queue.
     *
     * This method must break ties in the same way the removeMin
     * method breaks ties.
     *
     * @throws EmptyContainerException  if the queue is empty
     */
    T peekMin();

    /**
     * Inserts the given item into the queue.
     *
     * @throws IllegalArgumentException  if the item is null
     */
    void insert(T item);

    /**
     * This method finds the given item in the heap, and removes it.
     *
     * Note: this is an OPTIONAL method. A valid IPriorityQueue is not
     * required to support this operation.
     *
     * (Basically, we are giving you the option to implement this method
     * because it may be useful when implementing Dijkstra's algorithm,
     * depending on how you approach it.)
     *
     * @throws  UnsupportedOperationException  if the IPriorityQueue implementation decides not to support this method
     * @throws  NoSuchElementException  if the given item does not exist
     */
    void remove(T item);

    /**
     * Returns the number of elements contained within this queue.
     */
    int size();

    /**
     * Returns 'true' if this queue is empty, and false otherwise.
     */
    default boolean isEmpty() {
        return this.size() == 0;
    }
}
