package com.laamella.sexpression.model;

import com.laamella.sexpression.visitor.StructuralPrinterVisitor;

import java.util.function.Consumer;

public abstract class Meta implements Node {
	@Override
	public final Otherwise whenList(Consumer<AtomList> action) {
		return new Otherwise(true);
	}

	@Override
	public final Otherwise whenAtom(Consumer<Atom> action) {
		return new Otherwise(true);
	}

	@Override
	public final boolean isAtom() {
		return false;
	}

	@Override
	public final boolean isList() {
		return false;
	}

	@Override
	public final Atom asAtom() {
		throw new IllegalStateException();
	}

	@Override
	public final AtomList asList() {
		throw new IllegalStateException();
	}

	@Override
	public SExpression asSExpression() {
		throw new IllegalStateException();
	}

	@Override
	public Meta asMeta() {
		return this;
	}

	@Override
	public boolean isSExpression() {
		return false;
	}

	@Override
	public boolean isMeta() {
		return true;
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
