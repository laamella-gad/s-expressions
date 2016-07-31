package com.laamella.sexpression.model;

public abstract class SExpression extends Node {
    @Override
    public final boolean isSExpression() {
        return true;
    }

    @Override
    public SExpression asSExpression() {
        return this;
    }
}
