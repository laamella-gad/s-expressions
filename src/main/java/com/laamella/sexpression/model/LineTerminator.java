package com.laamella.sexpression.model;

import com.laamella.sexpression.visitor.Visitor;

import java.util.function.Consumer;

public class LineTerminator extends Meta {
	@Override
	public <A, R> R visit(Visitor<A, R> visitor, A arg) throws Exception {
		return visitor.accept(this, arg);
	}

	@Override
	public Otherwise whenComment(Consumer<Comment> action) {
		return new Otherwise(true);
	}

	@Override
	public Otherwise whenWhitespace(Consumer<Whitespace> action) {
		return new Otherwise(true);
	}

	@Override
	public Otherwise whenLineTerminator(Consumer<LineTerminator> action) {
		action.accept(this);
		return new Otherwise(false);
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
	public LineTerminator asLineTerminator() {
		return this;
	}

	@Override
	public Whitespace asWhitespace() {
		throw new IllegalStateException();
	}
}
