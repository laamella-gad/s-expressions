package com.laamella.sexpression.utils;

import com.laamella.sexpression.model.AtomList;
import com.laamella.sexpression.model.Document;
import com.laamella.sexpression.model.Factory;
import com.laamella.sexpression.model.Node;
import javaslang.collection.Vector;

import java.util.Optional;
import java.util.function.Predicate;

import static com.laamella.sexpression.model.Factory.atom;
import static com.laamella.sexpression.model.Factory.space;

public class Cursor {
    private AtomList list;
    private int position = 0;

    public Cursor(AtomList list) {
        this.list = list;
    }

    public Cursor(AtomList list, int position) {
        this.list = list;
        this.position = position;
    }

    public Cursor() {
        list = Factory.document();
    }

    /**
     * Insert the nodes.
     * Afterwards, the cursor is just after the inserted nodes.
     */
    public Cursor insert(Node... newNodes) {
        for (Node n : newNodes) {
            insertNode(n);
            forwardNode();
        }
        return this;
    }

    public Cursor insert(String... atoms) {
        for (String s : atoms) {
            insertSpaceWhenNecessary();

            insertNode(atom(s));
        }
        return this;
    }

    private void insertNode(Node node) {
        list.setNodes(list.nodes().insert(position, node));
        position++;
    }

    /**
     * Insert a new AtomList.
     * Afterwards, the cursor is at the start of the new list.
     */
    public Cursor insertAndEnterList() {
        insertSpaceWhenNecessary();
        AtomList newList = Factory.list();
        insert(newList);
        list = newList;
        position = 0;
        return this;
    }

    private void insertSpaceWhenNecessary() {
        if (!atBeginOfList()) {
            if (!nodeAt(position - 1).isWhitespace()) {
                insertNode(space());
            }
        }
    }

    private Node nodeAt(int i) {
        return list.nodes().get(i);
    }

    public Cursor forward() {
        if (!atEndOfList()) {
            position++;
        }
        forwardToNext(Node::isSExpression);
        return this;
    }

    public Cursor forwardToNext(Predicate<Node> check) {
        while (!atEndOfList() && check.test(list.nodes().get(position))) {
            position++;
        }
        return this;
    }

    public Cursor backward() {
        if (!atBeginOfList()) {
            position--;
            backwardToNext(Node::isSExpression);
        }
        return this;
    }

    private void backwardToNext(Predicate<Node> check) {
        while (!atBeginOfList() && check.test(list.nodes().get(position))) {
            position--;
        }
    }

    public Cursor forwardNode() {
        if (!atEndOfList()) {
            position++;
        }
        return this;
    }

    public Cursor backwardNode() {
        if (!atBeginOfList()) {
            position--;
        }
        return this;
    }

    public Cursor exitList() {
        AtomList oldList = list;
        list = list.parent().asList();
        position = list.findPositionOfNode(oldList).get();
        return this;
    }

    public Cursor enterList() {
        list = node().get().asList();
        toBegin();
        return this;
    }

    public Cursor toEnd() {
        position = list.nodes().length();
        return this;
    }

    public Cursor toBegin() {
        position = 0;
        return this;
    }

    public Optional<Node> node() {
        if (atEndOfList()) {
            return Optional.empty();
        }
        return Optional.of(list.nodes().get(position));
    }

    public Document document() {
        return list.document();
    }

    public boolean atEndOfList() {
        return position >= list.nodes().size();
    }

    public boolean atBeginOfList() {
        return position == 0;
    }

    public Cursor anotherCursor() {
        return new Cursor(list, position);
    }

    public Cursor goTo(Node node) {
        Vector<Node> nodes = list.nodes();
        for (int i = 0; i < nodes.length(); i++) {
            if (nodes.get(i) == node) {
                position = i;
                return this;
            }
        }
        return this;
    }
}
