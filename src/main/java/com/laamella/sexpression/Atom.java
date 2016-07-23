package com.laamella.sexpression;

public class Atom extends SExpressionNode {
    public final String value;

    public Atom(CharSequence value) {
        this.value = value.toString();
    }

    @Override
    public String toString() {
        return value;
    }
}
