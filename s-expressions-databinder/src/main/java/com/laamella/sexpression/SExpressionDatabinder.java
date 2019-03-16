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
        addTypeAdapter(Long.class,
                (value, generator) -> generator.onText(value.toString(), false),
                Long::parseLong);
        addTypeAdapter(long.class,
                (value, generator) -> generator.onText(value.toString(), false),
                Long::parseLong);
        addTypeAdapter(Float.class,
                (value, generator) -> generator.onText(value.toString(), false),
                Float::parseFloat);
        addTypeAdapter(float.class,
                (value, generator) -> generator.onText(value.toString(), false),
                Float::parseFloat);
        addTypeAdapter(Double.class,
                (value, generator) -> generator.onText(value.toString(), false),
                Double::parseDouble);
        addTypeAdapter(double.class,
                (value, generator) -> generator.onText(value.toString(), false),
                Double::parseDouble);
        addTypeAdapter(Boolean.class,
                (value, generator) -> generator.onText(value.toString(), false),
                Boolean::parseBoolean);
        addTypeAdapter(boolean.class,
                (value, generator) -> generator.onText(value.toString(), false),
                Boolean::parseBoolean);
        addTypeAdapter(Byte.class,
                (value, generator) -> generator.onText(value.toString(), false),
                Byte::parseByte);
        addTypeAdapter(byte.class,
                (value, generator) -> generator.onText(value.toString(), false),
                Byte::parseByte);
        addTypeAdapter(Character.class,
                (value, generator) -> generator.onText(value.toString(), false),
                c -> c.charAt(0));
        addTypeAdapter(char.class,
                (value, generator) -> generator.onText(value.toString(), false),
                c -> c.charAt(0));
        addTypeAdapter(Short.class,
                (value, generator) -> generator.onText(value.toString(), false),
                Short::parseShort);
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
