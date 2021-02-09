package com.laamella.sexpression;

public abstract class SchemaType {
    public final String name;
    public final boolean nameHidden;
    public final boolean required;
    public final boolean repeatable;

    public SchemaType(String name, boolean nameHidden, boolean required, boolean repeatable) {
        this.name = name;
        this.nameHidden = nameHidden;
        this.required = required;
        this.repeatable = repeatable;
    }
}
