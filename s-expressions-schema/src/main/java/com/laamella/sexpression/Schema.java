package com.laamella.sexpression;

public class Schema {
    public final ComplexType typeContainer = new ComplexType("root", true, true, true);

    @Override
    public String toString() {
        return "com.laamella.sexpression.Schema{" +
                "rootType=" + typeContainer +
                '}';
    }
}
