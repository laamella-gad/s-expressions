package com.laamella.sexpression.testobjects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddressBook {
    public List<Address> addresses = new ArrayList<>();

    public AddressBook(Address... addresses) {
        this.addresses.addAll(Arrays.asList(addresses));
    }
}
