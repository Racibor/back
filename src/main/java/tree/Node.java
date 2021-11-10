package tree;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Node {
    private HashMap<String, Object> map;

    public Node() {
        map = new HashMap<>();
    }

    public Set<Map.Entry<String, Object>> getNodeObjects() {
        return map.entrySet();
    }

    public void put(String key, Object value) {
        map.put(key, value);
    }

    public void remove(String key) {
        map.remove(key);
    }

    public Object get(String key) {
        return map.get(key);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return map.equals(node.map);
    }

    @Override
    public int hashCode() {
        return Objects.hash(map);
    }
}