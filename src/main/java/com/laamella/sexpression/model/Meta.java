package com.laamella.sexpression.model;

import com.laamella.sexpression.visitor.Visitor;

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

}
