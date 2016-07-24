package com.laamella.sexpression;

import java.io.Reader;

public class CharSource {
    public static void push(String string, CharSink charSink) throws Exception {
        for (char c : string.toCharArray()) {
            charSink.accept(c);
        }
        charSink.close();
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
