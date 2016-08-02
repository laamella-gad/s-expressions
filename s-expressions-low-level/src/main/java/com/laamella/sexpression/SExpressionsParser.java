package com.laamella.sexpression;

import com.laamella.sexpression.codec.AtomCodec;
import com.laamella.sexpression.model.AtomList;
import com.laamella.sexpression.model.Document;
import com.laamella.sexpression.model.Factory;
import com.laamella.sexpression.model.Node;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Optional;

public class SExpressionsParser implements CharSink, Closeable {
    private final SExpressionsStreamingParser parser = new SExpressionsStreamingParser(new SExpressionsStreamingParser.Callback() {
        private final Deque<AtomList> stack = new ArrayDeque<>();
        private Document document = null;
        private final AtomCodec[] decodeList = new AtomCodec[]{AtomCodec.BASE64, AtomCodec.DOUBLE_QUOTE, AtomCodec.SIMPLE};

        @Override
        public void onText(String text) {
            for (AtomCodec codec : decodeList) {
                Optional<byte[]> raw = codec.decode(text);
                if (raw.isPresent()) {
                    addToTopList(Factory.atom(raw.get(), codec));
                    return;
                }
            }
        }

        private void addToTopList(Node node) {
            if (stack.isEmpty()) {
                document.add(node);
            } else {
                stack.peek().add(node);
            }

        }

        @Override
        public void onWhitespace(String whitespace) {
            addToTopList(Factory.whitespace(whitespace));
        }

        @Override
        public void onEndOfLine() {
            addToTopList(Factory.nl());
        }

        @Override
        public void onListBegin() {
            stack.push(Factory.list());
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
            addToTopList(Factory.comment(comment));
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
            document = Factory.document();
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
