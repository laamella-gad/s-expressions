package com.laamella.sexpression.model;

public abstract class SExpression extends Node {
    public SExpression(Node parent) {
        super(parent);
    }

    @Override
    public final boolean isSExpression() {
        return true;
    }

    @Override
    public SExpression asSExpression() {
        return this;
    }
}
