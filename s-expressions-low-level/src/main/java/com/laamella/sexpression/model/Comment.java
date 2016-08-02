package com.laamella.sexpression.model;

import com.laamella.sexpression.visitor.Visitor;

public class Comment extends Meta {
    public String text;

    public Comment(Node parent, String text) {
        super(parent);
        this.text = text;
    }

    @Override
    public <A, R> R visit(Visitor<A, R> visitor, A arg) throws Exception {
        return visitor.accept(this, arg);
    }

    @Override
    public boolean isComment() {
        return true;
    }

    @Override
    public Comment asComment() {
        return this;
    }

}
