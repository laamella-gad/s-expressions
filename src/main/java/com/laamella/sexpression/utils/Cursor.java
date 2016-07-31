package com.laamella.sexpression.utils;

import com.laamella.sexpression.model.AtomList;
import com.laamella.sexpression.model.Node;

import java.util.Optional;

public class Cursor {
    private AtomList list;
    private int position = 0;

    public Cursor(AtomList list) {
        this.list = list;
    }

    public Cursor() {
        list = new AtomList();
    }

    public Cursor insert(Node node) {
        return this;
    }

    public Cursor insertListAndEnter() {
        return this;
    }

    public Cursor forward() {
        return this;
    }

    public Cursor backward() {
        return this;
    }

    public Cursor forwardNode() {
        return this;
    }

    public Cursor backwardNode() {
        return this;
    }

    public Cursor exitList() {
        return this;
    }

    public Cursor enterList() {
        return this;
    }

    public Cursor toEnd() {
        return this;
    }

    public Cursor toBegin() {
        position = 0;
        return this;
    }

    public Optional<Node> node() {
        if (positionValid()) {
            return Optional.of(list.nodes().get(position));
        } else
            return Optional.empty();
    }

    private boolean positionValid() {
        return position < list.nodes().size();
    }
}
