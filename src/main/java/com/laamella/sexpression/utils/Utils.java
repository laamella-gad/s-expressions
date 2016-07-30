package com.laamella.sexpression.utils;

public class Utils {
    public static boolean hasWhitespace(String string) {
        for (char c : string.toCharArray()) {
            if (Character.isWhitespace(c)) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasSpecialCharacters(String string) {
        for (char c : string.toCharArray()) {
            if (Character.isWhitespace(c)) {
                continue;
            }
            if (c < 32 || c == 127) {
                return true;
            }
        }
        return false;
    }
}
