// creates a Disjoint Set
package datastructures.concrete;


import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IDisjointSet;

/**
 * See IDisjointSet for more details.
 */
public class ArrayDisjointSet<T> implements IDisjointSet<T> {
    private int[] pointers;
    //private int size;
    private IDictionary<T, Integer> storage;
    private int value;
    

    // However, feel free to add more methods and private helper methods.
    // You will probably need to add one or two more fields in order to
    // successfully implement this class.

    public ArrayDisjointSet() {
        this.pointers = new int[1];
        this.storage = new ChainedHashDictionary<T, Integer>();
        //this.size = size;
        this.value = 0;
    }

    @Override
    public void makeSet(T item) {
        if (storage.containsKey(item)) {
            throw new IllegalArgumentException();
        }
        storage.put(item, value);
        int[] newSize = new int[value + 1];
        for (int i = 0; i < pointers.length; i++) {
            newSize[i] = pointers[i];
        }
        newSize[value] = -1;
        pointers = newSize;
        value++;
    }

    @Override
    public int findSet(T item) {
        if (!storage.containsKey(item)) {
            throw new IllegalArgumentException();
        }
        value = storage.get(item);
        int point = value;
        int second = 0;
        while (pointers[value] >= 0) {
            value = pointers[value];
        }
        second = value;
        while (pointers[point] >= 0) {
            int third = point;
            point = pointers[point];
            pointers[third] = second;
        }
        return value;
    }

    @Override
    public void union(T item1, T item2) {
        if (!storage.containsKey(item1) || !storage.containsKey(item2)) {
            throw new IllegalArgumentException();
        }
        int first = findSet(item1);
        int second = findSet(item2);
        if (first == second) {
            throw new IllegalArgumentException();
        }
        int prior = 0;
        if (pointers[first] < pointers[second]) {
            prior = pointers[first];
            pointers[second] = first;
        } else if (pointers[first] > pointers[second]){
            prior = pointers[second];
            pointers[first] = second;
        } else {
            prior = pointers[first] - 1;
            pointers[first] = second;
            pointers[second] = prior;
        }
    }
}
