package com.smartbear.zephyrscale.junit.customformat;

import com.fasterxml.jackson.annotation.JsonInclude;

public class CustomFormatTestCase {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String key;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String name;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
