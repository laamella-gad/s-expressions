package com.laamella.sexpression;

// TODO some kind of generic result type?
public class SchemaCollector implements SchemaParser.Callback {
    private Schema schema = null;

    public Schema getSchema() {
        return schema;
    }

    @Override
    public void onResult(Schema schema) {
        this.schema = schema;
    }

    @Override
    public void onError(SExpressionsToListsParser.Error error) {
        throw new RuntimeException(error.toString());
    }

    @Override
    public void onOpenStream() {

    }

    @Override
    public void onCloseStream() {

    }
}
