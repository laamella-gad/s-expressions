package com.laamella.sexpression.visitor;

import org.junit.jupiter.api.Test;

import static com.laamella.sexpression.model.Factory.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PrinterVisitorTest {
    private final PrinterVisitor visitor = new PrinterVisitor();
    private final StringBuilder output = new StringBuilder();

    @Test
    public void printDocument() throws Exception {
        document(atom("abc"), atom("def")).visit(visitor, output);
        assertEquals("abc def", output.toString());
    }

    @Test
    public void printAtomList() throws Exception {
        list(atom("abc"), atom("def")).visit(visitor, output);
        assertEquals("(abc def)", output.toString());
    }
}