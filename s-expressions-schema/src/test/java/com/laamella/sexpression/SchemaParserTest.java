package com.laamella.sexpression;

import org.junit.jupiter.api.Test;

class SchemaParserTest {
    @Test
    void abc() {
        SchemaCollector schemaCollector = new SchemaCollector();
        new SExpressionsStreamingLexer(
                new SExpressionsStreamingParser(
                        new SExpressionsToListsParser(
                                new SchemaParser(schemaCollector))))
                .pushString("(piet (a) (b) (c (x) (y) (z)))");
        Schema schema = schemaCollector.getSchema();
        System.out.println(schema);
    }
}
