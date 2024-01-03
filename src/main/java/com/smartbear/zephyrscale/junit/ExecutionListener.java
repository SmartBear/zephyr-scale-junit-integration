package com.smartbear.zephyrscale.junit;

import static com.smartbear.zephyrscale.junit.file.CustomFormatFile.generateCustomFormatFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

import com.smartbear.zephyrscale.junit.annotation.TestCase;
import com.smartbear.zephyrscale.junit.customformat.CustomFormatContainer;
import com.smartbear.zephyrscale.junit.customformat.CustomFormatExecution;
import com.smartbear.zephyrscale.junit.customformat.CustomFormatExecution.CustomFormatExecutionBuilder;
import com.smartbear.zephyrscale.junit.customformat.CustomFormatTestCase;
import com.smartbear.zephyrscale.junit.customformat.CustomFormatTestCase.CustomFormatTestCaseBuilder;

public class ExecutionListener extends RunListener {

    List<CustomFormatExecution> executionList = null;
    
    private boolean status = true;

    @Override
    public void testRunStarted(Description description) throws Exception {
    	
        super.testRunStarted(description);

        executionList = Collections.synchronizedList(new ArrayList<CustomFormatExecution>());
    }

    @Override
    public void testFailure(Failure failure) throws Exception {
   
        super.testFailure(failure);
        status = false;
        String message = failure.getException() != null && failure.getMessage() != null ?  "Failed: " + failure.getMessage() : "Failed";
        executionList.add(getExecution(failure.getDescription(), message));
    }
    
    
    @Override
    public void testIgnored(Description description) throws Exception {
    	super.testIgnored(description);
    	status = false;
    	executionList.add(getExecution(description, "Ignored"));
    }

    @Override
    public void testFinished(Description description) throws Exception {
        super.testFinished(description);
        if(status)
        	executionList.add(getExecution(description, "Passed"));
        status = true;
    }

    @Override
    public void testRunFinished(Result result) throws Exception {
        super.testRunFinished(result);
        
        generateCustomFormatFile(CustomFormatContainer.builder().executions(executionList).build());
    }

    private CustomFormatExecution getExecution(Description description, String result) {
    	
    	CustomFormatExecutionBuilder custFormatExecutionBuilder = CustomFormatExecution.builder();
    	custFormatExecutionBuilder.source(description.getTestClass().getName() + "." + description.getMethodName());
    	custFormatExecutionBuilder.result(result);
    	
    	CustomFormatTestCaseBuilder customFormatTestCaseBuilder = CustomFormatTestCase.builder();
    	
    	TestCase testCase = description.getAnnotation(TestCase.class);
    	if(testCase !=  null) {
    		customFormatTestCaseBuilder.key(testCase.key()).name(testCase.name());
    		custFormatExecutionBuilder.testCase(customFormatTestCaseBuilder.build());
    	} 
    	return custFormatExecutionBuilder.build();	
    }
}
