package com.laamella.sexpression.model;

import com.laamella.sexpression.visitor.PrinterVisitor;
import com.laamella.sexpression.visitor.Visitor;

import java.util.List;

public abstract class SExpression {
    public List<String> comments;

    private SExpression parent;

    public SExpression(SExpression parent, List<String> comments) {
        this.comments = comments;
        setParent(parent);
    }

    public abstract <A, R> R visit(Visitor<A, R> visitor, A arg) throws Exception;

    public boolean isAtom() {
        return false;
    }

    public boolean isAtomList() {
        return false;
    }

    public boolean isDocument() {
        return false;
    }

    public Atom asAtom() {
        throw new IllegalStateException();
    }

    public AtomList asAtomList() {
        throw new IllegalStateException();
    }

    /**
     * AtomList and Document contain their elements in an internal Vector.
     * This methods returns it.
     * It is a getter camouflaged as a type cast.
     *
     * @return the contents of this node as a vector.
     */
    public List<SExpression> asList() {
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
