package com.laamella.sexpression;

import com.laamella.sexpression.visitor.Visitor;

public interface SExpressionNode {
	<A, R> R visit(Visitor<A, R> visitor, A arg) throws Exception;
}

