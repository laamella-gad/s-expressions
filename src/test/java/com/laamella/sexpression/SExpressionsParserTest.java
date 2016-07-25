package com.laamella.sexpression;

import org.junit.Test;

import java.io.IOException;

import static com.laamella.sexpression.CharSource.push;
import static org.junit.Assert.assertEquals;

public class SExpressionsParserTest {
	private String result = "";
	private final SExpressionsParser parser = new SExpressionsParser(new SExpressionsParser.Callback() {
		@Override
		public void onOrphanAtom(String text) {
			result += "|a:" + text;
		}

		@Override
		public void onExpression(AtomList expression) {
			result += "|e:" + expression.toString();
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
		push("wer ry zcv", parser);
		assertEquals("|<|a:wer|a:ry|a:zcv|>", result);
	}

	@Test
	public void oneExpr() throws IOException {
		push("(wer ry zcv)", parser);
		assertEquals("|<|e:(wer ry zcv)|>", result);
	}

	@Test
	public void nestedExpr() throws IOException {
		push("(wer (ry zcv) (1 2) kkk)", parser);
		assertEquals("|<|e:(wer (ry zcv) (1 2) kkk)|>", result);
	}

	@Test
	public void tooManyClosingParentheses() throws IOException {
		push("())", parser);
		assertEquals("|<|e:()|!:TOO_MANY_CLOSING_PARENTHESES|>", result);
	}

	@Test
	public void unclosedParentheses() throws IOException {
		push("(", parser);
		assertEquals("|<|!:UNCLOSED_PARENTHESES|>", result);
	}

}