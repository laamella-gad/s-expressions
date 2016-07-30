package com.laamella.sexpression;

import com.laamella.sexpression.model.*;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;

public class SExpressionsParser implements CharSink, Closeable {
    private final SExpressionsStreamingParser parser = new SExpressionsStreamingParser(new SExpressionsStreamingParser.Callback() {
        private final Deque<AtomList> stack = new ArrayDeque<>();
        private Document document = null;

        @Override
        public void onText(String text) {
            if (stack.isEmpty()) {
                // TODO #26
                document.add(text);
            } else {
                // TODO #26
                stack.peek().add(text);
            }
        }

        @Override
        public void onWhitespace(String whitespace) {
            Whitespace ws = new Whitespace(whitespace);
            if (stack.isEmpty()) {
                document.add(ws);
            } else {
                stack.peek().add(ws);
            }
        }

        @Override
        public void onEndOfLine() {
            EndOfLine eol = new EndOfLine();
            if (stack.isEmpty()) {
                document.add(eol);
            } else {
                stack.peek().add(eol);
            }
        }

        @Override
        public void onListBegin() {
            stack.push(new AtomList());
        }

        @Override
        public void onListEnd() {
            if (stack.isEmpty()) {
                callback.onError(Error.TOO_MANY_CLOSING_PARENTHESES);
                return;
            }
            AtomList finishedList = stack.pop();
            if (stack.isEmpty()) {
                document.add(finishedList);
                callback.onExpression(finishedList);
            } else {
                stack.peek().add(finishedList);
            }
        }

        @Override
        public void onComment(String comment) {
            Comment c = new Comment(comment);
            if (stack.isEmpty()) {
                document.add(c);
            } else {
                stack.peek().add(c);
            }
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
            document = new Document();
            callback.onOpenStream();
        }

        @Override
        public void onCloseStream() {
            if (stack.size() != 0) {
                callback.onError(Error.UNCLOSED_PARENTHESES);
            }
            callback.onDocument(document);
            callback.onCloseStream();
        }
    });

    private final Callback callback;

    public enum Error {TOO_MANY_CLOSING_PARENTHESES, STREAM_ENDED_WHILE_IN_QUOTES, UNCLOSED_PARENTHESES}

    public SExpressionsParser(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {

        void onDocument(Document document);

        void onExpression(AtomList expression);

        void onError(Error error);

        void onOpenStream();

        void onCloseStream();


        class Adapter implements Callback {
            @Override
            public void onError(Error error) {
            }

            @Override
            public void onExpression(AtomList expression) {
            }

            @Override
            public void onOpenStream() {
            }

            @Override
            public void onCloseStream() {
            }

            @Override
            public void onDocument(Document document) {
            }
        }

        class DocumentGrabbingCallback extends Adapter {
            public Document document;

            @Override
            public void onDocument(Document document) {
                this.document = document;
            }

            @Override
            public void onError(Error error) {
                throw new RuntimeException(error.name());
            }
        }
    }

    @Override
    public void accept(char c) {
        parser.accept(c);
    }

    @Override
    public void close() throws IOException {
        parser.close();
    }
}
