package com.laamella.sexpression.model;

import com.laamella.sexpression.visitor.PrinterVisitor;
import com.laamella.sexpression.visitor.Visitor;

import java.util.LinkedList;
import java.util.List;

public class AtomList implements SExpression {
    public final List<SExpression> values = new LinkedList<>();

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
}
