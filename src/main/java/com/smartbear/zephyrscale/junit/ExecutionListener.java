package com.smartbear.zephyrscale.junit;

import com.smartbear.zephyrscale.junit.annotation.TestCase;
import com.smartbear.zephyrscale.junit.builder.CustomFormatContainerBuilder;
import com.smartbear.zephyrscale.junit.decorator.TestDescriptionDecorator;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

import static com.smartbear.zephyrscale.junit.file.CustomFormatFile.generateCustomFormatFile;
import static org.apache.commons.lang3.StringUtils.isEmpty;

public class ExecutionListener extends RunListener {

    private final Logger logger = org.slf4j.LoggerFactory.getLogger(ExecutionListener.class);

    private CustomFormatContainerBuilder customFormatContainerBuilder;
    private List<String> errorMessages = new ArrayList<>();

    @Override
    public void testRunStarted(Description description) throws Exception {
        super.testRunStarted(description);

        customFormatContainerBuilder = new CustomFormatContainerBuilder();
    }

    @Override
    public void testFailure(Failure failure) throws Exception {
        super.testFailure(failure);

        customFormatContainerBuilder.registerFailure(new TestDescriptionDecorator(failure.getDescription()));
    }

    @Override
    public void testFinished(Description description) throws Exception {
        super.testFinished(description);
        checkTestCaseAnnotation(description);

        customFormatContainerBuilder.registerFinished(new TestDescriptionDecorator(description));
    }

    @Override
    public void testRunFinished(Result result) throws Exception {
        super.testRunFinished(result);

        if (!errorMessages.isEmpty()) {
            printErrorMessages();
        } else {
            generateCustomFormatFile(customFormatContainerBuilder.getCustomFormatContainer());
        }
    }

    private void checkTestCaseAnnotation(Description description) {
        TestCase testCaseAnnotation = description != null ? description.getAnnotation(TestCase.class) : null;

        if (testCaseAnnotation != null && allFieldsAreEmpty(testCaseAnnotation)) {
            errorMessages.add("[ERROR - Zephyr Scale] You must inform at least one parameter to TestCase annotation in method " + description.getDisplayName());
        }
    }

    private boolean allFieldsAreEmpty(TestCase testCaseAnnotation) {
        return isEmpty(testCaseAnnotation.key()) && isEmpty(testCaseAnnotation.name());
    }

    private void printErrorMessages() {
        logger.error("[ERROR - Zephyr Scale] Zephyr Scale Results JSON File was not generated due to the following errors");
        errorMessages.forEach(errorMessage -> logger.error(errorMessage));
    }
}
