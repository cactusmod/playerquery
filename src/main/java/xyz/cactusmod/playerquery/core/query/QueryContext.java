package xyz.cactusmod.playerquery.core.query;

import java.util.HashMap;
import java.util.Map;

public class QueryContext {

    private final Map<String, Object> values = new HashMap<>();

    public <T> void put(String name, T value) {
        values.put(name, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String name) {
        return (T) values.get(name);
    }

}