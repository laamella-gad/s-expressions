package com.laamella.sexpression;

import com.laamella.sexpression.properties.SProperties;
import org.junit.Test;

import java.util.Map;

public class Samples {
    @Test
    public void configurationFile() throws Exception {
        SProperties properties = new SProperties();
        properties.loadResource("/config.s");

        properties.get("greeting").ifPresent(System.out::println);
        properties.get("application.window.height").ifPresent(System.out::println);
        properties.set("application.window.height", "500");
        properties.set("this.is.new", "Hurray! New!");

        for (Map.Entry<String, String> e : properties) {
            System.out.println(e.getKey() + "->" + e.getValue());
        }

        StringBuilder stringBuilder = new StringBuilder();
        properties.save(stringBuilder);
        System.out.println(stringBuilder);
    }
}
