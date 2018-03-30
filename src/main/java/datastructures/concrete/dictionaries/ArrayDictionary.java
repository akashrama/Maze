// creates an ArrayDictionary otherwise know as a Map
package datastructures.concrete.dictionaries;

import java.util.Iterator;
import java.util.NoSuchElementException;

import datastructures.concrete.KVPair;
import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;

/**
 * See IDictionary for more details on what this class should do
 */
public class ArrayDictionary<K, V> implements IDictionary<K, V> {
    private Pair<K, V>[] pairs;
    private int size; 

    public ArrayDictionary() {
        pairs = makeArrayOfPairs(20);
        size = 0;   
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain Pair<K, V> objects.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private Pair<K, V>[] makeArrayOfPairs(int arraySize) {
        return (Pair<K, V>[]) (new Pair[arraySize]);
    }

    @Override
    public V get(K key) {
        int i = index(key);
        if (i >= 0) {
            return pairs[i].value;
        }
        throw new NoSuchKeyException();
    }
    
    private int index(K key) {
        for (int i = 0; i < size; i++) {
        		if (key != null) {
        			if (pairs[i].key == key || key.equals(pairs[i].key)) {
        				return i;
        			} 
        		} else {
        			if (pairs[i].key == key || pairs[i].key.equals(key)) {
        				return i;
        			} 
        		}
        }
        return -1;
    }

    @Override
    public void put(K key, V value) {
        int index = index(key);
        if (index != -1) {
            pairs[index].value = value;
        } else {
            Pair<K, V>[] temp = makeArrayOfPairs(size + 1);
            for (int i = 0; i < size; i++) {
                temp[i] = pairs[i];
            }
            Pair<K, V> add = new Pair<K, V>(key, value);
            temp[size] = add;
            pairs = temp;
            size++;
        }
    }

    @Override
    public V remove(K key) {
        int index = index(key);
        V result;
        if (index >= 0) {
            size--;
            result = pairs[index].value;
            for (int i = index; i < size; i++) {
                pairs[i] = pairs[i+1];
            }
            return result;
        } else {
            throw new NoSuchKeyException();
        }
    }

    @Override
    public boolean containsKey(K key) {
        return index(key) >= 0;
    }

    @Override
    public int size() {
        return size;
    }

    private static class Pair<K, V> {
        public K key;
        public V value;

        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return this.key + "=" + this.value;
        }
    }
    
    public Iterator<KVPair<K, V>> iterator() {
        return new ArrayDictionaryIterator<>(this.pairs, this.size);
    }
       
    private static class ArrayDictionaryIterator<K, V> implements Iterator<KVPair<K, V>> {
        private int pos;
        private int size;
        private Pair<K, V>[] pairs;
        
        public ArrayDictionaryIterator(Pair<K, V>[] pairs, int size) {
            this.pairs = pairs;
            pos = 0;
            this.size = size;
        }
        
        

        @Override
        public boolean hasNext() {
            return (pos < size);
        }

        @Override
        public KVPair<K, V> next() {
            K key;
            V value;
            if (hasNext()) {
                if (pairs[pos].key != null) {
                    key = pairs[pos].key;
                } else {
                    key = null;
                }
                
                value = pairs[pos].value;
                pos++;
                KVPair<K, V> now = new KVPair<>(key, value);   
                return now;
            } else {
                throw new NoSuchElementException();
            }
        }
    }
}
