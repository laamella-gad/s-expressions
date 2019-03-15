package com.laamella.sexpression;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SExpressionsStreamingParserTest {
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
    void lostAtoms() {
        parser.pushString("wer ry zcv");
        assertEquals("|<|t:wer|w: |t:ry|w: |t:zcv|>", result);
    }

    @Test
    void oneExpr() {
        parser.pushString("(wer ry zcv)");
        assertEquals("|<|(|t:wer|w: |t:ry|w: |t:zcv|)|>", result);
    }

    @Test
    void nestedExpr() {
        parser.pushString("(wer (ry zcv) (1 2) kkk)");
        assertEquals("|<|(|t:wer|w: |(|t:ry|w: |t:zcv|)|w: |(|t:1|w: |t:2|)|w: |t:kkk|)|>", result);
    }

    @Test
    void insignificantWhitespace() {
        parser.pushString("     (   wer           \n\t         kkk)   ");
        assertEquals("|<|w:     |(|w:   |t:wer|w:           |eol|w:\t         |t:kkk|)|w:   |>", result);
    }

    @Test
    void commentOutsideExpressionIsFine() {
        parser.pushString(";wer ry zcv\n()");
        assertEquals("|<|c:wer ry zcv|eol|(|)|>", result);
    }

    @Test
    void commentCanHaveWhitespaceInFrontOfIt() {
        parser.pushString("                 \t\t;wer ry zcv\n()");
        assertEquals("|<|w:                 \t\t|c:wer ry zcv|eol|(|)|>", result);
    }

    @Test
    void commentInsideExpressionOnALineIsFine() {
        parser.pushString("(ab\n;wer ry zcv\nbc)");
        assertEquals("|<|(|t:ab|eol|c:wer ry zcv|eol|t:bc|)|>", result);
    }

    @Test
    void quoteSomeAtoms() {
        parser.pushString("(ab     \"bc  rr)))\"    )");
        assertEquals("|<|(|t:ab|w:     |t:\"bc  rr)))\"|w:    |)|>", result);
    }

    @Test
    void quotedMultiline() {
        parser.pushString("(\"bc\n\nrr\")");
        assertEquals("|<|(|t:\"bc\n\nrr\"|)|>", result);
    }


    @Test
    void quotedComment() {
        parser.pushString("(\"bc\n;hello\nrr\")");
        assertEquals("|<|(|t:\"bc\n;hello\nrr\"|)|>", result);
    }


    @Test
    void commentedQuote() {
        parser.pushString(";a \" quote");
        assertEquals("|<|c:a \" quote|>", result);
    }


    @Test
    void unclosedQuotes() {
        parser.pushString("\"");
        assertEquals("|<|!:STREAM_ENDED_WHILE_IN_QUOTES|>", result);
    }

}