package com.smartbear.zephyrscale.junit.builder;

import com.smartbear.zephyrscale.junit.customformat.CustomFormatExecution;
import com.smartbear.zephyrscale.junit.decorator.TestDescriptionDecorator;

public class CustomFormatExecutionBuilder {

    private final CustomFormatExecution customFormatExecution;

    public CustomFormatExecutionBuilder() {
        customFormatExecution = new CustomFormatExecution();
    }

    public CustomFormatExecutionBuilder build(TestDescriptionDecorator testDescriptionDecorator) {
        customFormatExecution.setTestCase(testDescriptionDecorator.getTestCase());
        customFormatExecution.setSource(testDescriptionDecorator.getSource());

        return this;
    }

    public CustomFormatExecution getCustomFormatExecution() {
        return customFormatExecution;
    }
}
