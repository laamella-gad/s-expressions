package com.laamella.sexpression.model;

import com.laamella.sexpression.visitor.StructuralPrinterVisitor;
import com.laamella.sexpression.visitor.Visitor;

public abstract class Node {
    private Node parent;

    public Node(Node parent) {
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

    public boolean isComment() {
        return false;
    }

    public boolean isWhitespace() {
        return false;
    }

    public boolean isLineTerminator() {
        return false;
    }

    public boolean isSExpression() {
        return false;
    }

    public boolean isMeta() {
        return false;
    }

    public Atom asAtom() {
        throw new IllegalStateException();
    }

    public AtomList asList() {
        throw new IllegalStateException();
    }

    public Comment asComment() {
        throw new IllegalStateException();
    }

    public Document asDocument() {
        throw new IllegalStateException();
    }

    public LineTerminator asLineTerminator() {
        throw new IllegalStateException();
    }

    public Whitespace asWhitespace() {
        throw new IllegalStateException();
    }

    public SExpression asSExpression() {
        throw new IllegalStateException();
    }

    public Meta asMeta() {
        throw new IllegalStateException();
    }

    public Node parent() {
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

    public void setParent(Node parent) {
        this.parent = parent;
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        try {
            visit(StructuralPrinterVisitor.TO_STRING, output);
        } catch (Exception e) {
            return e.getMessage();
        }
        return output.toString();
    }
}
