import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ForgettingMap<K, V> {

    private final Map<K, V> map  = new HashMap<>();
    private final List<K>   list = new ArrayList<>();

    private final int maxSize;

    public ForgettingMap(final int maxSize) {
        this.maxSize = maxSize;
    }

    public synchronized V add(final K key, final V val) {
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