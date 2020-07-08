package com.laamella.sexpression;

import java.io.*;
import java.nio.charset.Charset;

/**
 * Something that wants char-by-char input pushed into it.
 */
public interface CharSink extends Closeable {
    void push(char c);

    default void push(Reader reader) throws IOException {
        try {
            while (true) {
                int c = reader.read();
                if (c == -1) {
                    close();
                    return;
                }
                push((char) c);
            }
        } finally {
            reader.close();
        }
    }

    default void pushResource(String resourceName, Charset encoding) throws IOException {
        try (InputStream resourceAsStream = CharSink.class.getResourceAsStream(resourceName);
             InputStreamReader reader = new InputStreamReader(resourceAsStream, encoding)) {
            push(reader);
        }
    }

    default void pushString(String string) {
        for (char c : string.toCharArray()) {
            push(c);
        }
        try {
            close();
        } catch (IOException e) {
            // This is very improbable: make it a fatal error
            throw new RuntimeException(e);
        }
    }
}
