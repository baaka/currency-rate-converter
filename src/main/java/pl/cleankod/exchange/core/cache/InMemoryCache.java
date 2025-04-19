package pl.cleankod.exchange.core.cache;

import java.util.LinkedHashMap;

public class InMemoryCache<K, V> extends LinkedHashMap<K, V> {
    public InMemoryCache(int initialCapacity,
                         float loadFactor,
                         boolean accessOrder) {
        super(initialCapacity, loadFactor, accessOrder);
    }
}
