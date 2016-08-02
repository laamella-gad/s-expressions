package com.laamella.sexpression.model;

import com.laamella.sexpression.codec.AtomCodec;
import com.laamella.sexpression.codec.CombinedCodec;
import javaslang.collection.Vector;

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

    public static AtomList list(SExpression... nodes) {
        return new AtomList(null, Vector.of(nodes));
    }

    public static Comment comment(String text) {
        return new Comment(text);
    }

    public static Document document(SExpression... nodes) {
        return new Document(Vector.of(nodes));
    }
}
