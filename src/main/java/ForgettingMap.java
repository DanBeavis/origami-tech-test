import java.util.concurrent.ConcurrentHashMap;

public class ForgettingMap {

    private final ConcurrentHashMap<Integer, String> map = new ConcurrentHashMap<>();

    private final int maxSize;

    public ForgettingMap(final int maxSize) {
        this.maxSize = maxSize;
    }

    public String add(final Integer integer, final String string) {
        return map.put(integer, string);
    }

    public String find(final Integer integer) {
        return map.get(integer);
    }

    public int size() {
        return map.size();
    }

    public int getMaxSize() {
        return maxSize;
    }
}