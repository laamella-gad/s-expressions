package com.laamella.sexpression;

import com.laamella.sexpression.testobjects.Address;
import com.laamella.sexpression.testobjects.AddressBook;
import com.laamella.sexpression.testobjects.Index;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class SExpressionSerializerTest {
    private final SExpressionDatabinder binder = new SExpressionDatabinder();

    @Test
    void testSerializeNull() {
        String serialized = binder.createSerializer().serialize(null);
        assertEquals("", serialized);
    }

    @Test
    void testSerializeBoolean() {
        String serialized = binder.createSerializer().serialize(true);
        assertEquals("true", serialized);
    }

    @Test
    void testSerializeByte() {
        String serialized = binder.createSerializer().serialize((byte) 15);
        assertEquals("15", serialized);
    }

    @Test
    void testSerializeChar() {
        String serialized = binder.createSerializer().serialize('x');
        assertEquals("x", serialized);
    }

    @Test
    void testSerializeShort() {
        String serialized = binder.createSerializer().serialize((short) 15);
        assertEquals("15", serialized);
    }

    @Test
    void testSerializeInt() {
        String serialized = binder.createSerializer().serialize(15);
        assertEquals("15", serialized);
    }

    @Test
    void testSerializeLong() {
        String serialized = binder.createSerializer().serialize(15L);
        assertEquals("15", serialized);
    }

    @Test
    void testSerializeFloat() {
        String serialized = binder.createSerializer().serialize(15.15f);
        assertEquals("15.15", serialized);
    }

    @Test
    void testSerializeDouble() {
        String serialized = binder.createSerializer().serialize(15.15);
        assertEquals("15.15", serialized);
    }

    @Test
    void testSerializeAddressObject() {
        String serialized = binder.createSerializer().serialize(new Address("Nieuweweg", 10));
        assertEquals("((street Nieuweweg) (number 10))", serialized);
    }

    @Test
    void testSerializeNullStreetObject() {
        String serialized = binder.createSerializer().serialize(new Address(null, 10));
        assertEquals("((number 10))", serialized);
    }

    @Test
    void testCustomSerializeAddressObject() {
        binder.addTypeAdapter(Address.class, (value, generator) -> generator.onText(value.street + ":" + value.number, false), null);
        String serialized = binder.createSerializer().serialize(new Address("Nieuweweg", 10));
        assertEquals("Nieuweweg:10", serialized);
    }

    @Test
    void testSerializeList() {
        AddressBook addressBook = new AddressBook(new Address("Nieuweweg", 10), new Address("Kade", 99));
        String serialized = binder.createSerializer().serialize(addressBook);
        assertEquals("((addresses (#com.laamella.sexpression.testobjects.Address ((street Nieuweweg) (number 10)) #com.laamella.sexpression.testobjects.Address ((street Kade) (number 99)))))", serialized);
    }

    @Test
    void testSerializeMap() {
        Index index = new Index(new Address("Nieuweweg", 10), new Address("Kade", 99));
        String serialized = binder.createSerializer().serialize(index);
        assertEquals("((addressByStreet ((Kade #com.laamella.sexpression.testobjects.Address ((street Kade) (number 99))) (Nieuweweg #com.laamella.sexpression.testobjects.Address ((street Nieuweweg) (number 10))))))", serialized);
    }
}