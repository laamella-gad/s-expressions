package com.laamella.sexpression;

import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

public class CharSource {
    public static <T extends CharSink & Closeable> void push(String string, T charSink) throws IOException {
        push(new StringReader(string), charSink);
    }

    public static <T extends CharSink & Closeable> void push(Reader reader, T charSink) throws IOException {
        while (true) {
            int c = reader.read();
            if (c == -1) {
                charSink.close();
                return;
            }
            charSink.accept((char) c);
        }
    }
}
