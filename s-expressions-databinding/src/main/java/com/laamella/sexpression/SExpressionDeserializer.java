package com.laamella.sexpression;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

public class SExpressionDeserializer {
    public Object deserialize(String expr) throws IOException {
        return deserialize(new StringReader(expr));
    }

    public Object deserialize(Reader reader) throws IOException {
        DatabindingCallback callback = new DatabindingCallback();
        SExpressionsStreamingLexer stream = new SExpressionsStreamingLexer(new SExpressionsStreamingParser(callback));
        CharSource.push(reader, stream);
        return callback.object;
    }
}
