package com.laamella.sexpression;

import java.io.Closeable;
import java.io.IOException;

public class SExpressionsStreamingParser implements CharSink, Closeable {
    private final SExpressionsStreamingLexer.Callback lexerCallback = new SExpressionsStreamingLexer.Callback() {
        private boolean inQuotes;
        private StringBuilder accumulator;
        private boolean inComment;
        private boolean nothingButWhitespaceOnThisNewLine;

        @Override
        public void onText(String text, long start, long end) {
            if (inComment) {
                accumulator.append(text);
                return;
            }
            nothingButWhitespaceOnThisNewLine = false;
            if (inQuotes) {
                accumulator.append(text);
            } else {
                callback.onAtom(text);
            }
        }

        @Override
        public void onWhitespace(String whitespace, long start, long end) {
            if (inComment) {
                accumulator.append(whitespace);
                return;
            }
            if (inQuotes) {
                accumulator.append(whitespace);
            }
        }

        @Override
        public void onOpeningBrace(char b, long pos) {
            if (inComment) {
                accumulator.appendCodePoint(b);
                return;
            }
            nothingButWhitespaceOnThisNewLine = false;
            if (inQuotes) {
                accumulator.appendCodePoint(b);
            } else {
                callback.onListBegin();
            }
        }

        @Override
        public void onClosingBrace(char b, long pos) {
            if (inComment) {
                accumulator.appendCodePoint(b);
                return;
            }
            nothingButWhitespaceOnThisNewLine = false;
            if (inQuotes) {
                accumulator.appendCodePoint(b);
            } else {
                callback.onListEnd();
            }
        }

        @Override
        public void onQuote(char q, long pos) {
            if (inComment) {
                accumulator.appendCodePoint(q);
                return;
            }
            nothingButWhitespaceOnThisNewLine = false;
            if (inQuotes) {
                callback.onAtom(accumulator.toString());
                accumulator = new StringBuilder();
                inQuotes = false;
            } else {
                inQuotes = true;
            }

        }

        @Override
        public void onCloseStream() {
            if (inQuotes) {
                callback.onError(Error.STREAM_ENDED_WHILE_IN_QUOTES);
            }
            if (inComment) {
                callback.onComment(accumulator.toString().trim());
            }
            callback.onCloseStream();
        }

        @Override
        public void onOpenStream() {
            inQuotes = false;
            accumulator = new StringBuilder();
            inComment = false;
            nothingButWhitespaceOnThisNewLine = true;
            callback.onOpenStream();
        }

        @Override
        public void onComment(char c, long pos) {
            if (inComment) {
                accumulator.appendCodePoint(c);
                return;
            }
            if (inQuotes) {
                accumulator.append(c);
            } else if (nothingButWhitespaceOnThisNewLine) {
                inComment = true;
            }
        }

        @Override
        public void onEndOfLine(long pos) {
            if (inQuotes) {
                accumulator.append("\n");
            }
            if (inComment) {
                callback.onComment(accumulator.toString().trim());
                accumulator = new StringBuilder();
            }
            nothingButWhitespaceOnThisNewLine = true;
            inComment = false;
        }
    };

    private final SExpressionsStreamingLexer lexer = new SExpressionsStreamingLexer(lexerCallback);

    private final Callback callback;

    public SExpressionsStreamingParser(Callback callback) {
        this.callback = callback;
    }

    @Override
    public void accept(char c) {
        lexer.accept(c);
    }

    @Override
    public void close() throws IOException {
        lexer.close();
    }

    public enum Error {STREAM_ENDED_WHILE_IN_QUOTES}

    public interface Callback {

        void onAtom(String text);

        void onListBegin();

        void onListEnd();

        void onComment(String comment);

        void onError(Error error);

        void onOpenStream();

        void onCloseStream();
    }
}
