package com.laamella.sexpression;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SExpressionsToListsParserTest {
    private String stream = "";
    private List<Object> result = null;

    private final SExpressionsStreamingLexer parser =
            new SExpressionsStreamingLexer(
                    new SExpressionsStreamingParser(
                            new SExpressionsToListsParser(new SExpressionsToListsParser.Callback() {
                                @Override
                                public void onExpression(List<Object> expression) {
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
                                public void onResult(List<Object> result) {
                                    stream += "|r:" + result;
                                    SExpressionsToListsParserTest.this.result = result;
                                }

                                @Override
                                public void onError(SExpressionsToListsParser.Error error) {
                                    stream += "|!:" + error.name();
                                }
                            })));

    @Test
    void lostAtoms() {
        CharSource.pushString("wer ry zcv\n;lost comment\n()", parser);
        assertEquals("|<|e:[]|r:[wer, ry, zcv, []]|>", stream);
    }

    @Test
    void oneExpr() {
        CharSource.pushString("(wer ry zcv)", parser);
        assertEquals("|<|e:[wer, ry, zcv]|r:[[wer, ry, zcv]]|>", stream);
    }

    @Test
    void quotedAtomBecomesSingleString() {
        CharSource.pushString("(\"wer ry zcv\")", parser);
        String atom = ((List<Object>) result.get(0)).get(0).toString();
        assertEquals("wer ry zcv", atom);
        assertEquals("|<|e:[wer ry zcv]|r:[[wer ry zcv]]|>", stream);
    }


    @Test
    void nestedExpr() {
        CharSource.pushString("(wer (ry zcv) (1 2) kkk)", parser);
        assertEquals("|<|e:[wer, [ry, zcv], [1, 2], kkk]|r:[[wer, [ry, zcv], [1, 2], kkk]]|>", stream);
    }

    @Test
    void tooManyClosingParentheses() {
        CharSource.pushString("())", parser);
        assertEquals("|<|e:[]|!:TOO_MANY_CLOSING_PARENTHESES|r:[[]]|>", stream);
    }

    @Test
    void unclosedParentheses() {
        CharSource.pushString("(", parser);
        assertEquals("|<|!:UNCLOSED_PARENTHESES|r:[]|>", stream);
    }
}