package com.laamella.sexpression;

import java.util.Collection;

public interface AddressFactoryTemplate<AddressBook, AddressCollection extends Collection<Address>, Address, AddressStreet, AddressNumber> {
    AddressBook createAddressBook(AddressCollection addressCollection);

    AddressCollection createAddressCollection();

    Address createAddress(AddressStreet street, AddressNumber number);

    AddressStreet createAddressStreet(String value);

    AddressNumber createAddressNumber(String value);

    void onError();
}