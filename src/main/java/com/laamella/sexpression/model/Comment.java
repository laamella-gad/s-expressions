package com.laamella.sexpression.model;

import com.laamella.sexpression.visitor.Visitor;

import java.util.function.Consumer;

public class Comment implements Node {
    public final String text;

    public Comment(String text) {
        this.text = text;
    }

    @Override
    public <A, R> R visit(Visitor<A, R> visitor, A arg) throws Exception {
        return visitor.accept(this, arg);
    }

    @Override
    public Otherwise whenList(Consumer<AtomList> action) {
        return new Otherwise(true);
    }

    @Override
    public Otherwise whenAtom(Consumer<Atom> action) {
        return new Otherwise(true);
    }

    @Override
    public Otherwise whenComment(Consumer<Comment> action) {
        action.accept(this);
        return new Otherwise(false);
    }

    @Override
    public boolean isAtom() {
        return false;
    }

    @Override
    public boolean isList() {
        return false;
    }

    @Override
    public boolean isComment() {
        return true;
    }

    @Override
    public Atom toAtom() {
        throw new IllegalStateException();
    }

    @Override
    public AtomList toList() {
        throw new IllegalStateException();
    }

    @Override
    public Comment toComment() {
        return this;
    }

}
