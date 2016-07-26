package com.laamella.sexpression.model;

import com.laamella.sexpression.visitor.Visitor;

import java.util.function.Consumer;

public interface Node {
    <A, R> R visit(Visitor<A, R> visitor, A arg) throws Exception;

    Otherwise whenList(Consumer<AtomList> action);

    Otherwise whenAtom(Consumer<Atom> action);

    Otherwise whenComment(Consumer<Comment> action);

    boolean isAtom();

    boolean isList();

    boolean isComment();

    Atom toAtom();

    AtomList toList();

    Comment toComment();

    class Otherwise {
        private final boolean b;

        public Otherwise(boolean executeElse) {
            this.b = executeElse;
        }

        public void otherwise(Runnable r) {
            if (b) r.run();
        }
    }
}
