package com.laamella.sexpression;

import java.io.IOException;
import java.io.Writer;

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
            throw new RuntimeException(e);
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
                throw new RuntimeException(e);
            }

            needSpace = false;
        }
    }

    void onWhitespace(String whitespace) {
        needSpace = false;
        try {
            writer.write(whitespace);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    void onOpeningBrace() {
        writeSpaceWhenNeeded();
        try {
            writer.write('(');
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    void onClosingBrace() {
        needSpace = false;
        try {
            writer.write(')');
            needSpace = true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void onQuote() {
        needSpace = false;
        try {
            writer.write('\'');
        } catch (IOException e) {
            throw new RuntimeException(e);
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
            throw new RuntimeException(e);
        }
    }

    void onEndOfLine() {
        try {
            writer.write('\n');
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
