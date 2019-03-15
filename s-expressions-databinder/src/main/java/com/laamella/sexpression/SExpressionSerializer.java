package com.laamella.sexpression;

import java.io.StringWriter;
import java.util.Map;
import java.util.Stack;

public final class SExpressionSerializer {
    private final Stack<ObjectToGenerate> objectsToGenerate = new Stack<>();
    private final Map<Class<?>, FromTypeAdapter> fromTypeAdapters;

    SExpressionSerializer(Map<Class<?>, FromTypeAdapter> fromTypeAdapters) {
        this.fromTypeAdapters = fromTypeAdapters;
    }

    public String serialize(Object o) {
        StringWriter stringWriter = new StringWriter();
        SExpressionsStreamingGenerator generator = new SExpressionsStreamingGenerator(stringWriter);
        serializeValue(o, generator);
        generate(generator);
        return stringWriter.toString();
    }

    private java.lang.reflect.Field[] reflect(Object o) {
        java.lang.reflect.Field[] fields = o.getClass().getDeclaredFields();
        for (java.lang.reflect.Field field : fields) {
            field.setAccessible(true);
        }
        return fields;
    }

    private void pushObject(Object o, SExpressionsStreamingGenerator generator) {
        if (o == null) {
            throw new IllegalStateException("Don't want nulls here.");
        }
        ObjectToGenerate objectToGenerate = new ObjectToGenerate(o);
        java.lang.reflect.Field[] declaredFields = reflect(o);
        for (int i = declaredFields.length; i > 0; i--) {
            java.lang.reflect.Field field = declaredFields[i - 1];
            try {
                Object value = field.get(o);
                if (shouldBeSerialized(value)) {
                    objectToGenerate.fieldsToGenerate.push(new Field(field.getName(), value));
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        objectsToGenerate.push(objectToGenerate);
        generator.onOpeningBrace();
    }

    private boolean shouldBeSerialized(Object value) {
        if (value == null) {
            return false;
        }
        return true;
    }

    private void serializeValue(Object object, SExpressionsStreamingGenerator generator) {
        if (object == null) {
            return;
        }
        FromTypeAdapter fromTypeAdapter = fromTypeAdapters.get(object.getClass());
        if (fromTypeAdapter == null) {
            pushObject(object, generator);
        } else {
            fromTypeAdapter.serialize(object, generator);
        }
    }

    private void generate(SExpressionsStreamingGenerator generator) {
        while (!objectsToGenerate.empty()) {
            ObjectToGenerate objectToGenerate = objectsToGenerate.peek();
            if (objectToGenerate.fieldsToGenerate.empty()) {
                objectsToGenerate.pop();
                generator.onClosingBrace();
            } else {
                Field field = objectToGenerate.fieldsToGenerate.pop();
                if (shouldBeSerialized(field.value)) {
                    generator.onOpeningBrace();
                    generator.onText(field.name, false);
                    serializeValue(field.value, generator);
                    generator.onClosingBrace();
                }
            }
        }
    }

    static class ObjectToGenerate {
        final Object object;
        final Stack<Field> fieldsToGenerate = new Stack<>();

        ObjectToGenerate(Object object) {
            this.object = object;
        }
    }

    static class Field {
        final String name;
        final Object value;

        Field(String name, Object value) {
            this.name = name;
            this.value = value;
        }
    }
}
