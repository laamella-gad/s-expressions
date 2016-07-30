package com.laamella.sexpression.model;

import com.laamella.sexpression.CharSource;
import com.laamella.sexpression.SExpressionsParser;
import com.laamella.sexpression.SExpressionsParser.Callback.DocumentGrabbingCallback;
import com.laamella.sexpression.visitor.Visitor;
import javaslang.collection.Vector;

import java.io.IOException;
import java.io.Reader;

public class Document extends AtomList {
    public Document(Node... nodes) {
        super(nodes);
    }

    public Document(Vector<Node> nodes) {
        super(nodes);
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public <A, R> R visit(Visitor<A, R> visitor, A arg) throws Exception {
        return visitor.accept(this, arg);
    }

    public static Document from(Reader reader) throws IOException {
        DocumentGrabbingCallback callback = new DocumentGrabbingCallback();
        final SExpressionsParser parser = new SExpressionsParser(callback);
        CharSource.push(reader, parser);
        return callback.document;
    }

    public static Document fromResource(String resourceName) throws IOException {
        DocumentGrabbingCallback callback = new DocumentGrabbingCallback();
        final SExpressionsParser parser = new SExpressionsParser(callback);
        CharSource.pushResource(resourceName, CharSource.UTF8, parser);
        return callback.document;
    }

    public static Document fromString(String string) {
        DocumentGrabbingCallback callback = new DocumentGrabbingCallback();
        final SExpressionsParser parser = new SExpressionsParser(callback);
        CharSource.pushString(string, parser);
        return callback.document;
    }
}
