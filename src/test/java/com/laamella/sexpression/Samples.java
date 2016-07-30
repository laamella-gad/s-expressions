package com.laamella.sexpression;

import com.laamella.sexpression.properties.SProperties;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

public class Samples {
    @Test
    public void configurationFile() throws IOException {
        SProperties properties = new SProperties();
        properties.loadResource("/config.s");

        properties.get("application.window.height").ifPresent(System.out::println);

        for (Map.Entry<String, String> e : properties) {
            System.out.println(e.getKey() + "->" + e.getValue());
        }

        properties.print();
    }
}
