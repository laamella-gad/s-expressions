package com.laamella.sexpression.model;

import com.laamella.sexpression.visitor.Visitor;

import java.util.function.Consumer;

public interface Node {
    <A, R> R visit(Visitor<A, R> visitor, A arg) throws Exception;

    Otherwise whenList(Consumer<AtomList> action);

    Otherwise whenAtom(Consumer<Atom> action);

    Otherwise whenComment(Consumer<Comment> action);

    Otherwise whenWhitespace(Consumer<Whitespace> action);

    Otherwise whenLineTerminator(Consumer<EndOfLine> action);

    // TODO whens for the meta nodes

    boolean isAtom();

    boolean isList();

    boolean isComment();

    boolean isWhitespace();

    boolean isLineTerminator();

    boolean isSExpression();

    boolean isMeta();

    Atom asAtom();

    AtomList asList();

    Comment asComment();

    EndOfLine asLineTerminator();

    Whitespace asWhitespace();

    SExpression asSExpression();

    Meta asMeta();

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
