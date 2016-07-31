package com.laamella.sexpression.model;

import com.laamella.sexpression.visitor.Visitor;

import java.util.function.Consumer;

public abstract class Node {
    private Node parent;

    public abstract <A, R> R visit(Visitor<A, R> visitor, A arg) throws Exception;

    public abstract boolean isAtom();

    public abstract boolean isList();

    public abstract boolean isComment();

    public abstract boolean isWhitespace();

    public abstract boolean isLineTerminator();

    public abstract boolean isSExpression();

    public abstract boolean isMeta();

    public abstract Atom asAtom();

    public abstract AtomList asList();

    public abstract Comment asComment();

    public abstract EndOfLine asLineTerminator();

    public abstract Whitespace asWhitespace();

    public abstract SExpression asSExpression();

    public abstract Meta asMeta();

    public Node parent() {
        return parent;
    }
    
    public void setParent(Node parent){
        this.parent = parent;
    }
}
