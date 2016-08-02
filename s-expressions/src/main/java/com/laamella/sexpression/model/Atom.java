package com.laamella.sexpression.model;

import com.laamella.sexpression.CharSource;
import com.laamella.sexpression.codec.AtomCodec;
import com.laamella.sexpression.visitor.Visitor;

public class Atom extends SExpression {
    private final byte[] data;
    private final AtomCodec codec;

    public Atom(SExpression parent, byte[] data, AtomCodec codec) {
        super(parent);
        this.data = data;
        this.codec = codec;
    }

    /**
     * @return the atom as it would appear in an s-expression.
     */
    public String encoded() {
        return codec.encode(data).get();
    }

    /**
     * @return the raw bytes represented by this atom.
     */
    public byte[] raw() {
        return data;
    }

    /**
     * @return the actual text represented by this atom.
     * If the atom contains binary data this is undefined, use raw() instead.
     */
    public String value() {
        return new String(data, CharSource.UTF8);
    }

    @Override
    public <A, R> R visit(Visitor<A, R> visitor, A arg) throws Exception {
        return visitor.accept(this, arg);
    }

    @Override
    public boolean isAtom() {
        return true;
    }

    @Override
    public Atom asAtom() {
        return this;
    }
}
