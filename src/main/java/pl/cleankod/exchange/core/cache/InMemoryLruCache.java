package pl.cleankod.exchange.core.cache;

import java.util.Map;

public class InMemoryLruCache<K, V> extends InMemoryCache<K, V> {
    private final int maxSize;

    public InMemoryLruCache(int maxSize) {
        super(maxSize, 0.75f, true);
        this.maxSize = maxSize;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > maxSize;
    }
}
