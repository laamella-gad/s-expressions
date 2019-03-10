package com.laamella.sexpression.model;

import com.laamella.sexpression.visitor.Visitor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.laamella.sexpression.model.Factory.atom;
import static com.laamella.sexpression.visitor.Visitor.EnterDecision;

public class AtomList extends SExpression {
    private List<SExpression> list;

    public AtomList(SExpression parent, List<SExpression> nodes, List<String> comments) {
        super(parent, comments);
        setNodes(nodes);
    }

    public void add(SExpression node) {
        list.add(node);
        node.setParent(this);
    }

    public void setNodes(SExpression... nodes) {
        if (nodes == null) {
            setNodes(new ArrayList<>());
        } else {
            setNodes(Arrays.asList(nodes));
        }
    }

    public void setNodes(List<SExpression> nodes) {
        fixParents(nodes);
        this.list = nodes;
    }

    private void fixParents(List<SExpression> nodes) {
        nodes.forEach(n -> n.setParent(this));
    }

    public void add(CharSequence atom) {
        add(atom(atom));
    }

    public Optional<Integer> findPosition(SExpression e) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) == e) {
                return Optional.of(i);
            }
        }
        return Optional.empty();
    }

    public List<SExpression> asList() {
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
    public boolean isAtomList() {
        return true;
    }

    @Override
    public AtomList asAtomList() {
        return this;
    }

    public boolean isAllAtoms() {
        for (SExpression e : list) {
            if (e.isAtomList()) {
                return false;
            }
        }
        return true;
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }
}
