package com.laamella.sexpression.testobjects;

import java.util.List;

public class AddressBook {
    public List<Address> addresses;

    public AddressBook(List<Address> addresses) {
        this.addresses = addresses;
    }

    @Override
    public String toString() {
        return "AddressBook{" +
                "addresses=" + addresses +
                '}';
    }
}
