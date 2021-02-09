package com.laamella.sexpression.testobjects;

import java.time.LocalDate;

public class Person {
    public final String name;
    public final LocalDate birthday;
    public final boolean favourite;
    public final Address address;

    public Person(String name, LocalDate birthday, boolean favourite, Address address) {
        this.name = name;
        this.birthday = birthday;
        this.favourite = favourite;
        this.address = address;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", birthday=" + birthday +
                ", favourite=" + favourite +
                ", address=" + address +
                '}';
    }
}
