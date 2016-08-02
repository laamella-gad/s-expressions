package com.laamella.sexpression.model;

import com.laamella.sexpression.visitor.PrinterVisitor;
import com.laamella.sexpression.visitor.Visitor;
import javaslang.collection.Vector;

public abstract class SExpression {
    public Vector<String> comments = Vector.empty();

    private SExpression parent;

    public SExpression(SExpression parent, Vector<String> comments) {
        this.comments = comments;
        setParent(parent);
    }

    public abstract <A, R> R visit(Visitor<A, R> visitor, A arg) throws Exception;

    public boolean isAtom() {
        return false;
    }

    public boolean isList() {
        return false;
    }

    public boolean isDocument() {
        return false;
    }
    
    public Atom asAtom() {
        throw new IllegalStateException();
    }

    public AtomList asList() {
        throw new IllegalStateException();
    }

    public Document asDocument() {
        throw new IllegalStateException();
    }

    public SExpression parent() {
        return parent;
    }

    public Document document() {
        if (isDocument()) {
            return asDocument();
        }
        if (parent == null) {
            setParent(Factory.document());
        }
        return parent.document();
    }

    public void setParent(SExpression parent) {
        this.parent = parent;
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        try {
            visit(PrinterVisitor.TO_STRING, output);
        } catch (Exception e) {
            return e.getMessage();
        }
        return output.toString();
    }
}
