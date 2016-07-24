package com.laamella.sexpression;

public interface CharSink extends AutoCloseable {
    void accept(char c);
}
