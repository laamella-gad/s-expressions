package com.laamella.sexpression.model;

import com.laamella.sexpression.visitor.Visitor;

import java.util.function.Consumer;

public class EndOfLine extends Meta {
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
		return false;
	}

	@Override
	public boolean isLineTerminator() {
		return true;
	}

	@Override
	public Comment asComment() {
		throw new IllegalStateException();
	}

	@Override
	public EndOfLine asLineTerminator() {
		return this;
	}

	@Override
	public Whitespace asWhitespace() {
		throw new IllegalStateException();
	}
}
