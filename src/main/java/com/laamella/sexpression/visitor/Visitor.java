package com.laamella.sexpression.visitor;

import com.laamella.sexpression.model.Atom;
import com.laamella.sexpression.model.AtomList;
import com.laamella.sexpression.model.Comment;

public interface Visitor<A, R> {
    R accept(Atom atom, A arg) throws Exception;

    R accept(AtomList atomList, A arg) throws Exception;

    R accept(Comment comment, A arg) throws Exception;
    
    class Adapter<A,R> implements Visitor<A, R> {

        @Override
        public R accept(Atom atom, A arg) throws Exception {
            return null;
        }

        @Override
        public R accept(AtomList atomList, A arg) throws Exception {
            return null;
        }

        @Override
        public R accept(Comment comment, A arg) throws Exception {
            return null;
        }
    }
}
