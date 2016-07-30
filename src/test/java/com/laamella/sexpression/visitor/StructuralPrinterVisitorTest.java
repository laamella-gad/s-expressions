package com.laamella.sexpression.visitor;

import com.laamella.sexpression.model.Atom;
import com.laamella.sexpression.model.AtomList;
import com.laamella.sexpression.model.Document;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StructuralPrinterVisitorTest {
    private final StructuralPrinterVisitor visitor = new StructuralPrinterVisitor();
    private final StringBuilder output = new StringBuilder();

    @Test
    public void printDocument() throws Exception {
        visitor.accept(new Document(new Atom("abc"), new Atom("def")), output);
        assertEquals("abc def", output.toString());
    }

    @Test
    public void printAtomList() throws Exception {
        visitor.accept(new AtomList(new Atom("abc"), new Atom("def")), output);
        assertEquals("(abc def)", output.toString());
    }
}