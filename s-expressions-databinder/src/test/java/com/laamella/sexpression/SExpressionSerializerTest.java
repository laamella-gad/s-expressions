package com.laamella.sexpression;

import com.laamella.sexpression.testobjects.Address;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SExpressionSerializerTest {
    private final SExpressionDatabinder factory = new SExpressionDatabinder();

    @Test
    void testSerializeNull() {
        String serialized = factory.createSerializer().serialize(null);
        assertEquals("", serialized);
    }

    @Test
    void testSerializeInt() {
        String serialized = factory.createSerializer().serialize(15);
        assertEquals("15", serialized);
    }

    @Test
    void testSerializeAddressObject() {
        String serialized = factory.createSerializer().serialize(new Address("Nieuweweg", 10));
        assertEquals("((street Nieuweweg) (number 10))", serialized);
    }

    @Test
    void testSerializeNullStreetObject() {
        String serialized = factory.createSerializer().serialize(new Address(null, 10));
        assertEquals("((number 10))", serialized);
    }

    @Test
    void testCustomSerializeAddressObject() {
        factory.addTypeAdapter(Address.class, (value, generator) -> generator.onText(value.street + ":" + value.number, false), null);
        factory.createSerializer();
        String serialized = factory.createSerializer().serialize(new Address("Nieuweweg", 10));
        assertEquals("Nieuweweg:10", serialized);
    }
}