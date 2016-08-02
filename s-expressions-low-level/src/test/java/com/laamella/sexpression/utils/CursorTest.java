package com.laamella.sexpression.utils;

import com.laamella.sexpression.visitor.LiteralPrinterVisitor;
import org.junit.Test;

import static com.laamella.sexpression.model.Factory.*;
import static org.junit.Assert.assertEquals;

public class CursorTest {
    @Test
    public void defaultConstructorMakesAnEmptyDocument() throws Exception {
        Cursor cursor = new Cursor();
        assertResult("", cursor);
    }

    @Test
    public void insertNodes() throws Exception {
        Cursor cursor = new Cursor()
                .insertNodes(comment("lol"), nl(), list(), space(), atom("lol2"), space())
                .insertNodes(atom("Just before the end"))
                .insertNodes(atom("At the end"));

        assertResult(";lol\n() lol2 \"Just before the end\"\"At the end\"", cursor);
    }

    @Test
    public void insertAtoms() throws Exception {
        Cursor cursor = new Cursor()
                .insert("a", "b", "c")
                .goToBeginning()
                .insert("1")
                .goToEnd()
                .insert("2")
                .goToPosition(2)
                .insert("x");

        assertResult("1 a x b c 2", cursor);
    }

    @Test
    public void insertAtBeginAndEnd() throws Exception {
        Cursor cursor = new Cursor()
                .insertNodes(atom("x"), atom("x"))
                .goToBeginning()
                .insertNodes(atom("b"))
                .goToEnd()
                .insertNodes(atom("e"));

        assertResult("bxxe", cursor);
    }

    @Test
    public void goForwardBackwardNode() throws Exception {
        Cursor cursor = new Cursor()
                .insertNodes(atom("b"), atom("e"))
                .goBackwardNode()
                .insertNodes(atom("d"))
                .goToBeginning()
                .insertNodes(atom("a"))
                .goForwardNode()
                .insertNodes(atom("c"));

        assertResult("abcde", cursor);
    }


    @Test
    public void goForwardBackward() throws Exception {
        Cursor cursor = new Cursor()
                .insert("b", "e")
                .goBackward()
                .insert("d")
                .goToBeginning()
                .insert("a")
                .goForward()
                .insert("c");

        assertResult("a b c d e", cursor);
    }

    @Test
    public void enterLists() throws Exception {
        Cursor cursor = new Cursor()
                .insert("o")
                .insertAndEnterList()
                .insert("e")
                .insertAndEnterList()
                .insert("x")
                .exitList()
                .goToEnd()
                .insert("i")
                .exitList()
                .goToEnd()
                .insert("o");

        assertResult("o (e (x) i) o", cursor);
    }

    private void assertResult(String expected, Cursor cursor) throws Exception {
        StringBuilder output = new StringBuilder();
        cursor.document().visit(new LiteralPrinterVisitor(), output);
        assertEquals(expected, output.toString());
    }
}