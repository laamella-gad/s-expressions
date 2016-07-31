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
                .insert(comment("lol"), nl(), list(), space(), atom("lol2"), space())
                .insert(atom("Just before the end"))
                .insert(atom("At the end"));

        assertResult(";lol\n() lol2 \"Just before the end\"\"At the end\"", cursor);
    }

    @Test
    public void insertAtoms() throws Exception {
        Cursor cursor = new Cursor()
                .insert("a", "b", "c");

        assertResult("a b c", cursor);
    }


    @Test
    public void insertAtBeginAndEnd() throws Exception {
        Cursor cursor = new Cursor()
                .insert(atom("x"), atom("x"))
                .toBegin()
                .insert(atom("b"))
                .toEnd()
                .insert(atom("e"));

        assertResult("bxxe", cursor);
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
                .toEnd()
                .insert("i")
                .exitList()
                .toEnd()
                .insert("o");

        assertResult("o (e (x) i) o", cursor);
    }

    private void assertResult(String expected, Cursor cursor) throws Exception {
        StringBuilder output = new StringBuilder();
        cursor.document().visit(new LiteralPrinterVisitor(), output);
        assertEquals(expected, output.toString());
    }
}