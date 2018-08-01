package com.laamella.sexpression.model;

import com.laamella.sexpression.codec.AtomCodec;
import com.laamella.sexpression.codec.CombinedCodec;
import io.vavr.collection.Vector;

import static com.laamella.sexpression.codec.AtomCodec.*;

public final class Factory {
    private static final AtomCodec DEFAULT_STRING_CODEC = new CombinedCodec(SIMPLE, DOUBLE_QUOTE, BASE64);

    private Factory() {
    }

    public static Atom atom(byte[] data, AtomCodec codec) {
        return new Atom(null, data, codec);
    }

    public static Atom atom(CharSequence value, AtomCodec codec) {
        return new Atom(null, codec.decode(value).get(), codec);
    }

    public static Atom atom(CharSequence value) {
        return new Atom(null, DEFAULT_STRING_CODEC.decode(value).get(), DEFAULT_STRING_CODEC);
    }

    public static AtomList list(Node... nodes) {
        return new AtomList(null, Vector.of(nodes));
    }

    public static Comment comment(String text) {
        return new Comment(null, text);
    }

    public static LineTerminator nl() {
        return new LineTerminator(null);
    }

    public static Whitespace space() {
        return new Whitespace(null, " ");
    }

    public static Document document(Node... nodes) {
        return new Document(Vector.of(nodes));
    }

    public static Whitespace whitespace(String whitespace) {
        return new Whitespace(null, whitespace);
    }
}
