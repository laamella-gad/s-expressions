package com.laamella.sexpression;

import com.laamella.sexpression.testobjects.Address;
import com.laamella.sexpression.testobjects.AddressBook;

import java.util.ArrayList;
import java.util.List;

public abstract class AddressFactory implements AddressFactoryTemplate<AddressBook, List<Address>, Address, String, Integer> {
    @Override
    public AddressBook createAddressBook(List<Address> addresses) {
        return new AddressBook(addresses);
    }

    @Override
    public List<Address> createAddressCollection() {
        return new ArrayList<>();
    }

    @Override
    public Address createAddress(String street, Integer number) {
        return new Address(street, number);
    }

    @Override
    public String createAddressStreet(String value) {
        return value;
    }

    @Override
    public Integer createAddressNumber(String value) {
        return Integer.parseInt(value);
    }


}
