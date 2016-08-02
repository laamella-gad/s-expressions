package com.laamella.sexpression.model;

import com.laamella.sexpression.utils.Cursor;
import com.laamella.sexpression.visitor.Visitor;
import javaslang.collection.Vector;

import java.util.Optional;

import static com.laamella.sexpression.model.Factory.atom;
import static com.laamella.sexpression.visitor.Visitor.EnterDecision;

public class AtomList extends SExpression {
    private Vector<Node> nodes;
    private Vector<SExpression> list;

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
        list = nodes.filter(Node::isSExpression).map(Node::asSExpression);
    }

    public void add(CharSequence atom) {
        setNodes(nodes.append(atom(atom)));
    }

    public Optional<Integer> findPositionOfNode(Node node) {
        for (int i = 0; i < nodes.length(); i++) {
            if (nodes.get(i) == node) {
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

    /**
     * @return all the nodes here, including comments, whitespace, etc.
     */
    public Vector<Node> nodes() {
        return nodes;
    }

    /**
     * @return the atoms and lists inside this list.
     */
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
