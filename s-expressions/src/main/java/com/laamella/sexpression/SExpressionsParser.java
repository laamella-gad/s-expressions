package com.laamella.sexpression;

import com.laamella.sexpression.codec.AtomCodec;
import com.laamella.sexpression.model.Atom;
import com.laamella.sexpression.model.AtomList;
import com.laamella.sexpression.model.Document;
import com.laamella.sexpression.model.SExpression;
import io.vavr.collection.Vector;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Optional;

import static com.laamella.sexpression.codec.AtomCodec.*;
import static com.laamella.sexpression.model.Factory.document;

public class SExpressionsParser implements SExpressionsStreamingParser.Callback {
    private final Deque<AtomList> stack = new ArrayDeque<>();
    private Vector<String> comments = Vector.empty();
    private Document document = null;
    private final AtomCodec[] decodeList = new AtomCodec[]{BASE64, DOUBLE_QUOTE, SIMPLE};

    @Override
    public void onText(String text) {
        for (AtomCodec codec : decodeList) {
            Optional<byte[]> raw = codec.decode(text);
            if (raw.isPresent()) {
                addToTopList(new Atom(null, raw.get(), codec, comments));
                comments = Vector.empty();
                return;
            }
        }
    }

    private void addToTopList(SExpression node) {
        getTopList().add(node);
    }

    private AtomList getTopList() {
        if (stack.isEmpty()) {
            return document;
        } else {
            return stack.peek();
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
        stack.push(new AtomList(null, Vector.empty(), comments));
        comments = Vector.empty();
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
        comments = comments.append(comment);
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
        document = document();
        callback.onOpenStream();
    }

    @Override
    public void onCloseStream() {
        if (stack.size() != 0) {
            callback.onError(Error.UNCLOSED_PARENTHESES);
        }
        document = new Document(document.asVector(), comments);
        callback.onDocument(document);
        callback.onCloseStream();
    }

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
}
