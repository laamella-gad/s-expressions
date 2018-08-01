package com.laamella.sexpression.utils;

import com.laamella.sexpression.Cursor;
import com.laamella.sexpression.visitor.LiteralPrinterVisitor;
import org.junit.jupiter.api.Test;

import static com.laamella.sexpression.model.Factory.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
	public void insertAtBeginAndEnd() throws Exception {
		Cursor cursor = new Cursor()
				.insert(atom("x"), atom("x"))
				.goToBeginning()
				.insert(atom("b"))
				.goToEnd()
				.insert(atom("e"));

		assertResult("bxxe", cursor);
	}

	@Test
	public void goForwardBackwardNode() throws Exception {
		Cursor cursor = new Cursor()
				.insert(atom("b"), atom("e"))
				.goBackward()
				.insert(atom("d"))
				.goToBeginning()
				.insert(atom("a"))
				.goForward()
				.insert(atom("c"));

		assertResult("abcde", cursor);
	}


	@Test
	public void enterLists() throws Exception {
		Cursor cursor = new Cursor()
				.insert(atom("o"))
				.insertAndEnterList()
				.insert(atom("e"))
				.insertAndEnterList()
				.insert(atom("x"))
				.exitList()
				.goToEnd()
				.insert(atom("i"))
				.exitList()
				.goToEnd()
				.insert(atom("o"));

		assertResult("o(e(x)i)o", cursor);
	}

	private void assertResult(String expected, Cursor cursor) throws Exception {
		StringBuilder output = new StringBuilder();
		cursor.document().visit(new LiteralPrinterVisitor(), output);
		assertEquals(expected, output.toString());
	}
}