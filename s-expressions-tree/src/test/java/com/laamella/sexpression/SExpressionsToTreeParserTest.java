package com.laamella.sexpression;

import com.laamella.sexpression.model.Atom;
import com.laamella.sexpression.model.AtomList;
import com.laamella.sexpression.model.Document;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SExpressionsToTreeParserTest {
    private String stream = "";
    private Document document = null;

    private final SExpressionsStreamingLexer parser =
            new SExpressionsStreamingLexer(
                    new SExpressionsStreamingParser(
                            new SExpressionsToTreeParser(new SExpressionsToTreeParser.Callback() {
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
                                    SExpressionsToTreeParserTest.this.document = document;
                                }

                                @Override
                                public void onError(SExpressionsToTreeParser.Error error) {
                                    stream += "|!:" + error.name();
                                }
                            })));

    @Test
    public void lostAtoms() {
        parser.pushString("wer ry zcv\n;lost comment\n()");
        assertEquals("|<|e:()|d:wer ry zcv ()|>", stream);
    }

    @Test
    public void oneExpr() {
        parser.pushString("(wer ry zcv)");
        assertEquals("|<|e:(wer ry zcv)|d:(wer ry zcv)|>", stream);
    }

    @Test
    public void atomWithWhitespaceGetsQuoted() {
        parser.pushString("(\"wer ry zcv\")");
        Atom atom = document.asList().get(0).asList().get(0).asAtom();
        assertEquals("wer ry zcv", atom.value());
        assertEquals("|<|e:(\"wer ry zcv\")|d:(\"wer ry zcv\")|>", stream);
    }

    @Test
    public void atomWithBinaryDataGetsBase64Encoded() {
        parser.pushString("(abc |AAECAwQFBg==| abc)");
        Atom atom = document.asList().get(0).asList().get(1).asAtom();
        assertArrayEquals(new byte[]{0, 1, 2, 3, 4, 5, 6}, atom.data);
        assertEquals("|<|e:(abc |AAECAwQFBg==| abc)|d:(abc |AAECAwQFBg==| abc)|>", stream);
    }


    @Test
    public void nestedExpr() {
        parser.pushString("(wer (ry zcv) (1 2) kkk)");
        assertEquals("|<|e:(wer (ry zcv) (1 2) kkk)|d:(wer (ry zcv) (1 2) kkk)|>", stream);
    }

    @Test
    public void tooManyClosingParentheses() {
        parser.pushString("())");
        assertEquals("|<|e:()|!:TOO_MANY_CLOSING_PARENTHESES|d:()|>", stream);
    }

    @Test
    public void unclosedParentheses() {
        parser.pushString("(");
        assertEquals("|<|!:UNCLOSED_PARENTHESES|d:|>", stream);
    }

}