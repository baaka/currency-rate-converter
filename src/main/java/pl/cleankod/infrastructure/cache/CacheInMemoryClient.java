package pl.cleankod.infrastructure.cache;

import pl.cleankod.infrastructure.cache.inmemory.CacheInMemory;

import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;

public class CacheInMemoryClient<K, V> {
    private final Function<K, V> fetchFunction;
    private final Map<K, V> cache;
    private final ReentrantLock lock;

    public CacheInMemoryClient(Function<K, V> fetchFunction, CacheInMemory<K, V> cache) {
        this.fetchFunction = fetchFunction;
        this.cache = cache;
        this.lock = new ReentrantLock();
    }

    public V fetch(K key) {
        lock.lock();
        try {
            return cache.computeIfAbsent(key, k -> {
                V value = fetchFunction.apply(k);
                if (value != null) {
                    return value;
                }
                throw new IllegalStateException("Fetched value is null for key: " + k);
            });
        } finally {
            lock.unlock();
        }
    }
}
