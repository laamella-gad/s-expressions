package com.laamella.sexpression;

import java.util.LinkedList;
import java.util.List;

public class AtomList implements SExpressionNode {
    public final List<SExpressionNode> values = new LinkedList<>();

    public void add(SExpressionNode sExpressionNode) {
        values.add(sExpressionNode);
    }

    public void add(CharSequence atom) {
        values.add(new Atom(atom));
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        try {
            SExpressionPrinterVisitor.TO_STRING.accept(this, output);
        } catch (Exception e) {
            return e.getMessage();
        }
        return output.toString();
    }

    @Override
    public <A, R> R visit(Visitor<A,R > visitor, A arg) throws Exception {
        return visitor.accept(this, arg);
    }
}
