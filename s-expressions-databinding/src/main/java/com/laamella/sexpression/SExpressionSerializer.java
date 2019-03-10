package com.laamella.sexpression;

import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

interface CustomSerializer {
    void serialize(Object value, SExpressionsStreamingGenerator generator);
}

public final class SExpressionSerializer {
    private final Stack<ObjectToGenerate> objectsToGenerate = new Stack<>();
    private final Map<Class<?>, CustomSerializer> serializers = new HashMap<>();

    public SExpressionSerializer() {
        serializers.put(Integer.class, (value, generator) -> generator.onText(value.toString(), false));
        serializers.put(String.class, (value, generator) -> generator.onText(value.toString(), false));
    }

    public String serialize(Object o) {
        StringWriter stringWriter = new StringWriter();
        SExpressionsStreamingGenerator generator = new SExpressionsStreamingGenerator(stringWriter);
        analyzeValue(o, generator);
        generate(generator);
        return stringWriter.toString();
    }

    private void pushObject(Object o, SExpressionsStreamingGenerator generator) {
        if (o == null) {
            throw new IllegalStateException("Don't want nulls here.");
        }
        ObjectToGenerate objectToGenerate = new ObjectToGenerate(o);
        java.lang.reflect.Field[] declaredFields = o.getClass().getDeclaredFields();
        for (int i = declaredFields.length; i > 0; i--) {
            java.lang.reflect.Field field = declaredFields[i-1];
            try {
                field.setAccessible(true);
                objectToGenerate.fieldsToGenerate.push(new Field(field.getName(), field.get(o)));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        objectsToGenerate.push(objectToGenerate);
        generator.onOpeningBrace();
    }

    private void analyzeValue(Object object, SExpressionsStreamingGenerator generator) {
        if (object == null) {
            generator.onText("#null", false);
        } else {
            CustomSerializer serializer = serializers.get(object.getClass());
            if (serializer == null) {
                pushObject(object, generator);
            } else {
                serializer.serialize(object, generator);
            }
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
                generator.onOpeningBrace();
                generator.onText(field.name, false);
                analyzeValue(field.value, generator);
                generator.onClosingBrace();
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
