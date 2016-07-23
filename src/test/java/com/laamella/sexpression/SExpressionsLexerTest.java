package com.laamella.sexpression;

import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

import static org.junit.Assert.assertEquals;

public class SExpressionsLexerTest {
    private String result = "";

    private final SExpressionsLexer.Callback c = new SExpressionsLexer.Callback() {
        @Override
        public void onAtom(String atom, long start, long end) {
            result += "|a:" + atom + " " + start + " " + end;
        }

        @Override
        public void onWhitespace(String whitespace, long start, long end) {
            result += "|w:" + whitespace + " " + start + " " + end;
        }

        @Override
        public void onOpenBrace(char b, long pos) {
            result += "|ob:" + b + " " + pos;
        }

        @Override
        public void onCloseBrace(char b, long pos) {
            result += "|cb:" + b + " " + pos;
        }

        @Override
        public void onQuote(char q, long pos) {
            result += "|q:" + q + " " + pos;
        }

        @Override
        public void onClose() {
            result += "|c";
        }

        @Override
        public void onOpen() {
            result+="|o";
        }
    };

    private final SExpressionsLexer lexer = new SExpressionsLexer(c);

    @Test
    public void lexComplexCase() throws IOException {
        lexer.lex(new StringReader("(ae bde c ()() \t[{])[ "));
        assertEquals("|o|ob:( 0|a:ae 1 2|w:  3 3|a:bde 4 6|w:  7 7|a:c 8 8|w:  9 9|ob:( 10|cb:) 11|ob:( 12|cb:) 13|w: \t 14 15|ob:[ 16|ob:{ 17|cb:] 18|cb:) 19|ob:[ 20|w:  21 21|c", result);
    }

    @Test
    public void lexInitialAtomIsNotWhitespace() throws IOException {
        lexer.lex(new StringReader("ae"));
        assertEquals("|o|a:ae 0 1|c", result);
    }

    @Test
    public void lexReopen() throws IOException {
        lexer.lex(new StringReader("aa"));
        lexer.lex(new StringReader("bb"));
        assertEquals("|o|a:aa 0 1|c|o|a:bb 0 1|c", result);
    }
}