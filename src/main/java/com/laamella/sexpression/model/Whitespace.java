package com.laamella.sexpression.model;

import com.laamella.sexpression.visitor.Visitor;

public class Whitespace extends Meta {
	public String whitespace;

	public Whitespace(String whitespace){
		this.whitespace = whitespace;
	}
	@Override
	public <A, R> R visit(Visitor<A, R> visitor, A arg) throws Exception {
		return visitor.accept(this, arg);
	}

	@Override
	public boolean isComment() {
		return false;
	}

	@Override
	public boolean isWhitespace() {
		return true;
	}

	@Override
	public Whitespace asWhitespace() {
		return this;
	}
}
