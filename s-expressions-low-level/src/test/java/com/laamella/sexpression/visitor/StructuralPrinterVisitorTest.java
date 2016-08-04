package com.laamella.sexpression.visitor;

import org.junit.Test;

import static com.laamella.sexpression.model.Factory.*;
import static org.junit.Assert.assertEquals;

public class StructuralPrinterVisitorTest {
	private final StructuralPrinterVisitor visitor = new StructuralPrinterVisitor();
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