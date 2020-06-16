package com.laamella.sexpression;

import java.io.IOException;
import java.io.Writer;

/**
 * A low-level class for generating s-expression files.
 */
public class SExpressionsStreamingGenerator {

    private final Writer writer;
    private boolean needSpace = false;

    SExpressionsStreamingGenerator(Writer writer) {
        this.writer = writer;
    }

    void onText(String text, boolean quoted) {
        writeSpaceWhenNeeded();
        if (quoted) {
            onQuote();
        }
        try {
            writer.write(text);
            needSpace = true;
        } catch (IOException e) {
            onIOException(e);
        }
        if (quoted) {
            onQuote();
        }
    }

    private void writeSpaceWhenNeeded() {
        if (needSpace) {
            try {
                writer.write(" ");
            } catch (IOException e) {
                onIOException(e);
            }

            needSpace = false;
        }
    }

    void onWhitespace(String whitespace) {
        needSpace = false;
        try {
            writer.write(whitespace);
        } catch (IOException e) {
            onIOException(e);
        }

    }

    void onOpeningBrace() {
        writeSpaceWhenNeeded();
        try {
            writer.write('(');
        } catch (IOException e) {
            onIOException(e);
        }

    }

    void onClosingBrace() {
        needSpace = false;
        try {
            writer.write(')');
            needSpace = true;
        } catch (IOException e) {
            onIOException(e);
        }
    }

    void onQuote() {
        needSpace = false;
        try {
            writer.write('\'');
        } catch (IOException e) {
            onIOException(e);
        }
    }

    void onComment(String comment) {
        writeSpaceWhenNeeded();
        onCommentChar();
        onWhitespace(" ");
        onText(comment, false);
        onEndOfLine();
    }

    void onCommentChar() {
        writeSpaceWhenNeeded();
        try {
            writer.write(';');
        } catch (IOException e) {
            onIOException(e);
        }
    }

    void onEndOfLine() {
        try {
            writer.write('\n');
        } catch (IOException e) {
            onIOException(e);
        }
    }

    private void onIOException(IOException e) {
        throw new RuntimeException(e);
    }
}
