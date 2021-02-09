package com.laamella.sexpression.testobjects;

public class Address {
    public final String street;
    public final int number;

    public Address(String street, int number) {
        this.street = street;
        this.number = number;
    }

    @Override
    public String toString() {
        return "Address{" +
                "street='" + street + '\'' +
                ", number=" + number +
                '}';
    }
}
