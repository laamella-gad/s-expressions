package com.laamella.sexpression.model;

import java.util.function.Consumer;

public abstract class SExpression implements Node {
	@Override
	public final Otherwise whenComment(Consumer<Comment> action) {
		return new Otherwise(true);
	}

	@Override
	public final Otherwise whenWhitespace(Consumer<Whitespace> action) {
		return new Otherwise(true);
	}

	@Override
	public final Otherwise whenLineTerminator(Consumer<LineTerminator> action) {
		return new Otherwise(true);
	}

	@Override
	public final boolean isComment() {
		return false;
	}

	@Override
	public final boolean isWhitespace() {
		return false;
	}

	@Override
	public final boolean isLineTerminator() {
		return false;
	}

	@Override
	public final Comment asComment() {
		throw new IllegalStateException();
	}

	@Override
	public final LineTerminator asLineTerminator() {
		throw new IllegalStateException();
	}

	@Override
	public final Whitespace asWhitespace() {
		throw new IllegalStateException();
	}
}
