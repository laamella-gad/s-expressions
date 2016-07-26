package com.laamella.sexpression.model;

import com.laamella.sexpression.visitor.Visitor;

public interface SExpression extends Node {
    <A, R> R visit(Visitor<A, R> visitor, A arg) throws Exception;
}

