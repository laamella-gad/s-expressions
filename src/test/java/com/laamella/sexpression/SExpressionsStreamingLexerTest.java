package com.laamella.sexpression;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

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
    };

    private final SExpressionsStreamingLexer lexer = new SExpressionsStreamingLexer(c);

    @Test
    public void lexComplexCase() throws Exception {
        CharSource.push("(ae bde c ()() \t[{])[ ", lexer);
        assertEquals("|<|ob:( 0|t:ae 1 2|w:  3 3|t:bde 4 6|w:  7 7|t:c 8 8|w:  9 9|ob:( 10|cb:) 11|ob:( 12|cb:) 13|w: \t 14 15|t:[{] 16 18|cb:) 19|t:[ 20 20|w:  21 21|>", result);
    }

    @Test
    public void lexInitialAtomIsNotWhitespace() throws Exception {
        CharSource.push("ae", lexer);
        assertEquals("|<|t:ae 0 1|>", result);
    }

    @Test
    public void lexReopen() throws Exception {
        CharSource.push("aa", lexer);
        CharSource.push("bb", lexer);
        assertEquals("|<|t:aa 0 1|>|<|t:bb 0 1|>", result);
    }

    @Test
    public void lexComments() throws Exception {
        CharSource.push("aa\n\t; hello\nbbb", lexer);
        assertEquals("|<|t:aa 0 1|w:\n\t 2 3|c:; 4|w:  5 5|t:hello 6 10|w:\n 11 11|t:bbb 12 14|>", result);
    }
}