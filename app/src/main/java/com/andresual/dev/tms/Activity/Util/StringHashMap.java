package com.andresual.dev.tms.Activity.Util;

import java.util.HashMap;

public class StringHashMap extends HashMap<String, String> {
    public StringHashMap putMore(String key, String value) {
        put(key, value);
        return this;
    }
    public StringHashMap putMore(String key, double value) {
        put(key, Double.toString(value));
        return this;
    }
}
