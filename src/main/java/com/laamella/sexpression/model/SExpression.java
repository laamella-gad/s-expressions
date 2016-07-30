package com.laamella.sexpression.model;

import com.laamella.sexpression.visitor.StructuralPrinterVisitor;

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
	public final Otherwise whenLineTerminator(Consumer<EndOfLine> action) {
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
	public final boolean isSExpression() {
		return true;
	}

	@Override
	public final boolean isMeta() {
		return false;
	}

	@Override
	public final Comment asComment() {
		throw new IllegalStateException();
	}

	@Override
	public final EndOfLine asLineTerminator() {
		throw new IllegalStateException();
	}

	@Override
	public final Whitespace asWhitespace() {
		throw new IllegalStateException();
	}

	@Override
	public SExpression asSExpression() {
		return this;
	}

	@Override
	public Meta asMeta() {
		throw new IllegalStateException();
	}

	@Override
	public String toString() {
		StringBuilder output = new StringBuilder();
		try {
			visit(StructuralPrinterVisitor.TO_STRING, output);
		} catch (Exception e) {
			return e.getMessage();
		}
		return output.toString();
	}
}
