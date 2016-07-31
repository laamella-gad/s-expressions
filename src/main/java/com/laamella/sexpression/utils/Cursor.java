package com.laamella.sexpression.utils;

import com.laamella.sexpression.model.*;

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
    public Cursor insertNodes(Node... newNodes) {
        for (Node n : newNodes) {
            insertNode(n);
        }
        return this;
    }

    public Cursor insert(String... atoms) {
        for (String s : atoms) {
            insertSpaceBefore();
            insertNode(atom(s));
        }
        insertSpaceAfter();
        return this;
    }

    public Cursor insert(SExpression... exprs) {
        for (SExpression s : exprs) {
            insertSpaceBefore();
            insertNode(s);
        }
        insertSpaceAfter();
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
        insertSpaceBefore();
        AtomList newList = Factory.list();
        insertNode(newList);
        insertSpaceAfter();
        list = newList;
        position = 0;
        return this;
    }

    private void insertSpaceBefore() {
        if (!atFirstNode()) {
            if (!nodeAt(position - 1).isWhitespace()) {
                insertNode(space());
            }
        }
    }

    private void insertSpaceAfter() {
        if (!atEnd()) {
            if (!nodeAt(position).isWhitespace()) {
                insertNode(space());
            }
        }
    }

    private Node nodeAt(int i) {
        return list.nodes().get(i);
    }

    public Cursor goForward() {
        if (!atEnd()) {
            position++;
        }
        goToNext(Node::isSExpression);
        return this;
    }

    public Cursor goToNext(Predicate<Node> check) {
        while (!atEnd() && !check.test(list.nodes().get(position))) {
            position++;
        }
        return this;
    }

    public Cursor goBackward() {
        if (!atFirstNode()) {
            position--;
            goToPrevious(Node::isSExpression);
        }
        return this;
    }

    private void goToPrevious(Predicate<Node> check) {
        while (!atFirstNode() && !check.test(list.nodes().get(position))) {
            position--;
        }
    }

    public Cursor goForwardNode() {
        if (!atEnd()) {
            position++;
        }
        return this;
    }

    public Cursor goBackwardNode() {
        if (!atFirstNode()) {
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
        goToBeginning();
        return this;
    }

    public Cursor goToEnd() {
        position = list.nodes().length();
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
        return Optional.of(list.nodes().get(position));
    }

    public Document document() {
        return list.document();
    }

    public boolean atEnd() {
        return position >= list.nodes().size();
    }

    public boolean atFirstNode() {
        return position == 0;
    }

    public Cursor anotherCursor() {
        return new Cursor(list, position);
    }

    public Cursor goTo(Node node) {
        list.findPositionOfNode(node).ifPresent(p -> position = p);
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
