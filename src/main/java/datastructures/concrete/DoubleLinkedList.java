// creates a DoubleLinkedList
package datastructures.concrete;

import datastructures.interfaces.IList;
import misc.exceptions.EmptyContainerException;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class DoubleLinkedList<T> implements IList<T> {
    private Node<T> front;
    private Node<T> back;
    private int size;

    public DoubleLinkedList() {
        this.front = null;
        this.back = null;
        this.size = 0;
    }

    @Override
    public void add(T item) {
        Node<T> added = new Node<T>(item);
        if (size != 0) {
            back.next = added;
            added.prev = back; 
        }
        else {
            front = added;
        }
        back = added;
        size++;
    }

    @Override
    public T remove() {
        if (size == 0) {
            throw new EmptyContainerException();
        }
        size--;
        T temp = back.data;
        back = back.prev;
        if (size == 0) {
            front = null;
        } else {
            back.next = null;
        }
        return temp;
    }
    
    private Node<T> findCurrent(int index) {
        if (index < 0 || index >= this.size) {
            throw new IndexOutOfBoundsException();
        }
        int half = size / 2;
        Node<T> current = front;
        if (index < half) {
            for (int i = 0; i < index; i++) {
                current = current.next;
            }            
        } else {
            current = back;
            for (int i = size - 1; i > index; i--) {
                current = current.prev;
            }
        }
        return current;
    }

    @Override
    public T get(int index) {
        Node<T> current = findCurrent(index);
        return current.data;
    }

    @Override
    public void set(int index, T item) {
        Node<T> added = new Node<T>(item);
        if (size == 1) {
            front = added;
            back = added;
        } else {
            if (index < 0 || index >= this.size) {
                throw new IndexOutOfBoundsException();
            }
            Node<T> current = findCurrent(index);
            if (current == front) {
                current.next.prev = added;
                added.next = current.next;
                front = added;
            } else if (current == back) {
                current.prev.next = added; 
                added.prev = current.prev;
                back = added;
            } else {
                current.prev.next = added;
                current.next.prev = added;
                added.prev = current.prev;
                added.next = current.next;
            }
        }
    }

    @Override
    public void insert(int index, T item) {
        if (index < 0 || index > this.size) {
            throw new IndexOutOfBoundsException();
        } else {
            int half = size / 2;
            Node<T> current = front;
            if (index < half) {
                for (int i = 0; i < index; i++) {
                    current = current.next;
                }            
            } else {
                current = back;
                for (int i = size - 1; i > index; i--) {
                    current = current.prev;
                }
            }      
            Node<T> temp = new Node<T>(item);
            temp.next = current; 
            if (index == size || index == 0) {
                if (index == 0) {
                    if (size != 0) {
                        front.prev = temp;
                    }
                    front = temp;
                }
                if (index == size) {
                    if (size != 0) {
                        back.next = temp;
                    }
                    temp.prev = back;
                    back = temp;
                }
            } else {
                current.prev.next = temp;
                temp.prev= current.prev;
                current.prev = temp;
            }
            size++;
        }
    }

    @Override
    public T delete(int index) {
        Node<T> current = findCurrent(index);
        T init = current.data;
        Node<T> beg = current.prev;
        Node<T> end = current.next;
        if (size == 1) {
            back = null; 
            front = null;
        } else if (current == front) {
            front = end;
            front.prev = null;
        } else if (current == back) {
            back = beg;
            back.next = null;
        } else {
            beg.next = end;
            end.prev = beg;
        }
        size--;
        return init;
    }

    @Override
    public int indexOf(T item) {
        Node<T> current = front;
        for (int i = 0; i < this.size; i++) {
            if (current.data == item || current.data.equals(item)) {
                return i;
            }
            current = current.next;
        }
        return -1;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean contains(T other) {
        return indexOf(other) >= 0;
    }

    @Override
    public Iterator<T> iterator() {
        return new DoubleLinkedListIterator<>(this.front);
    }

    private static class Node<E> {
        public final E data;
        public Node<E> prev;
        public Node<E> next;

        public Node(Node<E> prev, E data, Node<E> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }

        public Node(E data) {
            this(null, data, null);
        }

    }

    private static class DoubleLinkedListIterator<T> implements Iterator<T> {
        private Node<T> current;

        public DoubleLinkedListIterator(Node<T> current) {
            this.current = current;
        }

        public boolean hasNext() {
            return current != null;
        }

        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            T data = current.data;
            current = current.next;
            return data;
        }
    }
}