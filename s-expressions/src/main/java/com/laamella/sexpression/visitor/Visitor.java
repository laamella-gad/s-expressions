package com.laamella.sexpression.visitor;

import com.laamella.sexpression.model.Atom;
import com.laamella.sexpression.model.AtomList;
import com.laamella.sexpression.model.Document;
import com.laamella.sexpression.model.SExpression;

import static com.laamella.sexpression.visitor.Visitor.EnterDecision.ENTER;

public interface Visitor<A, R> {
    Visitor<Appendable, Void> TO_STRING = new PrinterVisitor();

    enum EnterDecision {ENTER, DONT_ENTER}

    R accept(Atom atom, A arg) throws Exception;

    R accept(AtomList atomList, A arg) throws Exception;

    EnterDecision enter(AtomList atomList, A arg) throws Exception;

    void exit(AtomList atomList, R result, A arg) throws Exception;

    EnterDecision enter(Document document, A arg) throws Exception;

    void exit(Document document, R result, A arg) throws Exception;

    R accept(Document document, A arg) throws Exception;

    class Adapter<A, R> implements Visitor<A, R> {
        @Override
        public R accept(Atom atom, A arg) throws Exception {
            return null;
        }

        @Override
        public R accept(AtomList atomList, A arg) throws Exception {
            for (SExpression n : atomList.asVector()) {
                n.visit(this, arg);
            }
            return null;
        }

        @Override
        public EnterDecision enter(AtomList atomList, A arg) throws Exception {
            return ENTER;
        }

        @Override
        public void exit(AtomList atomList, R result, A arg) throws Exception {
        }

        @Override
        public EnterDecision enter(Document document, A arg) throws Exception {
            return ENTER;
        }

        @Override
        public void exit(Document document, R result, A arg) throws Exception {
        }

        @Override
        public R accept(Document document, A arg) throws Exception {
            for (SExpression n : document.asVector()) {
                n.visit(this, arg);
            }
            return null;
        }
    }
}
