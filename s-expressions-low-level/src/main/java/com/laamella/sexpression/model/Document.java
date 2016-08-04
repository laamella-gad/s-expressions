package com.laamella.sexpression.model;

import com.laamella.sexpression.CharSource;
import com.laamella.sexpression.SExpressionsParser;
import com.laamella.sexpression.SExpressionsParser.Callback.DocumentGrabbingCallback;
import com.laamella.sexpression.SExpressionsStreamingLexer;
import com.laamella.sexpression.SExpressionsStreamingParser;
import com.laamella.sexpression.visitor.Visitor;
import javaslang.collection.Vector;

import java.io.IOException;
import java.io.Reader;

public class Document extends AtomList {
    protected Document(Vector<Node> nodes) {
        super(null, nodes);
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public <A, R> R visit(Visitor<A, R> visitor, A arg) throws Exception {
        if (visitor.enter(this, arg) == Visitor.EnterDecision.ENTER) {
            R r = visitor.accept(this, arg);
            visitor.exit(this, r, arg);
            return r;
        }
        return null;
    }

    @Override
    public boolean isDocument() {
        return true;
    }

    @Override
    public Document asDocument() {
        return this;
    }

    public static Document from(Reader reader) throws IOException {
        DocumentGrabbingCallback callback = new DocumentGrabbingCallback();
        final SExpressionsStreamingLexer parser =
                new SExpressionsStreamingLexer(
                        new SExpressionsStreamingParser(
                                new SExpressionsParser(callback)));
        CharSource.push(reader, parser);
        return callback.document;
    }

    public static Document fromResource(String resourceName) throws IOException {
        DocumentGrabbingCallback callback = new DocumentGrabbingCallback();
        final SExpressionsStreamingLexer parser =
                new SExpressionsStreamingLexer(
                        new SExpressionsStreamingParser(
                                new SExpressionsParser(callback)));
        CharSource.pushResource(resourceName, CharSource.UTF8, parser);
        return callback.document;
    }

    public static Document fromString(String string) {
        DocumentGrabbingCallback callback = new DocumentGrabbingCallback();
        final SExpressionsStreamingLexer parser =
                new SExpressionsStreamingLexer(
                        new SExpressionsStreamingParser(
                                new SExpressionsParser(callback)));
        CharSource.pushString(string, parser);
        return callback.document;
    }
}
