// creates a ChainedHashDictionary otherwise known as a Hash Map using
//implementations form ArrayDictionary
package datastructures.concrete.dictionaries;

import datastructures.concrete.KVPair;
import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;
//import misc.exceptions.NotYetImplementedException;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ChainedHashDictionary<K, V> implements IDictionary<K, V> {
    private IDictionary<K, V>[] chains;
    private int numKey;

    public ChainedHashDictionary() {
        chains = makeArrayOfChains(3);
        numKey = 0;
    }
    

    /**
     * This method will return a new, empty array of the given chains.length
     * that can contain IDictionary<K, V> objects.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private IDictionary<K, V>[] makeArrayOfChains(int size) {
        // Note: You do not need to modify this method.
        // See ArrayDictionary's makeArrayOfPairs(...) method for
        // more background on why we need this method.
        return (IDictionary<K, V>[]) new IDictionary[size];
    }

    @Override
    public V get(K key) {
        int i = index(key);
        if (chains[i] != null) {
            return chains[i].get(key);
        }
        throw new NoSuchKeyException();
    }
    
    private int index(K key) {
        if (key == null) {
            return 0;
        } else {
            int code = Math.abs(key.hashCode()) % chains.length;
            return code;
        }
    }

    @Override
    public void put(K key, V value) {
        int index = index(key);
        if (chains[index] != null && chains[index].containsKey(key)) {
            chains[index].put(key, value);
        } else {
            numKey++;
            double lamda = 1.0 * numKey / chains.length;
            if (lamda < 0.9) {
               if (chains[index] == null) {
                   IDictionary<K, V> sub = new ArrayDictionary<K, V>();
                   chains[index] = sub;
               }
                chains[index].put(key, value);
            } else {
                IDictionary<K, V>[] temp = makeArrayOfChains(chains.length * 2);
                for (int i = 0; i < chains.length; i++) {
                    if (chains[i] != null) {
                        Iterator<KVPair<K, V>> current =  chains[i].iterator();
                        while (current.hasNext()) {
                            KVPair<K, V> pair = current.next();
                            int code = Math.abs(pair.getKey().hashCode()) % temp.length;
                            if (temp[code] == null) {
                                IDictionary<K, V> sub = new ArrayDictionary<K, V>();
                                temp[code] = sub;
                            }
                            temp[code].put(pair.getKey(), pair.getValue());
                        }
                    }
                }
                if (key == null) {
                        index = 0;
                } else {
                        index = Math.abs(key.hashCode()) % temp.length;
                }
                
                if (temp[index] == null) {
                    IDictionary<K, V> sub = new ArrayDictionary<K, V>();
                    temp[index] = sub;
                }
                temp[index].put(key, value);
                chains = temp;
            }
        }
    }

    @Override
    public V remove(K key) {
        
        int index = index(key);
        if (chains[index] != null && chains[index].containsKey(key)) {
            numKey--;
            return chains[index].remove(key);
        } else {
            throw new NoSuchKeyException();
        }
    }

    @Override
    public boolean containsKey(K key) {
        int index = index(key);
        if (chains[index] != null) {
            return chains[index].containsKey(key);
        }
        return false;
    }

    @Override
    public int size() {
        return numKey;
    }

    @Override
    public Iterator<KVPair<K, V>> iterator() {
        return new ChainedIterator<>(this.chains);
    }

    private static class ChainedIterator<K, V> implements Iterator<KVPair<K, V>> {
        private IDictionary<K, V>[] chains;
        private int pos;
        private Iterator<KVPair<K, V>> current;

        public ChainedIterator(IDictionary<K, V>[] chains) {
            this.chains = chains;
            this.pos = 0;
            this.current = null;
            
        }

        @Override
        public boolean hasNext() {
            for (; pos < chains.length; pos++) {
                    if (current == null && chains[pos] != null) {
                        current =  chains[pos].iterator();
                    } 
                    if (current != null) {
                        if (current.hasNext()) {
                            return true;
                        } 
                        current = null; // reset the iterator 
                    }
            }
            return false;
        }

        @Override
        public KVPair<K, V> next() {
            if (hasNext()) {
               return current.next();
            } else {
                throw new NoSuchElementException();
            }
        }
    }
}