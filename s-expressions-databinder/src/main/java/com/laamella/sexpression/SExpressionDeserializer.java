package com.laamella.sexpression;

import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;
import org.objenesis.instantiator.ObjectInstantiator;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Stack;

import static com.laamella.sexpression.SExpressionDeserializer.StreamState.*;

public class SExpressionDeserializer {
    private final Map<Class<?>, ToTypeAdapter> toTypeAdapters;

    SExpressionDeserializer(Map<Class<?>, ToTypeAdapter> toTypeAdapters) {
        this.toTypeAdapters = toTypeAdapters;
    }

    public <T> T deserialize(String expr, Class<T> rootClass) throws IOException {
        return deserialize(new StringReader(expr), rootClass);
    }

    public <T> T deserialize(Reader reader, Class<T> rootClass) throws IOException {
        SExpressionStreamingDeserializer<T> callback = new SExpressionStreamingDeserializer<T>(rootClass);
        SExpressionsStreamingLexer stream = new SExpressionsStreamingLexer(new SExpressionsStreamingParser(callback));
        stream.push(reader);
        return callback.root;
    }

    interface ValueLocator {
        void setValue(Object value);

        Class<?> getValueType();
    }

    class FieldValueLocator implements ValueLocator {
        private final Object o;
        private final Field f;

        FieldValueLocator(Object o, Field f) {
            this.o = o;
            this.f = f;
        }

        @Override
        public void setValue(Object value) {
            try {
                f.set(o, value);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public Class<?> getValueType() {
            return f.getType();
        }
    }

    enum StreamState {
        EXPECT_VALUE_OR_OBJECT,
        EXPECT_FIELD_NAME,
        EXPECT_VALUE_END,
        EXPECT_FIELD_OR_OBJECT_END
    }

    private class SExpressionStreamingDeserializer<T> implements SExpressionsStreamingParser.Callback {

        private final Objenesis objenesis = new ObjenesisStd();

        private final Stack<Object> stack = new Stack<>();
        private Stack<ValueLocator> valueLocators = new Stack<>();
        T root;

        private StreamState streamState = EXPECT_VALUE_OR_OBJECT;

        public SExpressionStreamingDeserializer(Class<T> rootClass) {
            valueLocators.push(new ValueLocator() {
                @Override
                public void setValue(Object value) {
                    root = rootClass.cast(value);
                }

                @Override
                public Class<?> getValueType() {
                    return rootClass;
                }
            });
        }

        @Override
        public void onText(String text) {
            switch (streamState) {
                case EXPECT_VALUE_OR_OBJECT:
                    System.out.println("Found value " + text);
                    ValueLocator valueLocator = valueLocators.pop();
                    ToTypeAdapter toTypeAdapter = toTypeAdapters.get(valueLocator.getValueType());
                    if (toTypeAdapter == null) {
                        throw new IllegalStateException("No toTypeAdapter for type " + valueLocator.getValueType().getName());
                    }
                    Object value = toTypeAdapter.deserialize(text);
                    valueLocator.setValue(value);
                    streamState = EXPECT_VALUE_END;
                    break;
                case EXPECT_FIELD_NAME:
                    System.out.println("Found field " + text);
                    Object o = stack.peek();
                    try {
                        Field field = o.getClass().getField(text);
                        field.setAccessible(true);
                        valueLocators.push(new FieldValueLocator(o, field));
                    } catch (NoSuchFieldException e) {
                        throw new RuntimeException(e);
                    }
                    streamState = EXPECT_VALUE_OR_OBJECT;
                    break;
                default:
                    throw new IllegalStateException();
            }
        }

        @Override
        public void onListBegin() {
            switch (streamState) {
                case EXPECT_VALUE_OR_OBJECT:
                    ValueLocator valueLocator = valueLocators.peek();
                    ObjectInstantiator<?> instantiator = objenesis.getInstantiatorOf(valueLocator.getValueType());
                    System.out.println("( object " + valueLocator.getValueType().getName());
                    Object o = instantiator.newInstance();
                    stack.push(o);
                    streamState = EXPECT_FIELD_OR_OBJECT_END;
                    break;
                case EXPECT_FIELD_OR_OBJECT_END:
                    System.out.println("( field");
                    streamState = EXPECT_FIELD_NAME;
                    break;
                default:
                    throw new IllegalStateException();
            }
        }

        @Override
        public void onListEnd() {
            switch (streamState) {
                case EXPECT_VALUE_END:
                    System.out.println("value )");
                    streamState = EXPECT_FIELD_OR_OBJECT_END;
                    break;
                case EXPECT_FIELD_OR_OBJECT_END:
                    System.out.println("object )");
                    Object value = stack.pop();
                    valueLocators.pop().setValue(value);
                    break;
                default:
                    throw new IllegalStateException();
            }
        }

        @Override
        public void onCloseStream() {
            if (!stack.isEmpty()) {
                throw new IllegalStateException();
            }
            if (!valueLocators.isEmpty()) {
                throw new IllegalStateException();
            }
        }

        @Override
        public void onComment(String comment) {

        }

        @Override
        public void onError(SExpressionsStreamingParser.Error error) {

        }

        @Override
        public void onOpenStream() {

        }

        @Override
        public void onWhitespace(String whitespace) {

        }

        @Override
        public void onEndOfLine() {

        }
    }
}