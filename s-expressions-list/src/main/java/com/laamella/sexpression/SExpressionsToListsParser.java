package com.laamella.sexpression;

import com.laamella.sexpression.codec.AtomCodec;

import java.util.*;

import static com.laamella.sexpression.codec.AtomCodec.*;

public class SExpressionsToListsParser implements SExpressionsStreamingParser.Callback {
    private final Deque<List<Object>> stack = new ArrayDeque<>();
    private final AtomCodec[] decodeList = new AtomCodec[]{BASE64, DOUBLE_QUOTE, SIMPLE};

    @Override
    public void onText(String text) {
        List<Object> top = stack.peekFirst();
        if (top == null) {
            callback.onError(Error.TOO_MANY_CLOSING_PARENTHESES);
        } else {
            for (AtomCodec codec : decodeList) {
                Optional<byte[]> raw = codec.decode(text);
                if (raw.isPresent()) {
                    top.add(new String(raw.get(), CharSource.UTF8));
                    return;
                }
            }
        }
    }

    @Override
    public void onWhitespace(String whitespace) {
    }

    @Override
    public void onEndOfLine() {
    }

    @Override
    public void onListBegin() {
        List<Object> top = stack.peekFirst();
        if (top == null) {
            callback.onError(Error.TOO_MANY_CLOSING_PARENTHESES);
        } else {
            List<Object> newList = new ArrayList<>();
            top.add(newList);
            stack.push(newList);
        }
    }

    @Override
    public void onListEnd() {
        if (stack.size() == 1) {
            callback.onError(Error.TOO_MANY_CLOSING_PARENTHESES);
        } else {
            List<Object> finishedList = stack.pop();
            if (stack.size() == 1) {
                callback.onExpression(finishedList);
            }
        }
    }

    @Override
    public void onComment(String comment) {
    }

    @Override
    public void onError(SExpressionsStreamingParser.Error error) {
        switch (error) {
            case STREAM_ENDED_WHILE_IN_QUOTES:
                callback.onError(Error.STREAM_ENDED_WHILE_IN_QUOTES);
                break;
            default:
                throw new AssertionError("Problem translating unknown error that should have been handled here.");
        }
    }

    @Override
    public void onOpenStream() {
        stack.clear();
        stack.push(new ArrayList<>());
        callback.onOpenStream();
    }

    @Override
    public void onCloseStream() {
        if (stack.size() != 1) {
            callback.onError(Error.UNCLOSED_PARENTHESES);
        }
        if (!stack.isEmpty()) {
            callback.onResult(stack.pop());
        }

        callback.onCloseStream();
    }

    private final Callback callback;

    public enum Error {TOO_MANY_CLOSING_PARENTHESES, STREAM_ENDED_WHILE_IN_QUOTES, UNCLOSED_PARENTHESES}

    public SExpressionsToListsParser(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {

        void onResult(List<Object> document);

        void onError(Error error);

        void onOpenStream();

        void onCloseStream();

        void onExpression(List<Object> finishedList);


        class Adapter implements Callback {
            @Override
            public void onError(Error error) {
            }

            @Override
            public void onOpenStream() {
            }

            @Override
            public void onCloseStream() {
            }

            @Override
            public void onExpression(List<Object> finishedExpression) {

            }

            @Override
            public void onResult(List<Object> document) {
            }
        }

        class ResultGrabbingCallback extends Adapter {
            public List<Object> result;

            @Override
            public void onResult(List<Object> result) {
                this.result = result;
            }

            @Override
            public void onError(Error error) {
                throw new RuntimeException(error.name());
            }
        }
    }
}
