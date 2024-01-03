package com.smartbear.zephyrscale.junit;

import static com.smartbear.zephyrscale.junit.customformat.CustomFormatConstants.FAILED;
import static com.smartbear.zephyrscale.junit.customformat.CustomFormatConstants.PASSED;
import static com.smartbear.zephyrscale.junit.file.CustomFormatFile.generateCustomFormatFile;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartbear.zephyrscale.junit.annotation.TestCase;
import com.smartbear.zephyrscale.junit.customformat.CustomFormatContainer;
import com.smartbear.zephyrscale.junit.customformat.CustomFormatExecution;
import com.smartbear.zephyrscale.junit.customformat.CustomFormatExecution.CustomFormatExecutionBuilder;
import com.smartbear.zephyrscale.junit.customformat.CustomFormatTestCase;
import com.smartbear.zephyrscale.junit.customformat.CustomFormatTestCase.CustomFormatTestCaseBuilder;
import com.smartbear.zephyrscale.junit.decorator.TestDescriptionDecorator;
import com.smartbear.zephyrscale.junit.file.CustomFormatFile;

public class ExecutionListenerTest {

    class TestCaseAnnotationTest {
        @TestCase(key = "JQA-T1")
        public void shouldHaveTestCaseWithKeyJQAT1() {}

        @TestCase(key = "JQA-T1")
        public void shouldHaveAnotherTestCaseWithKeyJQAT1(){}

        @TestCase(key = "JQA-T2")
        public void shouldHaveTestCaseWithKeyJQAT2() {}

        public void shouldPassWithoutATestCaseKey(){}

        public void shouldFailWithoutATestCaseKey(){}

        @TestCase(key = "JQA-T1", name = "Have one Result For each Test Case Name")
        public void shouldHaveOneResultForEachTestCaseKey(){}

        @TestCase(key = "JQA-T2", name = "Create File With Empty Result")
        public void shouldCreateFileWithEmptyResult(){}

        @TestCase(name = "Successful Login")
        public void shouldHaveTestCaseWithName(){}
    }

    @Test
    public void shouldCreateFileWithEmptyResult() throws Exception {
        ExecutionListener executionListener = new ExecutionListener();
        executionListener.testRunStarted(null);
        executionListener.testRunFinished(null);

        CustomFormatContainer customFormatContainer = getZephyrScaleJUnitResults();
        assertTrue(customFormatContainer.getExecutions().isEmpty());
    }

    @Test
    public void shouldNotSetNameWhenItIsNotSetInTestCaseAnnotation() throws Exception {
        ExecutionListener executionListener = new ExecutionListener();
        executionListener.testRunStarted(null);

        Method method = TestCaseAnnotationTest.class.getDeclaredMethod("shouldHaveTestCaseWithKeyJQAT1");
        Description descriptionJQAT1 = Description.createTestDescription(this.getClass(), method.getName(), method.getAnnotations());
        executionListener.testFinished(descriptionJQAT1);

        executionListener.testRunFinished(null);

        CustomFormatContainer customFormatContainer = getZephyrScaleJUnitResults();

        assertEquals(1, customFormatContainer.getExecutions().size());
        CustomFormatExecution jqat1Result = customFormatContainer.getExecutions().get(0);
        assertTrue(jqat1Result.getTestCase().getName().isEmpty());
    }

    @Test
    public void shouldNotSetKeyWhenItIsNotSetInTestCaseAnnotation() throws Exception {
        ExecutionListener executionListener = new ExecutionListener();
        executionListener.testRunStarted(null);

        Method method = TestCaseAnnotationTest.class.getDeclaredMethod("shouldHaveTestCaseWithName");

        Description descriptionJQAT1 = Description.createTestDescription(this.getClass(), method.getName(), method.getAnnotations());
        executionListener.testFinished(descriptionJQAT1);

        executionListener.testRunFinished(null);

        CustomFormatContainer customFormatContainer = getZephyrScaleJUnitResults();

        assertEquals(1, customFormatContainer.getExecutions().size());
        CustomFormatExecution jqat1Result = customFormatContainer.getExecutions().get(0);
        assertTrue(jqat1Result.getTestCase().getKey().isEmpty());
    }

    @Test
    public void shouldHaveOnePassedAndOneFailResultForEachNotMappedToTestCaseKeyMethod() throws Exception {
        ExecutionListener executionListener = new ExecutionListener();
        executionListener.testRunStarted(null);

        Method shouldPassWithoutATestCaseKey = TestCaseAnnotationTest.class.getDeclaredMethod("shouldPassWithoutATestCaseKey");
        Description descriptionJQAT1 = Description.createTestDescription(this.getClass(), shouldPassWithoutATestCaseKey.getName());
        executionListener.testFinished(descriptionJQAT1);

        Method shouldFailWithoutATestCaseKey = TestCaseAnnotationTest.class.getDeclaredMethod("shouldFailWithoutATestCaseKey");
        Description descriptionJQAT2 = Description.createTestDescription(this.getClass(), shouldFailWithoutATestCaseKey.getName());
        executionListener.testFailure(new Failure(descriptionJQAT2, null));
        executionListener.testFinished(descriptionJQAT2);

        executionListener.testRunFinished(null);

        CustomFormatContainer customFormatContainer = getZephyrScaleJUnitResults();

        assertEquals(2, customFormatContainer.getExecutions().size());

        CustomFormatExecution jqat1Result = customFormatContainer.getExecutions().get(0);
        assertEquals(PASSED, jqat1Result.getResult());

        CustomFormatExecution jqat2Result = customFormatContainer.getExecutions().get(1);
        assertEquals(FAILED, jqat2Result.getResult());
    }

    @Test
    public void shouldHaveOnePassedResultForEachTestCaseKeyWhenThereWasNoFail() throws Exception {
        ExecutionListener executionListener = new ExecutionListener();
        executionListener.testRunStarted(null);

        Method shouldHaveTestCaseWithKeyJQAT1 = TestCaseAnnotationTest.class.getDeclaredMethod("shouldHaveTestCaseWithKeyJQAT1");
        Description descriptionJQAT1 = Description.createTestDescription(this.getClass(), shouldHaveTestCaseWithKeyJQAT1.getName(), shouldHaveTestCaseWithKeyJQAT1.getAnnotations());
        executionListener.testFinished(descriptionJQAT1);

        Method shouldHaveTestCaseWithKeyJQAT2 = TestCaseAnnotationTest.class.getDeclaredMethod("shouldHaveTestCaseWithKeyJQAT2");
        Description descriptionJQAT2 = Description.createTestDescription(this.getClass(), shouldHaveTestCaseWithKeyJQAT2.getName(), shouldHaveTestCaseWithKeyJQAT2.getAnnotations());
        executionListener.testFinished(descriptionJQAT2);

        executionListener.testRunFinished(null);

        CustomFormatContainer customFormatContainer = getZephyrScaleJUnitResults();

        assertEquals(2, customFormatContainer.getExecutions().size());
        customFormatContainer.getExecutions().forEach(result -> assertEquals(PASSED, result.getResult()));
    }

    @Test
    public void shouldHaveOnePassedResultForEachTestCaseNameWhenThereWasNoFail() throws Exception {
        ExecutionListener executionListener = new ExecutionListener();
        executionListener.testRunStarted(null);

        Method shouldHaveOneResultForEachTestCaseKey = TestCaseAnnotationTest.class.getDeclaredMethod("shouldHaveOneResultForEachTestCaseKey");
        Description descriptionJQAT1 = Description.createTestDescription(this.getClass(), shouldHaveOneResultForEachTestCaseKey.getName(), shouldHaveOneResultForEachTestCaseKey.getAnnotations());
        executionListener.testFinished(descriptionJQAT1);

        Method shouldCreateFileWithEmptyResult = TestCaseAnnotationTest.class.getDeclaredMethod("shouldCreateFileWithEmptyResult");
        Description descriptionJQAT2 = Description.createTestDescription(this.getClass(), shouldCreateFileWithEmptyResult.getName(), shouldCreateFileWithEmptyResult.getAnnotations());
        executionListener.testFailure(new Failure(descriptionJQAT2, null));
        executionListener.testFinished(descriptionJQAT2);

        executionListener.testRunFinished(null);

        CustomFormatContainer customFormatContainer = getZephyrScaleJUnitResults();

        assertEquals(2, customFormatContainer.getExecutions().size());

        CustomFormatExecution jqat1Result = getResultByTestCaseName(customFormatContainer, "Have one Result For each Test Case Name");
        assertEquals(PASSED, jqat1Result.getResult());

        CustomFormatExecution jqat2Result = getResultByTestCaseName(customFormatContainer, "Create File With Empty Result");
        assertEquals(FAILED, jqat2Result.getResult());
    }

    @Test
    public void shouldHaveOnePassedAndOneFailedResultsWhenThereWasSomeFail() throws Exception {
        ExecutionListener executionListener = new ExecutionListener();
        executionListener.testRunStarted(null);

        Method shouldHaveTestCaseWithKeyJQAT1 = TestCaseAnnotationTest.class.getDeclaredMethod("shouldHaveTestCaseWithKeyJQAT1");
        Description descriptionJQAT1 = Description.createTestDescription(this.getClass(), shouldHaveTestCaseWithKeyJQAT1.getName(), shouldHaveTestCaseWithKeyJQAT1.getAnnotations());
        executionListener.testFinished(descriptionJQAT1);

        Method shouldHaveTestCaseWithKeyJQAT2 = TestCaseAnnotationTest.class.getDeclaredMethod("shouldHaveTestCaseWithKeyJQAT2");
        Description descriptionJQAT2 = Description.createTestDescription(this.getClass(), shouldHaveTestCaseWithKeyJQAT2.getName(), shouldHaveTestCaseWithKeyJQAT2.getAnnotations());
        executionListener.testFailure(new Failure(descriptionJQAT2, null));
        executionListener.testFinished(descriptionJQAT2);

        executionListener.testRunFinished(null);

        CustomFormatContainer customFormatContainer = getZephyrScaleJUnitResults();

        assertEquals(2, customFormatContainer.getExecutions().size());

        CustomFormatExecution jqat1Result = getResultByTestCaseKey(customFormatContainer, "JQA-T1");
        assertEquals(PASSED, jqat1Result.getResult());

        CustomFormatExecution jqat2Result = getResultByTestCaseKey(customFormatContainer, "JQA-T2");
        assertEquals(FAILED, jqat2Result.getResult());
    }

    @Test
    public void shouldHaveMultipleResultsForTheSameTestCaseKeyWhenTheyAreAnnotatedInDifferentTestMethods() throws Exception {
        ExecutionListener executionListener = new ExecutionListener();
        executionListener.testRunStarted(null);

        Method shouldHaveTestCaseWithKeyJQAT1 = TestCaseAnnotationTest.class.getDeclaredMethod("shouldHaveTestCaseWithKeyJQAT1");

        Description descriptionJQAT1 = Description.createTestDescription(this.getClass(), shouldHaveTestCaseWithKeyJQAT1.getName(), shouldHaveTestCaseWithKeyJQAT1.getAnnotations());
        executionListener.testFinished(descriptionJQAT1);

        Method shouldHaveAnotherTestCaseWithKeyJQAT1 = TestCaseAnnotationTest.class.getDeclaredMethod("shouldHaveAnotherTestCaseWithKeyJQAT1");

        Description descriptionJQAT2 = Description.createTestDescription(this.getClass(), shouldHaveAnotherTestCaseWithKeyJQAT1.getName(), shouldHaveAnotherTestCaseWithKeyJQAT1.getAnnotations());
        executionListener.testFailure(new Failure(descriptionJQAT2, null));
        executionListener.testFinished(descriptionJQAT2);

        executionListener.testRunFinished(null);

        CustomFormatContainer customFormatContainer = getZephyrScaleJUnitResults();

        assertEquals(2, customFormatContainer.getExecutions().size());

        CustomFormatExecution jqat1Result = getExecutionByDescription(customFormatContainer, descriptionJQAT1);
        assertEquals("JQA-T1", jqat1Result.getTestCase().getKey());
        assertEquals(PASSED, jqat1Result.getResult());

        CustomFormatExecution jqat2Result = getExecutionByDescription(customFormatContainer, descriptionJQAT2);
        assertEquals("JQA-T1", jqat2Result.getTestCase().getKey());
        assertEquals(FAILED, jqat2Result.getResult());
    }
    
    
    @Test
    public void shouldHaveOnlyOneResultForTestCaseKeyWhenItRunsAsParameterizedJUnitTest() throws IOException, NoSuchMethodException {
        
    	List<CustomFormatExecution> executionList = new ArrayList<>();
    	
        Method shouldHaveTestCaseWithKeyJQAT1 = TestCaseAnnotationTest.class.getDeclaredMethod("shouldHaveTestCaseWithKeyJQAT1");

        Description descriptionParam1 = Description.createTestDescription(this.getClass(), shouldHaveTestCaseWithKeyJQAT1.getName() + "[0]", shouldHaveTestCaseWithKeyJQAT1.getAnnotations());
        CustomFormatExecution  execution1 = getExecution(descriptionParam1, PASSED);
        executionList.add(execution1);
       

        Description descriptionParam2 = Description.createTestDescription(this.getClass(), shouldHaveTestCaseWithKeyJQAT1.getName() + "[1]", shouldHaveTestCaseWithKeyJQAT1.getAnnotations());
        CustomFormatExecution  execution2 = getExecution(descriptionParam2, PASSED);
        executionList.add(execution2);

        generateCustomFormatFile(CustomFormatContainer.builder().executions(executionList).build());

        CustomFormatContainer customFormatContainer = getZephyrScaleJUnitResults();
        assertEquals(2, customFormatContainer.getExecutions().size());

        assertEquals(PASSED, customFormatContainer.getExecutions().get(0).getResult());
        assertEquals(PASSED, customFormatContainer.getExecutions().get(1).getResult());
    }

    @Test
    public void shouldHaveFailedResultForTestCaseKeyWhenItRunsAsParameterizedJUnitTestAndOneOfThemHaveFailed() throws IOException, NoSuchMethodException {
        
    	List<CustomFormatExecution> executionList = new ArrayList<>();
    	
        Method shouldHaveTestCaseWithKeyJQAT1 = TestCaseAnnotationTest.class.getDeclaredMethod("shouldHaveTestCaseWithKeyJQAT1");

        Description descriptionParam1 = Description.createTestDescription(this.getClass(), shouldHaveTestCaseWithKeyJQAT1.getName() + "[0]", shouldHaveTestCaseWithKeyJQAT1.getAnnotations());
        CustomFormatExecution  execution1 = getExecution(descriptionParam1, FAILED);
        executionList.add(execution1);
        
        Description descriptionParam2 = Description.createTestDescription(this.getClass(), shouldHaveTestCaseWithKeyJQAT1.getName() + "[1]", shouldHaveTestCaseWithKeyJQAT1.getAnnotations());
        CustomFormatExecution  execution2 = getExecution(descriptionParam2, PASSED);
        executionList.add(execution2);

        generateCustomFormatFile(CustomFormatContainer.builder().executions(executionList).build());

        CustomFormatContainer customFormatContainer = getZephyrScaleJUnitResults();
        assertEquals(2, customFormatContainer.getExecutions().size());

        assertEquals(FAILED, customFormatContainer.getExecutions().get(0).getResult());
        assertEquals(PASSED, customFormatContainer.getExecutions().get(1).getResult());
    }

    
    private CustomFormatExecution getResultByTestCaseKey(CustomFormatContainer customFormatContainer, String testCaseKey) {
        return customFormatContainer.getExecutions()
                .stream()
                .filter(r -> r.getTestCase().getKey().equals(testCaseKey))
                .findFirst()
                .get();
    }

    private CustomFormatExecution getResultByTestCaseName(CustomFormatContainer customFormatContainer, String testCaseName) {
        return customFormatContainer.getExecutions()
                .stream()
                .filter(r -> r.getTestCase().getName().equals(testCaseName))
                .findFirst()
                .get();
    }

    private CustomFormatExecution getExecutionByDescription(CustomFormatContainer customFormatContainer, Description testMethodID) {
        return customFormatContainer.getExecutions()
                .stream()
                .filter(r -> r.getSource().equals(new TestDescriptionDecorator(testMethodID).getSource()))
                .findFirst()
                .get();
    }

    private CustomFormatContainer getZephyrScaleJUnitResults() throws IOException {
        File generatedResultFile = new File(CustomFormatFile.DEFAULT_ZEPHYRSCALE_RESULT_FILE_NAME);
        return new ObjectMapper().readValue(generatedResultFile, CustomFormatContainer.class);
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
