package com.smartbear.zephyrscale.junit.customformat;

import com.fasterxml.jackson.annotation.JsonInclude;

public class CustomFormatExecution {

    private String source;

    private String result;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private CustomFormatTestCase testCase;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public CustomFormatTestCase getTestCase() {
        return testCase;
    }

    public void setTestCase(CustomFormatTestCase testCase) {
        this.testCase = testCase;
    }
}
