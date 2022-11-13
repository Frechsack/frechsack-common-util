package frechsack.prod.util.collection;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class HashMultiMap<Key, Value> extends AbstractMultiMap<Key, Value>{
    
    private final HashMap<Key, Collection<Value>> map;

    public HashMultiMap(int initialCapacity, float loadFactor) {
        map = new HashMap<>(initialCapacity, loadFactor);
    }

    public HashMultiMap(int initialCapacity) {
        map = new HashMap<>(initialCapacity);
    }

    public HashMultiMap() {
        map = new HashMap<>();
    }

    public HashMultiMap(Map<? extends Key, ? extends Collection<Value>> m) {
        map = new HashMap<>(m);
    }

    @Override
    protected Map<Key, Collection<Value>> getModel() {
        return map;
    }
}
