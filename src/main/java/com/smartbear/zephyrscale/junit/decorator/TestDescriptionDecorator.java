package com.smartbear.zephyrscale.junit.decorator;

import org.junit.runner.Description;

import com.smartbear.zephyrscale.junit.annotation.TestCase;
import com.smartbear.zephyrscale.junit.customformat.CustomFormatTestCase;
import com.smartbear.zephyrscale.junit.customformat.CustomFormatTestCase.CustomFormatTestCaseBuilder;

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

		TestCase testCase = description.getAnnotation(TestCase.class);

		CustomFormatTestCaseBuilder customFormatTestCaseBuilder = CustomFormatTestCase.builder();

		if (testCase != null) {
			customFormatTestCaseBuilder.key(testCase.key()).name(testCase.name());
		}

		return customFormatTestCaseBuilder.build();
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
