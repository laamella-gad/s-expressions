package com.laamella.sexpression.visitor;

import com.laamella.sexpression.Atom;
import com.laamella.sexpression.AtomList;

public interface Visitor<A, R> {
	R accept(Atom atom, A arg) throws Exception;

	R accept(AtomList atomList, A arg) throws Exception;
}
