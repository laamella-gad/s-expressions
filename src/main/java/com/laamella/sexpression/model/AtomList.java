package com.laamella.sexpression.model;

import com.laamella.sexpression.visitor.PrinterVisitor;
import com.laamella.sexpression.visitor.Visitor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class AtomList extends SExpression {
    public final List<SExpression> values = new ArrayList<>();
    public final List<Node> nodes = new ArrayList<>();

    public void add(SExpression sExpression) {
        values.add(sExpression);
    }

    public void add(CharSequence atom) {
        values.add(new Atom(atom));
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
        action.accept(this);
        return new Otherwise(false);
    }

    @Override
    public Otherwise whenAtom(Consumer<Atom> action) {
        return new Otherwise(true);
    }

    @Override
    public boolean isAtom() {
        return false;
    }

    @Override
    public boolean isList() {
        return true;
    }

    @Override
    public Atom asAtom() {
        throw new IllegalStateException();
    }

    @Override
    public AtomList asList() {
        return this;
    }

    public boolean isAllAtoms() {
        for (SExpression e : values) {
            if (e.isList()) {
                return false;
            }
        }
        return true;
    }

    public boolean isEmpty() {
        return values.isEmpty();
    }
}
