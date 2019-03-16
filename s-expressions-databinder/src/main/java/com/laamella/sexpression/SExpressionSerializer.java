package com.laamella.sexpression;

import java.io.StringWriter;
import java.util.*;

public final class SExpressionSerializer {
    private final Stack<ThingToGenerate> thingsToGenerate = new Stack<>();
    private final Map<Class<?>, FromTypeAdapter> fromTypeAdapters;
    private final Set<Object> seen = new HashSet<>();

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
        if (fromTypeAdapter != null) {
            fromTypeAdapter.serialize(object, generator);
        } else {
            if (seen.contains(object)) {
                throw new IllegalStateException("Circular reference to " + object.toString());
            }
            seen.add(object);

            final ThingToGenerate thingToGenerate;
            if (object instanceof Iterable) {
                thingToGenerate = new IterableToGenerate(object);
            } else if (object instanceof Map) {
                thingToGenerate = new MapToGenerate(object);
            } else {
                thingToGenerate = new ObjectToGenerate(object);
            }
            thingToGenerate.begin(generator);
            thingsToGenerate.push(thingToGenerate);
        }
    }

    private void generate(SExpressionsStreamingGenerator generator) {
        while (!thingsToGenerate.empty()) {
            ThingToGenerate thingToGenerate = thingsToGenerate.peek();
            if (thingToGenerate.isDoneGenerating()) {
                ThingToGenerate thing = thingsToGenerate.pop();
                thing.end(generator);
            } else {
                thingToGenerate.step(generator);
            }
        }
    }

    private interface ThingToGenerate {
        boolean isDoneGenerating();

        void step(SExpressionsStreamingGenerator generator);

        void end(SExpressionsStreamingGenerator generator);

        void begin(SExpressionsStreamingGenerator generator);
    }

    private final class ObjectToGenerate implements ThingToGenerate {

        private final class Field {
            final String name;
            final Object value;

            Field(String name, Object value) {
                this.name = name;
                this.value = value;
            }
        }

        final Object object;
        final Stack<Field> fieldsToGenerate = new Stack<>();
        boolean fieldNeedsClosing = false;

        ObjectToGenerate(Object object) {
            this.object = object;
            java.lang.reflect.Field[] declaredFields = reflect(object);
            for (int i = declaredFields.length; i > 0; i--) {
                java.lang.reflect.Field field = declaredFields[i - 1];
                try {
                    Object value = field.get(object);
                    if (shouldBeSerialized(value)) {
                        fieldsToGenerate.push(new Field(field.getName(), value));
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        @Override
        public boolean isDoneGenerating() {
            return fieldsToGenerate.empty();
        }

        @Override
        public void step(SExpressionsStreamingGenerator generator) {
            Field field = fieldsToGenerate.pop();
            if (shouldBeSerialized(field.value)) {
                if (fieldNeedsClosing) {
                    generator.onClosingBrace();
                }
                fieldNeedsClosing = true;
                generator.onOpeningBrace();
                generator.onText(field.name, false);
                serializeValue(field.value, generator);
            }
        }

        @Override
        public void end(SExpressionsStreamingGenerator generator) {
            if (fieldNeedsClosing) {
                generator.onClosingBrace();
            }
            generator.onClosingBrace();
        }

        @Override
        public void begin(SExpressionsStreamingGenerator generator) {
            generator.onOpeningBrace();
        }
    }

    private final class MapToGenerate implements ThingToGenerate {
        private final Iterator iterator;
        private boolean entryNeedsClosing = false;

        public MapToGenerate(Object map) {
            iterator = ((Map) map).entrySet().iterator();
        }

        @Override
        public boolean isDoneGenerating() {
            return !iterator.hasNext();
        }

        @Override
        public void step(SExpressionsStreamingGenerator generator) {
            if (entryNeedsClosing) {
                generator.onClosingBrace();
            }
            entryNeedsClosing = true;

            Map.Entry<?, ?> entry = (Map.Entry<?, ?>) iterator.next();
            Object key = entry.getKey();
            generator.onOpeningBrace();
            generator.onText(key.toString(), false);
            Object value = entry.getValue();
            generator.onText("#" + value.getClass().getName(), false);
            serializeValue(value, generator);
        }

        @Override
        public void end(SExpressionsStreamingGenerator generator) {
            if (entryNeedsClosing) {
                generator.onClosingBrace();
            }
            generator.onClosingBrace();
        }

        @Override
        public void begin(SExpressionsStreamingGenerator generator) {
            generator.onOpeningBrace();
        }
    }

    private final class IterableToGenerate implements ThingToGenerate {
        private final Iterator iterator;

        public IterableToGenerate(Object iterable) {
            iterator = ((Iterable) iterable).iterator();
        }

        @Override
        public boolean isDoneGenerating() {
            return !iterator.hasNext();
        }

        @Override
        public void step(SExpressionsStreamingGenerator generator) {
            Object value = iterator.next();
            generator.onText("#" + value.getClass().getName(), false);
            serializeValue(value, generator);
        }

        @Override
        public void end(SExpressionsStreamingGenerator generator) {
            generator.onClosingBrace();
        }

        @Override
        public void begin(SExpressionsStreamingGenerator generator) {
            generator.onOpeningBrace();
        }
    }

}
