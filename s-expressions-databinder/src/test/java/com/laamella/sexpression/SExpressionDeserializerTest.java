package com.laamella.sexpression;

import com.laamella.sexpression.testobjects.Address;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class SExpressionDeserializerTest {
    @Test
    void aaa() throws IOException {
        SExpressionDeserializer deserializer = new SExpressionDatabinder().createDeserializer();
        Address address = deserializer.deserialize("((street Nieuweweg) (number 10))", Address.class);
        System.out.println(address);
    }
}