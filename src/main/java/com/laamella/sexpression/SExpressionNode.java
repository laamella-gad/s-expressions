package com.laamella.sexpression;

public interface SExpressionNode {
	<A, R> R visit(Visitor<A, R> visitor, A arg) throws Exception;
}

