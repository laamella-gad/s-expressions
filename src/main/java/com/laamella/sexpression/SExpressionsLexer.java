package com.laamella.sexpression;

import java.io.IOException;
import java.io.Reader;

import static com.laamella.sexpression.LexState.*;

enum LexState {
    TEXT, WHITESPACE, OTHER, CLOSED
}

public class SExpressionsLexer implements AutoCloseable {
    private final Callback callback;
    private StringBuilder token = new StringBuilder();
    private long tokenStartPos = 0;
    private LexState state = CLOSED;
    private long pos = 0;

    public SExpressionsLexer(SExpressionsLexer.Callback callback) {
        this.callback = callback;
    }

    public void lex(Reader reader) throws IOException {
        while (true) {
            int c = reader.read();
            if (c == -1) {
                close();
                return;
            }
            lex((char) c);
        }
    }

    public void lex(char c) {
        switch (c) {
            case '#':
            case ';':
                inState(OTHER);
                callback.onComment(c, pos);
                break;
            case '(':
            case '[':
            case '{':
                inState(OTHER);
                callback.onOpenBrace(c, pos);
                break;
            case ')':
            case ']':
            case '}':
                inState(OTHER);
                callback.onCloseBrace(c, pos);
                break;
            case '"':
            case '\'':
                inState(OTHER);
                callback.onQuote(c, pos);
                break;
            case ' ':
            case '\t':
            case '\r':
            case '\n':
                inState(WHITESPACE);
                token.appendCodePoint(c);
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
        callback.onClose();
    }

    private void inState(LexState newState) {
        if (newState != state) {
            if (state == CLOSED) {
                callback.onOpen();
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
         * Called for these kinds of braces: ( { [
         */
        void onOpenBrace(char b, long pos);

        /**
         * Called for these kinds of braces: ) } ]
         */
        void onCloseBrace(char b, long pos);

        /**
         * Called for " '
         */
        void onQuote(char q, long pos);

        /**
         * Input ended. Sending more data will open it again.
         */
        void onClose();

        /**
         * Input (re)started.
         */
        void onOpen();

        /**
         * Called for potential comment characters: # ;
         */
        void onComment(char c, long pos);
    }
}
