package com.laamella.sexpression;

import com.laamella.sexpression.testobjects.Address;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class SExpressionSerializerTest {
    private final SExpressionSerializer databinder = new SExpressionSerializer();

    @Test
    void testSerializeNull() {
        String serialized = databinder.serialize(null);
        assertEquals("#null", serialized);
    }

    @Test
    void testSerializeInt() {
        String serialized = databinder.serialize(15);
        assertEquals("15", serialized);
    }

    @Test
    void testSerializeAddressObject() {
        String serialized = databinder.serialize(new Address("Nieuweweg", 10));
        assertEquals("((street Nieuweweg) (number 10))", serialized);
    }
}