package com.laamella.sexpression;

import java.io.*;
import java.nio.charset.Charset;

public interface CharSource {
    Charset UTF8 = Charset.forName("utf-8");

    static <T extends CharSink & Closeable> void push(Reader reader, T charSink) throws IOException {
        while (true) {
            int c = reader.read();
            if (c == -1) {
                charSink.close();
                return;
            }
            charSink.accept((char) c);
        }
    }

    static <T extends CharSink & Closeable> void pushResource(String resourceName, Charset encoding, T charSink) throws IOException {
        try (
                InputStream resourceAsStream = CharSource.class.getResourceAsStream(resourceName);
                InputStreamReader reader = new InputStreamReader(resourceAsStream, encoding);
        ) {
            push(reader, charSink);
        }
    }

    static <T extends CharSink & Closeable> void pushString(String string, T charSink) {
        for (char c : string.toCharArray()) {
            charSink.accept(c);
        }
        try {
            charSink.close();
        } catch (IOException e) {
            // This is very improbable: make it a fatal error
            throw new RuntimeException(e);
        }
    }
}
