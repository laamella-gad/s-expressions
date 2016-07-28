package com.laamella.sexpression;

import com.laamella.sexpression.model.Atom;
import com.laamella.sexpression.model.AtomList;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class SExpressionsParserTest {
	private String result = "";
	private AtomList lastExpression = null;

	private final SExpressionsParser parser = new SExpressionsParser(new SExpressionsParser.Callback() {
		@Override
		public void onOrphanText(String text) {
			result += "|t:" + text;
		}

		@Override
		public void onExpression(AtomList expression) {
			result += "|e:" + expression.toString();
			lastExpression = expression;
		}

		@Override
		public void onComment(String comment) {
			result += "|c:" + comment;
		}

		@Override
		public void onOpenStream() {
			result += "|<";
		}

		@Override
		public void onCloseStream() {
			result += "|>";
		}

		@Override
		public void onError(SExpressionsParser.Error error) {
			result += "|!:" + error.name();
		}
	});

	@Test
	public void lostAtoms() throws IOException {
		CharSource.pushString("wer ry zcv", parser);
		assertEquals("|<|t:wer|t:ry|t:zcv|>", result);
	}

	@Test
	public void oneExpr() throws IOException {
		CharSource.pushString("(wer ry zcv)", parser);
		assertEquals("|<|e:(wer ry zcv)|>", result);
	}

	@Test
	public void atomWithWhitespaceGetsQuoted() throws IOException {
		CharSource.pushString("(\"wer ry zcv\")", parser);
		Atom atom = lastExpression.list().get(0).asAtom();
		assertEquals("wer ry zcv", atom.value());
		assertEquals("|<|e:(\"wer ry zcv\")|>", result);
	}

	@Test
	public void atomWithBinaryDataGetsBase64Encoded() throws IOException {
		CharSource.pushString("(abc |AAECAwQFBg==| abc)", parser);
		Atom atom = lastExpression.list().get(1).asAtom();
		assertArrayEquals(new byte[]{0, 1, 2, 3, 4, 5, 6}, atom.raw());
		assertEquals("|<|e:(abc |AAECAwQFBg==| abc)|>", result);
	}


	@Test
	public void nestedExpr() throws IOException {
		CharSource.pushString("(wer (ry zcv) (1 2) kkk)", parser);
		assertEquals("|<|e:(wer (ry zcv) (1 2) kkk)|>", result);
	}

	@Test
	public void tooManyClosingParentheses() throws IOException {
		CharSource.pushString("())", parser);
		assertEquals("|<|e:()|!:TOO_MANY_CLOSING_PARENTHESES|>", result);
	}

	@Test
	public void unclosedParentheses() throws IOException {
		CharSource.pushString("(", parser);
		assertEquals("|<|!:UNCLOSED_PARENTHESES|>", result);
	}

}