package com.laamella.sexpression.visitor;

import org.junit.Test;

import static com.laamella.sexpression.model.Factory.*;
import static org.junit.Assert.assertEquals;

public class StructuralPrinterVisitorTest {
    private final PrinterVisitor visitor = new PrinterVisitor();
    private final StringBuilder output = new StringBuilder();

    @Test
    public void printDocument() throws Exception {
        visitor.accept(document(atom("abc"), atom("def")), output);
        assertEquals("abc def", output.toString());
    }

    @Test
    public void printAtomList() throws Exception {
        visitor.accept(list(atom("abc"), atom("def")), output);
        assertEquals("(abc def)", output.toString());
    }
}