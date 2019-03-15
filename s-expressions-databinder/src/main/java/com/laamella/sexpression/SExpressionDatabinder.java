package com.laamella.sexpression;

import java.util.HashMap;
import java.util.Map;

public class SExpressionDatabinder {
    private final Map<Class<?>, FromTypeAdapter> fromTypeAdapters = new HashMap<>();
    private final Map<Class<?>, ToTypeAdapter> toTypeAdapters = new HashMap<>();

    public SExpressionDatabinder() {
        addTypeAdapter(Integer.class,
                (value, generator) -> generator.onText(value.toString(), false),
                Integer::parseInt);
        addTypeAdapter(int.class,
                (value, generator) -> generator.onText(value.toString(), false),
                Integer::parseInt);
        addTypeAdapter(String.class,
                (value, generator) -> generator.onText(value, false),
                value -> value);

    }

    public <T> SExpressionDatabinder addTypeAdapter(Class<T> type, FromTypeAdapter<T> fromTypeAdapter, ToTypeAdapter<T> toTypeAdapter) {
        fromTypeAdapters.put(type, fromTypeAdapter);
        toTypeAdapters.put(type, toTypeAdapter);
        return this;
    }
    
    public SExpressionSerializer createSerializer() {
        return new SExpressionSerializer(fromTypeAdapters);
    }

    public SExpressionDeserializer createDeserializer() {
        return new SExpressionDeserializer(toTypeAdapters);
    }
}
