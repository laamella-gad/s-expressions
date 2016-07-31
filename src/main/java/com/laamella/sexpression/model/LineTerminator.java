package com.laamella.sexpression.model;

import com.laamella.sexpression.visitor.Visitor;

public class LineTerminator extends Meta {
    public LineTerminator(Document document) {
        super(document);
    }

    @Override
    public <A, R> R visit(Visitor<A, R> visitor, A arg) throws Exception {
        return visitor.accept(this, arg);
    }

    @Override
    public boolean isLineTerminator() {
        return true;
    }

    @Override
    public LineTerminator asLineTerminator() {
        return this;
    }
}
