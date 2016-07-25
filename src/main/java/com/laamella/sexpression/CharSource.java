package com.laamella.sexpression;

import java.io.Reader;
import java.io.StringReader;

public class CharSource {
    public static void push(String string, CharSink charSink) throws Exception {
        push(new StringReader(string), charSink);
    }

    public static void push(Reader reader, CharSink charSink) throws Exception {
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
