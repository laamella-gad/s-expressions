package com.laamella.sexpression.model;

import com.laamella.sexpression.visitor.PrinterVisitor;
import com.laamella.sexpression.visitor.Visitor;

import java.util.function.Consumer;

public class Atom implements SExpression {
    public final String value;

    public Atom(CharSequence value) {
        this.value = value.toString();
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        try {
            PrinterVisitor.TO_STRING.accept(this, output);
        } catch (Exception e) {
            return e.getMessage();
        }
        return output.toString();
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
        action.accept(this);
        return new Otherwise(false);
    }

    @Override
    public Otherwise whenComment(Consumer<Comment> action) {
        return new Otherwise(true);
    }

    @Override
    public boolean isAtom() {
        return true;
    }

    @Override
    public boolean isList() {
        return false;
    }

    @Override
    public boolean isComment() {
        return false;
    }

    @Override
    public Atom toAtom() {
        return this;
    }

    @Override
    public AtomList toList() {
        throw new IllegalStateException();
    }

    @Override
    public Comment toComment() {
        throw new IllegalStateException();
    }
}
