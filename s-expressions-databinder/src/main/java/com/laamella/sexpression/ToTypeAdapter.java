package com.laamella.sexpression;

@FunctionalInterface
public interface ToTypeAdapter<T> {
    T deserialize(String text);
}
