package com.laamella.sexpression;

import java.io.Reader;

public interface Lexer extends AutoCloseable {
    default void lex(Reader reader) throws Exception {
        while (true) {
            int c = reader.read();
            if (c == -1) {
                close();
                return;
            }
            lex((char) c);
        }
    }


    void lex(char c);
}
