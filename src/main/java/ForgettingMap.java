import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ForgettingMap<K, V> {

    private final Map<K, V>                 map         = new HashMap<>();
    private final LinkedHashMap<K, Integer> trackingMap = new LinkedHashMap<>();

    private final int maxSize;

    public ForgettingMap(final int maxSize) {
        this.maxSize = maxSize;
    }

    public synchronized V add(final K key, final V val) {
        if (map.size() == maxSize) {
            final K leastUsedKey = findLeastUsedKey();
            map.remove(leastUsedKey);
            trackingMap.remove(leastUsedKey);
        }

        trackingMap.put(key, 0);
        return map.put(key, val);
    }

    public synchronized V find(final K key) {
        if (trackingMap.get(key) != null) {
            trackingMap.put(key, trackingMap.get(key) + 1);
        }

        return map.get(key);
    }

    public int size() {
        return map.size();
    }

    public int getMaxSize() {
        return maxSize;
    }

    private K findLeastUsedKey() {
        return trackingMap.entrySet().stream().min(Map.Entry.comparingByValue()).get().getKey();
    }

    int getEntryFrequency(final K key) {
        return trackingMap.get(key);
    }
}