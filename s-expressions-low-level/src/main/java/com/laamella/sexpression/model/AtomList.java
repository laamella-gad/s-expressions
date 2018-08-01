package com.laamella.sexpression.model;

import com.laamella.sexpression.Cursor;
import com.laamella.sexpression.visitor.Visitor;
import io.vavr.collection.Vector;

import java.util.Optional;

import static com.laamella.sexpression.model.Factory.atom;
import static com.laamella.sexpression.visitor.Visitor.EnterDecision;
import static com.laamella.sexpression.visitor.Visitor.EnterDecision.ENTER;

public class AtomList extends SExpression {
    private Vector<Node> nodes;

    public AtomList(Node parent, Vector<Node> nodes) {
        super(parent);
        setNodes(nodes);
    }

    public void add(Node node) {
        setNodes(nodes.append(node));
    }

    public void setNodes(Node... nodes) {
        if (nodes == null) {
            setNodes(Vector.empty());
        } else {
            setNodes(Vector.of(nodes));
        }
    }

    public void setNodes(Vector<Node> nodes) {
        nodes.forEach(n -> n.setParent(this));
        this.nodes = nodes;
    }

    public void add(CharSequence atom) {
        setNodes(nodes.append(atom(atom)));
    }

    public Optional<Integer> findPosition(Node node) {
        for (int i = 0; i < nodes.length(); i++) {
            if (nodes.get(i) == node) {
                return Optional.of(i);
            }
        }
        return Optional.empty();
    }

    public Vector<Node> asVector() {
        return nodes;
    }

    @Override
    public <A, R> R visit(Visitor<A, R> visitor, A arg) throws Exception {
        EnterDecision enter = visitor.enter(this, arg);
        if (enter == ENTER) {
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
        for (Node e : nodes) {
            if (e.isList()) {
                return false;
            }
        }
        return true;
    }

    public boolean isEmpty() {
        return nodes.isEmpty();
    }

    public Cursor cursor() {
        return new Cursor(this);
    }
}
