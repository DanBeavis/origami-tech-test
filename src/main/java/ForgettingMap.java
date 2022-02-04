import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ForgettingMap<K, V> {

    private final ConcurrentHashMap<K, V> map  = new ConcurrentHashMap<>();
    private final ArrayList<K>            list = new ArrayList<>();

    private final int maxSize;

    public ForgettingMap(final int maxSize) {
        this.maxSize = maxSize;
    }

    public V add(final K key, final V val) {
        list.add(key);

        if (map.size() == maxSize) {
            map.remove(list.get(0));
            list.remove(0);
        }

        return map.put(key, val);
    }

    public V find(final K integer) {
        return map.get(integer);
    }

    public int size() {
        return map.size();
    }

    public int getMaxSize() {
        return maxSize;
    }
}