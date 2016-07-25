package com.laamella.sexpression;

public class SExpressionsParser implements CharSink {
    private final SExpressionsStreamingParser parser = new SExpressionsStreamingParser(new SExpressionsStreamingParser.Callback() {
        @Override
        public void onAtom(String text) {

        }

        @Override
        public void onExpression(AtomList expression) {

        }

        @Override
        public void onError(Error error) {

        }
    });

    @Override
    public void accept(char c) {
        parser.accept(c);
    }

    @Override
    public void close() throws Exception {
        parser.close();
    }
}
