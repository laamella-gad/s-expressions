package com.laamella.sexpression.model;

import com.laamella.sexpression.visitor.Visitor;

import java.util.function.Consumer;

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
	public boolean isLineTerminator() {
		return false;
	}

	@Override
	public Comment asComment() {
		throw new IllegalStateException();
	}

	@Override
	public EndOfLine asLineTerminator() {
		throw new IllegalStateException();
	}

	@Override
	public Whitespace asWhitespace() {
		return this;
	}
}
