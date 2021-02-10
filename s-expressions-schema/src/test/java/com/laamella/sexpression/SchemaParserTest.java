package com.laamella.sexpression;

import com.laamella.sexpression.SchemaParser.Callback.ResultGrabbingCallback;
import org.junit.jupiter.api.Test;

class SchemaParserTest {
    @Test
    void abc() {
        ResultGrabbingCallback schemaCollector = new ResultGrabbingCallback();
        new SExpressionsStreamingLexer(
                new SExpressionsStreamingParser(
                        new SExpressionsToListsParser(
                                new SchemaParser(schemaCollector))))
                .pushString("(piet (a) (b) (c (x) (y) (z)))");
        Schema schema = schemaCollector.getSchema();
        System.out.println(schema);
    }
}
