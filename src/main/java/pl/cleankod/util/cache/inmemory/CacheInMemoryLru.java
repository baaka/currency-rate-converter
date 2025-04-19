package pl.cleankod.util.cache.inmemory;

import java.util.Map;

public class CacheInMemoryLru<K, V> extends CacheInMemory<K, V> {
    private final int maxSize;

    public CacheInMemoryLru(int maxSize) {
        super(maxSize, 0.75f, true);
        this.maxSize = maxSize;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return this.size() > this.maxSize;
    }
}
