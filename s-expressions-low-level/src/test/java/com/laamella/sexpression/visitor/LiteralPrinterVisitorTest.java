package com.laamella.sexpression.visitor;

import com.laamella.sexpression.model.Document;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class LiteralPrinterVisitorTest {
    @Test
    public void atom() {
        assertSameAfterParsingAndPrinting("abc");
    }

    @Test
    public void atomList() {
        assertSameAfterParsingAndPrinting("(abc)");
    }

    @Test
    public void whitespaceAndQuotesAndComments() {
        assertSameAfterParsingAndPrinting(" ( ) \" \n \" \n ; \n\t");
    }

    private void assertSameAfterParsingAndPrinting(String e) {
        Document document = Document.fromString(e);
        StringBuilder out = new StringBuilder();
        try {
            document.visit(new LiteralPrinterVisitor(), out);
        } catch (Exception e1) {
            fail("");
        }
        assertEquals(e, out.toString());
    }
}