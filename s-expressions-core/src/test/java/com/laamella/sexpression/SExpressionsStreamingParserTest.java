package com.laamella.sexpression;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SExpressionsStreamingParserTest {
	private String result = "";

	private final SExpressionsStreamingLexer parser =
			new SExpressionsStreamingLexer(
					new SExpressionsStreamingParser(
							new SExpressionsStreamingParser.Callback() {
								@Override
								public void onText(String text) {
									result += "|t:" + text;
								}

								@Override
								public void onWhitespace(String whitespace) {
									result += "|w:" + whitespace;
								}

								@Override
								public void onEndOfLine() {
									result += "|eol";
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
							}));

	@Test
	public void lostAtoms() {
		CharSource.pushString("wer ry zcv", parser);
		assertEquals("|<|t:wer|w: |t:ry|w: |t:zcv|>", result);
	}

	@Test
	public void oneExpr() {
		CharSource.pushString("(wer ry zcv)", parser);
		assertEquals("|<|(|t:wer|w: |t:ry|w: |t:zcv|)|>", result);
	}

	@Test
	public void nestedExpr() {
		CharSource.pushString("(wer (ry zcv) (1 2) kkk)", parser);
		assertEquals("|<|(|t:wer|w: |(|t:ry|w: |t:zcv|)|w: |(|t:1|w: |t:2|)|w: |t:kkk|)|>", result);
	}

	@Test
	public void insignificantWhitespace() {
		CharSource.pushString("     (   wer           \n\t         kkk)   ", parser);
		assertEquals("|<|w:     |(|w:   |t:wer|w:           |eol|w:\t         |t:kkk|)|w:   |>", result);
	}

	@Test
	public void commentOutsideExpressionIsFine() {
		CharSource.pushString(";wer ry zcv\n()", parser);
		assertEquals("|<|c:wer ry zcv|eol|(|)|>", result);
	}

	@Test
	public void commentCanHaveWhitespaceInFrontOfIt() {
		CharSource.pushString("                 \t\t;wer ry zcv\n()", parser);
		assertEquals("|<|w:                 \t\t|c:wer ry zcv|eol|(|)|>", result);
	}

	@Test
	public void commentInsideExpressionOnALineIsFine() {
		CharSource.pushString("(ab\n;wer ry zcv\nbc)", parser);
		assertEquals("|<|(|t:ab|eol|c:wer ry zcv|eol|t:bc|)|>", result);
	}

	@Test
	public void quoteSomeAtoms() {
		CharSource.pushString("(ab     \"bc  rr)))\"    )", parser);
		assertEquals("|<|(|t:ab|w:     |t:\"bc  rr)))\"|w:    |)|>", result);
	}

	@Test
	public void quotedMultiline() {
		CharSource.pushString("(\"bc\n\nrr\")", parser);
		assertEquals("|<|(|t:\"bc\n\nrr\"|)|>", result);
	}


	@Test
	public void quotedComment() {
		CharSource.pushString("(\"bc\n;hello\nrr\")", parser);
		assertEquals("|<|(|t:\"bc\n;hello\nrr\"|)|>", result);
	}


	@Test
	public void commentedQuote() {
		CharSource.pushString(";a \" quote", parser);
		assertEquals("|<|c:a \" quote|>", result);
	}


	@Test
	public void unclosedQuotes() {
		CharSource.pushString("\"", parser);
		assertEquals("|<|!:STREAM_ENDED_WHILE_IN_QUOTES|>", result);
	}

}