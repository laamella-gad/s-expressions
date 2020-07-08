package com.laamella.sexpression;

import java.util.Collection;

class AddressBookDeserializer implements SExpressionsStreamingParser.Callback {
    private final AddressFactoryTemplate<Object, Collection<Object>, Object, Object, Object> factory;
    private Object addressStreet = null;
    private Object addressNumber;
    private Collection<Object> addressCollection;
    private Object addressBook;

    public AddressBookDeserializer(AddressFactoryTemplate factory) {
        this.factory = factory;
    }

    private enum State {
        ROOT, ADDRESS_STREET, ADDRESS_NUMBER, END_OF_ADDRESS, ADDRESS_COLLECTION
    }

    private State state = State.ROOT;

    @Override
    public void onText(String text) {
        switch (state) {
            case ADDRESS_STREET:
                addressStreet = factory.createAddressStreet(text);
                state = State.ADDRESS_NUMBER;
                break;
            case ADDRESS_NUMBER:
                addressNumber = factory.createAddressNumber(text);
                state = State.END_OF_ADDRESS;
                break;
            default:
                factory.onError();
        }
    }

    @Override
    public void onWhitespace(String whitespace) {

    }

    @Override
    public void onEndOfLine() {

    }

    @Override
    public void onListBegin() {
        switch (state) {
            case ROOT:
                addressCollection = factory.createAddressCollection();
                state = State.ADDRESS_COLLECTION;
                break;
            case ADDRESS_COLLECTION:
                state = State.ADDRESS_STREET;
                break;
            case ADDRESS_STREET:
                state = State.ADDRESS_NUMBER;
                break;
        }
    }

    @Override
    public void onListEnd() {
        switch (state) {
            case END_OF_ADDRESS:
                Object address = factory.createAddress(addressStreet, addressNumber);
                addressCollection.add(address);
                state = State.ADDRESS_COLLECTION;
                break;
            case ADDRESS_COLLECTION:
                addressBook = factory.createAddressBook(addressCollection);
                state = State.ROOT;
                break;
            default:
                factory.onError();
        }
    }

    @Override
    public void onComment(String comment) {

    }

    @Override
    public void onError(SExpressionsStreamingParser.Error error) {

    }

    @Override
    public void onOpenStream() {

    }

    @Override
    public void onCloseStream() {

    }
}
