package com.laamella.sexpression;

import com.laamella.sexpression.testobjects.Address;
import com.laamella.sexpression.testobjects.AddressBook;

import java.util.List;

public class Testje {
    public static void main(String[] args) {
        new SExpressionsStreamingLexer(
                new SExpressionsStreamingParser(
                        new AddressBookDeserializer(new AddressFactory() {
                            @Override
                            public AddressBook createAddressBook(List<Address> addresses) {
                                System.out.println(super.createAddressBook(addresses));
                                return null;
                            }

                            @Override
                            public void onError() {
                                System.out.println("OOPSIE");
                            }
                        })))
                .pushString("((Nieuweweg 277)(Oudestraat 15))");
    }
}
