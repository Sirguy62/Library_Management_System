package organise.creditrisk.infrastructure.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CacheClient {

    private static final Map<String, Object> cache =
            new ConcurrentHashMap<>();

    public void put(String key, Object value) {
        cache.put(key, value);
    }

    public Object get(String key) {
        return cache.get(key);
    }

    public void evict(String key) {
        cache.remove(key);
    }
}