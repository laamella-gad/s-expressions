package com.laamella.sexpression;

public class SimpleType extends SchemaType {
    public SimpleType(String name, boolean nameHidden, boolean required, boolean repeatable) {
        super(name, nameHidden, required, repeatable);
    }

    @Override
    public String toString() {
        return "com.laamella.sexpression.SimpleType{" +
                "name='" + name + '\'' +
                ", nameHidden=" + nameHidden +
                ", required=" + required +
                ", repeatable=" + repeatable +
                '}';
    }
}
