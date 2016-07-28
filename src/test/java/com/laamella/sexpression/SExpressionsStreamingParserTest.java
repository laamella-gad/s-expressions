package com.laamella.sexpression;

import org.junit.Test;

import static org.junit.Assert.*;

public class SExpressionsStreamingParserTest {
	private String result = "";
	private final SExpressionsStreamingParser parser = new SExpressionsStreamingParser(new SExpressionsStreamingParser.Callback() {
		@Override
		public void onText(String text) {
			result += "|a:" + text;
		}

		@Override
		public void onListBegin() {
			result += "|(";
		}

		@Override
		public void onListEnd() {
			result += "|)";
		}

		@Override
		public void onComment(String comment) {
			result += "|c:" + comment;
		}

		@Override
		public void onError(SExpressionsStreamingParser.Error error) {
			result += "|!:" + error.name();
		}

		@Override
		public void onOpenStream() {
			result += "|<";
		}

		@Override
		public void onCloseStream() {
			result += "|>";
		}
	});

	@Test
	public void lostAtoms() throws Exception {
		CharSource.pushString("wer ry zcv", parser);
		assertEquals("|<|a:wer|a:ry|a:zcv|>", result);
	}

	@Test
	public void oneExpr() throws Exception {
		CharSource.pushString("(wer ry zcv)", parser);
		assertEquals("|<|(|a:wer|a:ry|a:zcv|)|>", result);
	}

	@Test
	public void nestedExpr() throws Exception {
		CharSource.pushString("(wer (ry zcv) (1 2) kkk)", parser);
		assertEquals("|<|(|a:wer|(|a:ry|a:zcv|)|(|a:1|a:2|)|a:kkk|)|>", result);
	}

	@Test
	public void insignificantWhitespace() throws Exception {
		CharSource.pushString("     (   wer           \n\t         kkk)   ", parser);
		assertEquals("|<|(|a:wer|a:kkk|)|>", result);
	}

	@Test
	public void commentOutsideExpressionIsFine() throws Exception {
		CharSource.pushString(";wer ry zcv\n()", parser);
		assertEquals("|<|c:wer ry zcv|(|)|>", result);
	}

	@Test
	public void commentCanHaveWhitespaceInFrontOfIt() throws Exception {
		CharSource.pushString("                 \t\t;wer ry zcv\n()", parser);
		assertEquals("|<|c:wer ry zcv|(|)|>", result);
	}

	@Test
	public void commentInsideExpressionOnALineIsFine() throws Exception {
		CharSource.pushString("(ab\n;wer ry zcv\nbc)", parser);
		assertEquals("|<|(|a:ab|c:wer ry zcv|a:bc|)|>", result);
	}

	@Test
	public void quoteSomeAtoms() throws Exception {
		CharSource.pushString("(ab     \"bc  rr)))\"    )", parser);
		assertEquals("|<|(|a:ab|a:\"bc  rr)))\"|)|>", result);
	}

	@Test
	public void quotedMultiline() throws Exception {
		CharSource.pushString("(\"bc\n\nrr\")", parser);
		assertEquals("|<|(|a:\"bc\n\nrr\"|)|>", result);
	}


	@Test
	public void quotedComment() throws Exception {
		CharSource.pushString("(\"bc\n;hello\nrr\")", parser);
		assertEquals("|<|(|a:\"bc\n;hello\nrr\"|)|>", result);
	}


	@Test
	public void commentedQuote() throws Exception {
		CharSource.pushString(";a \" quote", parser);
		assertEquals("|<|c:a \" quote|>", result);
	}


	@Test
	public void unclosedQuotes() throws Exception {
		CharSource.pushString("\"", parser);
		assertEquals("|<|!:STREAM_ENDED_WHILE_IN_QUOTES|>", result);
	}

}