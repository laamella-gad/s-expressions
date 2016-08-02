package com.laamella.sexpression.model;

import com.laamella.sexpression.visitor.Visitor;
import javaslang.collection.Vector;

import java.util.Optional;

import static com.laamella.sexpression.model.Factory.atom;
import static com.laamella.sexpression.visitor.Visitor.EnterDecision;

public class AtomList extends SExpression {
    private Vector<SExpression> list;

    public AtomList(SExpression parent, Vector<SExpression> nodes, Vector<String> comments) {
        super(parent, comments);
        setNodes(nodes);
    }

    public void add(SExpression node) {
        setNodes(list.append(node));
    }

    public void setNodes(SExpression... nodes) {
        if (nodes == null) {
            setNodes(Vector.empty());
        } else {
            setNodes(Vector.of(nodes));
        }
    }

    public void setNodes(Vector<SExpression> nodes) {
        nodes.forEach(n -> n.setParent(this));
        this.list = nodes;
    }

    public void add(CharSequence atom) {
        setNodes(list.append(atom(atom)));
    }

    public Optional<Integer> findPositionOfNode(SExpression node) {
        for (int i = 0; i < list.length(); i++) {
            if (list.get(i) == node) {
                return Optional.of(i);
            }
        }
        return Optional.empty();
    }

    public Optional<Integer> findPosition(SExpression e) {
        for (int i = 0; i < list.length(); i++) {
            if (list.get(i) == e) {
                return Optional.of(i);
            }
        }
        return Optional.empty();
    }

    public Vector<SExpression> list() {
        return list;
    }


    @Override
    public <A, R> R visit(Visitor<A, R> visitor, A arg) throws Exception {
        EnterDecision enter = visitor.enter(this, arg);
        if (enter == EnterDecision.ENTER) {
            R r = visitor.accept(this, arg);
            visitor.exit(this, r, arg);
            return r;
        }
        return null;
    }

    @Override
    public boolean isList() {
        return true;
    }

    @Override
    public AtomList asList() {
        return this;
    }

    public boolean isAllAtoms() {
        for (SExpression e : list) {
            if (e.isList()) {
                return false;
            }
        }
        return true;
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }
}
