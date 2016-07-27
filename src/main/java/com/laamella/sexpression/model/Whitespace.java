package com.laamella.sexpression.model;

import com.laamella.sexpression.visitor.Visitor;

import java.util.function.Consumer;

public class Whitespace extends Meta {
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
		action.accept(this);
		return new Otherwise(false);

	}

	@Override
	public Otherwise whenLineTerminator(Consumer<LineTerminator> action) {
		return new Otherwise(true);
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
	public LineTerminator asLineTerminator() {
		throw new IllegalStateException();
	}

	@Override
	public Whitespace asWhitespace() {
		return this;
	}
}
