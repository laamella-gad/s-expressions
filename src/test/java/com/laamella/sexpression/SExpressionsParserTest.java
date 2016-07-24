package com.laamella.sexpression;

import org.junit.Test;

import static com.laamella.sexpression.CharSource.push;
import static org.junit.Assert.assertEquals;

public class SExpressionsParserTest {
    private String result = "";
    private final SExpressionsParser parser = new SExpressionsParser(new SExpressionsParser.Callback() {
        @Override
        public void onAtom(String text) {
            result += "|a:" + text;
        }

        @Override
        public void onExpression(AtomList expression) {
            result += "|e:" + expression.toString();
        }

        @Override
        public void onError(Error error) {
            result += "|!:" + error.name();
        }
    });

    @Test
    public void lostAtoms() throws Exception {
        push("wer ry zcv", parser);
        assertEquals("|a:wer|a:ry|a:zcv", result);
    }

    @Test
    public void oneExpr() throws Exception {
        push("(wer ry zcv)", parser);
        assertEquals("|e:(wer ry zcv)", result);
    }

    @Test
    public void nestedExpr() throws Exception {
        push("(wer (ry zcv) (1 2) kkk)", parser);
        assertEquals("|e:(wer (ry zcv) (1 2) kkk)", result);
    }

    @Test
    public void insignificantWhitespace() throws Exception {
        push("     (   wer           \n\t         kkk)   ", parser);
        assertEquals("|e:(wer kkk)", result);
    }

    @Test
    public void tooManyClosingParentheses() throws Exception {
        push("())", parser);
        assertEquals("|e:()|!:TOO_MANY_CLOSING_PARENTHESES", result);
    }

    @Test
    public void unclosedParentheses() throws Exception {
        push("(", parser);
        assertEquals("|!:UNCLOSED_PARENTHESES", result);
    }

    @Test
    public void commentOutsideExpressionIsFine() throws Exception {
        push(";wer ry zcv\n()", parser);
        assertEquals("|e:()", result);
    }

    @Test
    public void commentCanHaveWhitespaceInFrontOfIt() throws Exception {
        push("                 \t\t;wer ry zcv\n()", parser);
        assertEquals("|e:()", result);
    }

    @Test
    public void commentInsideExpressionOnALineIsFine() throws Exception {
        push("(ab\n;wer ry zcv\nbc)", parser);
        assertEquals("|e:(ab bc)", result);
    }

    @Test
    public void quoteSomeAtoms() throws Exception {
        push("(ab     \"bc  rr)))\"    )", parser);
        assertEquals("|e:(ab bc  rr))))", result);
    }

    @Test
    public void quotedMultiline() throws Exception {
        push("(\"bc\n\nrr\")", parser);
        assertEquals("|e:(bc\n\nrr)", result);
    }


    @Test
    public void quotedComment() throws Exception {
        push("(\"bc\n;hello\nrr\")", parser);
        assertEquals("|e:(bc\n;hello\nrr)", result);
    }


    @Test
    public void commentedQuote() throws Exception {
        push(";a \" quote", parser);
        assertEquals("", result);
    }


    @Test
    public void unclosedQuotes() throws Exception {
        push("\"", parser);
        assertEquals("|!:STREAM_ENDED_WHILE_IN_QUOTES", result);
    }

}