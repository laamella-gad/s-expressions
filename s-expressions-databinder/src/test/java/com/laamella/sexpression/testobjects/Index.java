package com.laamella.sexpression.testobjects;

import java.util.HashMap;
import java.util.Map;

public class Index {
    public final Map<String, Address> addressByStreet = new HashMap<>();

    public Index(Address... addresses) {
        for (Address address : addresses) {
            addressByStreet.put(address.street, address);
        }
    }
}
