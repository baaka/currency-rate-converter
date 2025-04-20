package pl.cleankod.infrastructure.cache.inmemory;

import java.util.LinkedHashMap;

public class CacheInMemory<K, V> extends LinkedHashMap<K, V> {
    public CacheInMemory(int initialCapacity,
                         float loadFactor,
                         boolean accessOrder) {
        super(initialCapacity, loadFactor, accessOrder);
    }
}
