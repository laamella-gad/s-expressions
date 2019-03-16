package com.laamella.sexpression;

import com.laamella.sexpression.testobjects.Address;
import com.laamella.sexpression.testobjects.AddressBook;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;

import static java.lang.Math.*;
import static org.junit.jupiter.api.Assertions.*;

class SExpressionDeserializerTest {

    private final SExpressionDatabinder binder = new SExpressionDatabinder();


    @Test
    void testDeserializeNull() throws IOException {
        SExpressionDeserializer deserializer = binder.createDeserializer();
        Address address = deserializer.deserialize("", Address.class);
        assertNull(address);
    }

    @Test
    void testDeserializeBoolean() throws IOException {
        SExpressionDeserializer deserializer = binder.createDeserializer();
        boolean nr = deserializer.deserialize("true", boolean.class);
        assertTrue(nr);
    }

    @Test
    void testDeserializeByte() throws IOException {
        SExpressionDeserializer deserializer = binder.createDeserializer();
        byte nr = deserializer.deserialize("15", byte.class);
        assertEquals(15, nr);
    }

    @Test
    void testDeserializeChar() throws IOException {
        SExpressionDeserializer deserializer = binder.createDeserializer();
        char nr = deserializer.deserialize("x", char.class);
        assertEquals('x', nr);
    }

    @Test
    void testDeserializeShort() throws IOException {
        SExpressionDeserializer deserializer = binder.createDeserializer();
        short nr = deserializer.deserialize("15", short.class);
        assertEquals(15, nr);
    }

    @Test
    void testDeserializeInt() throws IOException {
        SExpressionDeserializer deserializer = binder.createDeserializer();
        int nr = deserializer.deserialize("15", int.class);
        assertEquals(15, nr);
    }

    @Test
    void testDeserializeLong() throws IOException {
        SExpressionDeserializer deserializer = binder.createDeserializer();
        long nr = deserializer.deserialize("15", long.class);
        assertEquals(15, nr);
    }

    @Test
    void testDeserializeFloat() throws IOException {
        SExpressionDeserializer deserializer = binder.createDeserializer();
        float nr = deserializer.deserialize("15.15", float.class);
        assertTrue(abs(15.15 - nr) < 0.1);
    }

    @Test
    void testDeserializeDouble() throws IOException {
        SExpressionDeserializer deserializer = binder.createDeserializer();
        double nr = deserializer.deserialize("15.15", double.class);
        assertEquals(15.15, nr);
    }

    @Test
    void testDeserializeInteger() throws IOException {
        SExpressionDeserializer deserializer = binder.createDeserializer();
        int nr = deserializer.deserialize("15", Integer.class);
        assertEquals(15, nr);
    }

    @Test
    void testDeserializeAddressObject() throws IOException {
        SExpressionDeserializer deserializer = binder.createDeserializer();
        Address address = deserializer.deserialize("((street Nieuweweg) (number 10))", Address.class);
        System.out.println(address);
    }

    @Test
    void testCustomDeserializeAddressObject() throws IOException {
        binder.addTypeAdapter(Address.class, null, text -> {
            String[] parts = text.split(":");
            return new Address(parts[0], Integer.parseInt(parts[1]));
        });
        Address address = binder.createDeserializer().deserialize("Nieuweweg:10", Address.class);
        assertEquals("Nieuweweg", address.street);
        assertEquals(10, address.number);
    }

    @Test
    void testDeserializeList() throws IOException {
//        AddressBook addressBook = binder.createDeserializer().deserialize("(a b c)", AddressBook.class);
//        String serialized = binder.createSerializer().serialize(list);
//        assertEquals("[\"a\", \"b\", \"c\"]", serialized);
    }

    @Test
    void testDeserializeMap() throws IOException {
//        HashMap map = binder.createDeserializer().deserialize("((a A) (b B) (c C))", HashMap.class);
//        
//
//        Map<String, String> map = new HashMap<>();
//        map.put("a", "A");
//        map.put("b", "B");
//        map.put("c", "C");
//        String serialized = binder.createSerializer().serialize(map);
//        assertEquals(, serialized);
    }

}