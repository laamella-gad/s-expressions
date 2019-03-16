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

public final class SExpressionDeserializer {
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
        EXPECT_FIELD_START_OR_OBJECT_END
    }

    private final class SExpressionStreamingDeserializer<T> implements SExpressionsStreamingParser.Callback {

        private final Objenesis objenesis = new ObjenesisStd();

        private final Stack<Object> stack = new Stack<>();
        private Stack<ValueLocator> valueLocators = new Stack<>();
        T root = null;

        private StreamState streamState = EXPECT_VALUE_OR_OBJECT;

        public SExpressionStreamingDeserializer(Class<T> rootClass) {
            valueLocators.push(new ValueLocator() {
                @Override
                public void setValue(Object value) {
                    root = (T) value;
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
                case EXPECT_VALUE_END:
                    throw new IllegalStateException("Expected ), got " + text);
                case EXPECT_FIELD_START_OR_OBJECT_END:
                    throw new IllegalStateException("Expected ( or ), got " + text);
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
                    Object o = instantiator.newInstance();
                    stack.push(o);
                    streamState = EXPECT_FIELD_START_OR_OBJECT_END;
                    break;
                case EXPECT_FIELD_START_OR_OBJECT_END:
                    streamState = EXPECT_FIELD_NAME;
                    break;
                case EXPECT_VALUE_END:
                    throw new IllegalStateException("Expected ), got (");
                case EXPECT_FIELD_NAME:
                    throw new IllegalStateException("Expected field name, got (");
            }
        }

        @Override
        public void onListEnd() {
            switch (streamState) {
                case EXPECT_VALUE_END:
                    streamState = EXPECT_FIELD_START_OR_OBJECT_END;
                    break;
                case EXPECT_FIELD_START_OR_OBJECT_END:
                    Object value = stack.pop();
                    valueLocators.pop().setValue(value);
                    break;
                case EXPECT_VALUE_OR_OBJECT:
                    throw new IllegalStateException("Expected value or object start, got )");
                case EXPECT_FIELD_NAME:
                    throw new IllegalStateException("Expected field name, got )");
                default:
                    throw new IllegalStateException();
            }
        }

        @Override
        public void onCloseStream() {
            if (!stack.isEmpty()) {
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