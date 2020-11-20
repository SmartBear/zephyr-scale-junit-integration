package com.smartbear.zephyrscale.junit.decorator;

import com.smartbear.zephyrscale.junit.builder.CustomFormatTestCaseBuilder;
import com.smartbear.zephyrscale.junit.customformat.CustomFormatTestCase;
import org.junit.runner.Description;

public class TestDescriptionDecorator {

    private static final String PARAMETERIZED_TEST_NAME_PATTERN = "\\[[0-9]+]$";
    private Description description;

    public TestDescriptionDecorator(Description description) {
        this.description = description;
    }

    public String getSource() {
        String testClassName = description.getTestClass().getName();
        String methodName = getMethodName();
        return testClassName + "." + methodName;
    }

    public CustomFormatTestCase getTestCase() {
        return new CustomFormatTestCaseBuilder().build(description).getTestCase();
    }

    private String getMethodName() {
        return description.getMethodName().replaceFirst(PARAMETERIZED_TEST_NAME_PATTERN, "");
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TestDescriptionDecorator) {
            TestDescriptionDecorator testDescriptionDecorator = (TestDescriptionDecorator) obj;
            return this.getSource().equals(testDescriptionDecorator.getSource());
        }

        return false;
    }
}
