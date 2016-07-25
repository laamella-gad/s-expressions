package com.laamella.sexpression.visitor;

import com.laamella.sexpression.model.Atom;
import com.laamella.sexpression.model.AtomList;

public interface Visitor<A, R> {
    R accept(Atom atom, A arg) throws Exception;

    R accept(AtomList atomList, A arg) throws Exception;
}
