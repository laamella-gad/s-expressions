package com.laamella.sexpression;

public interface Visitor<A, R> {
	R accept(Atom atom, A arg) throws Exception;

	R accept(AtomList atomList, A arg) throws Exception;
}
