package com.laamella.sexpression;

@FunctionalInterface
interface FromTypeAdapter<T> {
    void serialize(T value, SExpressionsStreamingGenerator generator);
}
