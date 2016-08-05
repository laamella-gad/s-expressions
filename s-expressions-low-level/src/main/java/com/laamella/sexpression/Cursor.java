package com.laamella.sexpression;

import com.laamella.sexpression.model.*;

import java.util.Optional;
import java.util.function.Predicate;

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
        }
        return this;
    }

    private void insertNode(Node node) {
        list.setNodes(list.asVector().insert(position, node));
        position++;
    }

    /**
     * Insert a new AtomList.
     * Afterwards, the cursor is at the start of the new list.
     */
    public Cursor insertAndEnterList() {
        AtomList newList = Factory.list();
        insertNode(newList);
        list = newList;
        position = 0;
        return this;
    }

    private Node nodeAt(int i) {
        return list.asVector().get(i);
    }

    public Cursor goToNext(Predicate<Node> check) {
        // TODO needs a position++ here
        while (!atEnd() && !check.test(list.asVector().get(position))) {
            position++;
        }
        return this;
    }

    private void goToPrevious(Predicate<Node> check) {
        // TODO needs a position-- here
        while (!atFirstNode() && !check.test(list.asVector().get(position))) {
            position--;
        }
    }

    public Cursor goForward() {
        if (!atEnd()) {
            position++;
        }
        return this;
    }

    public Cursor goBackward() {
        if (!atFirstNode()) {
            position--;
        }
        return this;
    }

    public Cursor exitList() {
        AtomList oldList = list;
        list = list.parent().asList();
        position = list.findPosition(oldList).get();
        return this;
    }

    public Cursor enterList() {
        list = node().get().asList();
        goToBeginning();
        return this;
    }

    public Cursor goToEnd() {
        position = list.asVector().length();
        return this;
    }

    public Cursor goToBeginning() {
        position = 0;
        return this;
    }

    public Optional<Node> node() {
        if (atEnd()) {
            return Optional.empty();
        }
        return Optional.of(list.asVector().get(position));
    }

    public Document document() {
        return list.document();
    }

    public boolean atEnd() {
        return position >= list.asVector().size();
    }

    public boolean atFirstNode() {
        return position == 0;
    }

    public Cursor anotherCursor() {
        return new Cursor(list, position);
    }

    public Cursor goTo(Node node) {
        list.findPosition(node).ifPresent(p -> position = p);
        return this;
    }

    public Cursor goToPosition(int i) {
        goToBeginning();
        while (i > 0) {
            goForward();
            i--;
        }
        return this;
    }
}
