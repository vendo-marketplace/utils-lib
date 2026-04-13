package com.vendo.utils_lib;

public class StringUtils {

    public static boolean contains(String target, String... arr) {
        for (String value : arr) {
            if (target.equals(value)) return true;
        }

        return false;
    }

}
