package com.laamella.sexpression;

import java.util.List;

public class SchemaParser implements SExpressionsToListsParser.Callback {
    private final Callback callback;

    public SchemaParser(Callback callback) {
        this.callback = callback;
    }

    @Override
    public void onResult(List<Object> document) {
        Schema schema = new Schema();

        for (Object topLevelTypeDeclaration : document) {
            evaluate((List<Object>) topLevelTypeDeclaration, schema.typeContainer);
        }
        callback.onResult(schema);
    }

    private void evaluate(List<Object> list, ComplexType parent) {
        String name = (String) list.get(0);
        if (list.size() == 1) {
            parent.subTypes.put(name, new SimpleType(name, true, true, false));
            return;
        }
        ComplexType type = new ComplexType(name, false, false, true);
        parent.subTypes.put(name, type);
        for (int i = 1; i < list.size(); i++) {
            evaluate((List<Object>) list.get(i), type);
        }
    }

    @Override
    public void onError(SExpressionsToListsParser.Error error) {
        callback.onError(error);
    }

    @Override
    public void onOpenStream() {
        callback.onOpenStream();
    }

    @Override
    public void onCloseStream() {
        callback.onCloseStream();
    }

    @Override
    public void onExpression(List<Object> finishedList) {

    }

    public interface Callback {

        void onResult(Schema schema);

        void onError(SExpressionsToListsParser.Error error);

        void onOpenStream();

        void onCloseStream();

        class Adapter implements Callback {
            @Override
            public void onResult(Schema schema) {

            }

            @Override
            public void onError(SExpressionsToListsParser.Error error) {
            }

            @Override
            public void onOpenStream() {
            }

            @Override
            public void onCloseStream() {
            }
        }

        class ResultGrabbingCallback extends Adapter {
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
        }

    }
}
