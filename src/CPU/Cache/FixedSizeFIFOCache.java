package CPU.Cache;

import java.util.LinkedHashMap;
import java.util.Map;
/**
 * This is the FixedSizeFIFOCache Map class. It will provide FIFO functionality for cacheBlocks in Cache.
 *
 */
public class FixedSizeFIFOCache<K, V> extends LinkedHashMap<K, V> {
    private final int capacity;

    public FixedSizeFIFOCache(int capacity) {
        super(capacity, 0.75f, false); // Initialize with accessOrder set to false for FIFO
        this.capacity = capacity;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > capacity;
    }
}
