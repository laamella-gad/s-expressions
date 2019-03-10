package com.laamella.sexpression;

import java.io.Closeable;

import static com.laamella.sexpression.SExpressionsStreamingLexer.LexState.*;

public class SExpressionsStreamingLexer implements CharSink, Closeable {
    enum LexState {
        TEXT, WHITESPACE, OTHER, CLOSED
    }

    private Callback callback;
    private StringBuilder token = new StringBuilder();
    private long tokenStartPos = 0;
    private LexState state = CLOSED;
    private long pos = 0;

    public SExpressionsStreamingLexer(SExpressionsStreamingLexer.Callback callback) {
        setCallback(callback);
    }

    public void setCallback(SExpressionsStreamingLexer.Callback callback) {
        this.callback = callback;
    }

    public void accept(char c) {
        if (c == '\n') {
            inState(OTHER);
            callback.onEndOfLine(pos);
        } else if (Character.isWhitespace(c)) {
            inState(WHITESPACE);
            token.appendCodePoint(c);
        } else switch (c) {
            case ';':
                inState(OTHER);
                callback.onComment(pos);
                break;
            case '(':
                inState(OTHER);
                callback.onOpeningBrace(c, pos);
                break;
            case ')':
                inState(OTHER);
                callback.onClosingBrace(c, pos);
                break;
            case '"':
                inState(OTHER);
                callback.onQuote(c, pos);
                break;
            default:
                inState(TEXT);
                token.appendCodePoint(c);
                break;
        }
        pos++;
    }

    @Override
    public void close() {
        inState(CLOSED);
        callback.onCloseStream();
    }

    private void inState(LexState newState) {
        if (newState != state) {
            if (state == CLOSED) {
                callback.onOpenStream();
                pos = 0;
            }
            if (token.length() > 0) {
                switch (state) {
                    case TEXT:
                        callback.onText(token.toString(), tokenStartPos, pos - 1);
                        break;
                    case WHITESPACE:
                        callback.onWhitespace(token.toString(), tokenStartPos, pos - 1);
                        break;
                }
            }
            token = new StringBuilder();
            tokenStartPos = pos;
            state = newState;
        }
    }

    public interface Callback {

        /**
         * Called for everything that's not in one of the other categories
         */
        void onText(String text, long start, long end);

        /**
         * Whitespace includes tabs and newlines
         */
        void onWhitespace(String whitespace, long start, long end);

        /**
         * Called for (
         */
        void onOpeningBrace(char b, long pos);

        /**
         * Called for )
         */
        void onClosingBrace(char b, long pos);

        /**
         * Called for "
         */
        void onQuote(char q, long pos);

        /**
         * Input ended. Sending more data will open it again.
         */
        void onCloseStream();

        /**
         * Input (re)started.
         */
        void onOpenStream();

        /**
         * Called for comment characters: ;
         */
        void onComment(long pos);

        /**
         * Called on end of line, specifically when encountering the \n character. \r is treated as normal whitespace.
         */
        void onEndOfLine(long pos);
    }
}
