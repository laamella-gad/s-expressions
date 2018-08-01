package com.laamella.sexpression;

import com.laamella.sexpression.model.Atom;
import com.laamella.sexpression.model.AtomList;
import com.laamella.sexpression.model.Document;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SExpressionsParserTest {
	private String stream = "";
	private Document document = null;

	private final SExpressionsStreamingLexer parser =
			new SExpressionsStreamingLexer(
					new SExpressionsStreamingParser(
							new SExpressionsParser(new SExpressionsParser.Callback() {
								@Override
								public void onExpression(AtomList expression) {
									stream += "|e:" + expression.toString();
								}

								@Override
								public void onOpenStream() {
									stream += "|<";
								}

								@Override
								public void onCloseStream() {
									stream += "|>";
								}

								@Override
								public void onDocument(Document document) {
									stream += "|d:" + document;
									SExpressionsParserTest.this.document = document;
								}

								@Override
								public void onError(SExpressionsParser.Error error) {
									stream += "|!:" + error.name();
								}
							})));

	@Test
	public void lostAtoms() {
		CharSource.pushString("wer ry zcv\n;lost comment\n()", parser);
		assertEquals("|<|e:()|d:wer ry zcv ()|>", stream);
	}

	@Test
	public void oneExpr() {
		CharSource.pushString("(wer ry zcv)", parser);
		assertEquals("|<|e:(wer ry zcv)|d:(wer ry zcv)|>", stream);
	}

	@Test
	public void atomWithWhitespaceGetsQuoted() {
		CharSource.pushString("(\"wer ry zcv\")", parser);
		Atom atom = document.asVector().get(0).asVector().get(0).asAtom();
		assertEquals("wer ry zcv", atom.value());
		assertEquals("|<|e:(\"wer ry zcv\")|d:(\"wer ry zcv\")|>", stream);
	}

	@Test
	public void atomWithBinaryDataGetsBase64Encoded() {
		CharSource.pushString("(abc |AAECAwQFBg==| abc)", parser);
		Atom atom = document.asVector().get(0).asVector().get(1).asAtom();
		assertArrayEquals(new byte[]{0, 1, 2, 3, 4, 5, 6}, atom.data);
		assertEquals("|<|e:(abc |AAECAwQFBg==| abc)|d:(abc |AAECAwQFBg==| abc)|>", stream);
	}


	@Test
	public void nestedExpr() {
		CharSource.pushString("(wer (ry zcv) (1 2) kkk)", parser);
		assertEquals("|<|e:(wer (ry zcv) (1 2) kkk)|d:(wer (ry zcv) (1 2) kkk)|>", stream);
	}

	@Test
	public void tooManyClosingParentheses() {
		CharSource.pushString("())", parser);
		assertEquals("|<|e:()|!:TOO_MANY_CLOSING_PARENTHESES|d:()|>", stream);
	}

	@Test
	public void unclosedParentheses() {
		CharSource.pushString("(", parser);
		assertEquals("|<|!:UNCLOSED_PARENTHESES|d:|>", stream);
	}

}