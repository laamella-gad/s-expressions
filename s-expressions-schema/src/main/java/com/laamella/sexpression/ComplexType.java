package com.laamella.sexpression;

import java.util.HashMap;
import java.util.HashMap;
import java.util.Map;

public class ComplexType extends SchemaType {
    public final Map<String, SchemaType> subTypes = new HashMap<>();

    public ComplexType(String name, boolean nameHidden, boolean required, boolean repeatable) {
        super(name, nameHidden, required, repeatable);
    }

    @Override
    public String toString() {
        return "ComplexType{" +
                "subTypes=" + subTypes +
                ", name='" + name + '\'' +
                ", nameHidden=" + nameHidden +
                ", required=" + required +
                ", repeatable=" + repeatable +
                '}';
    }
}
