package com.laamella.sexpression;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SExpressionsStreamingLexerTest {
	private String result = "";

	private final SExpressionsStreamingLexer.Callback c = new SExpressionsStreamingLexer.Callback() {
		@Override
		public void onText(String text, long start, long end) {
			result += "|t:" + text + " " + start + " " + end;
		}

		@Override
		public void onWhitespace(String whitespace, long start, long end) {
			result += "|w:" + whitespace + " " + start + " " + end;
		}

		@Override
		public void onOpeningBrace(char b, long pos) {
			result += "|ob:" + b + " " + pos;
		}

		@Override
		public void onClosingBrace(char b, long pos) {
			result += "|cb:" + b + " " + pos;
		}

		@Override
		public void onQuote(char q, long pos) {
			result += "|q:" + q + " " + pos;
		}

		@Override
		public void onCloseStream() {
			result += "|>";
		}

		@Override
		public void onOpenStream() {
			result += "|<";
		}

		@Override
		public void onComment(char c, long pos) {
			result += "|c:" + c + " " + pos;
		}

		@Override
		public void onEndOfLine(long pos) {
			result += "|eol " + pos;
		}
	};

	private final SExpressionsStreamingLexer lexer = new SExpressionsStreamingLexer(c);

	@Test
	public void lexComplexCase() {
		CharSource.pushString("(ae bde c ()() \t[{])[ ", lexer);
		assertEquals("|<|ob:( 0|t:ae 1 2|w:  3 3|t:bde 4 6|w:  7 7|t:c 8 8|w:  9 9|ob:( 10|cb:) 11|ob:( 12|cb:) 13|w: \t 14 15|t:[{] 16 18|cb:) 19|t:[ 20 20|w:  21 21|>", result);
	}

	@Test
	public void lexInitialAtomIsNotWhitespace() {
		CharSource.pushString("ae", lexer);
		assertEquals("|<|t:ae 0 1|>", result);
	}

	@Test
	public void lexReopen() {
		CharSource.pushString("aa", lexer);
		CharSource.pushString("bb", lexer);
		assertEquals("|<|t:aa 0 1|>|<|t:bb 0 1|>", result);
	}

	@Test
	public void lexComments() {
		CharSource.pushString("aa\n\t; hello\nbbb", lexer);
		assertEquals("|<|t:aa 0 1|eol 2|w:\t 3 3|c:; 4|w:  5 5|t:hello 6 10|eol 11|t:bbb 12 14|>", result);
	}
}