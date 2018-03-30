// creates a chained Hash-Set using implementations from ChainedHashDictionary
package datastructures.concrete;

import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.ISet;
// import misc.exceptions.NotYetImplementedException;

import java.util.Iterator;
import java.util.NoSuchElementException;


public class ChainedHashSet<T> implements ISet<T> {
    private IDictionary<T, Boolean> map;

    public ChainedHashSet() {
        this.map = new ChainedHashDictionary<>();
    }

    @Override
    public void add(T item) {
        if (!contains(item)) {
        		map.put(item, true);
        } 
    }

    @Override
    public void remove(T item) {
        if (!contains(item)) {
        		throw new NoSuchElementException();
        } else {
        		map.remove(item);
        }
    }

    @Override
    public boolean contains(T item) {
       return map.containsKey(item);
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public Iterator<T> iterator() {
        return new SetIterator<>(this.map.iterator());
    }

    private static class SetIterator<T> implements Iterator<T> {
        private Iterator<KVPair<T, Boolean>> iter;

        public SetIterator(Iterator<KVPair<T, Boolean>> iter) {
            this.iter = iter;
        }

        @Override
        public boolean hasNext() {
            return iter.hasNext();
        }

        @Override
        public T next() {
            return iter.next().getKey();
        }
    }
}