package com.smartbear.zephyrscale.junit.decorator;

import org.junit.Test;
import org.junit.runner.Description;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class TestDescriptionDecoratorTest {

    @Test
    public void shouldGetDescriptionForRegularJUnitTest() {
        Description description = Description.createTestDescription(this.getClass(), "shouldGetDescriptionForRegularJUnitTest");
        TestDescriptionDecorator testMethodID = new TestDescriptionDecorator(description);

        assertTrue(testMethodID.getSource().endsWith("TestDescriptionDecoratorTest.shouldGetDescriptionForRegularJUnitTest"));
    }

    @Test
    public void shouldGetDescriptionForParameterizedJUnitTest() {
        Description description = Description.createTestDescription(this.getClass(), "shouldGetDescriptionForParameterizedJUnitTest[1]");
        TestDescriptionDecorator testMethodID = new TestDescriptionDecorator(description);

        assertTrue(testMethodID.getSource().endsWith("TestDescriptionDecoratorTest.shouldGetDescriptionForParameterizedJUnitTest"));
    }

    @Test
    public void shouldBeEqualForRegularJUnitTest() {
        Description description = Description.createTestDescription(this.getClass(), "shouldGetDescriptionForRegularJUnitTest");
        TestDescriptionDecorator testMethodID1 = new TestDescriptionDecorator(description);
        TestDescriptionDecorator testMethodID2 = new TestDescriptionDecorator(description);

        assertEquals(testMethodID1, testMethodID2);
    }

    @Test
    public void shouldNotBeEqualForRegularJUnitTest() {
        Description description1 = Description.createTestDescription(this.getClass(), "shouldGetDescriptionForRegularJUnitTest");
        TestDescriptionDecorator testMethodID1 = new TestDescriptionDecorator(description1);
        Description description2 = Description.createTestDescription(this.getClass(), "shouldGetDescriptionForParameterizedJUnitTest");
        TestDescriptionDecorator testMethodID2 = new TestDescriptionDecorator(description2);

        assertNotEquals(testMethodID1, testMethodID2);
    }

    @Test
    public void shouldBeEqualForParameterizedJUnitTest() {
        Description description1 = Description.createTestDescription(this.getClass(), "shouldGetDescriptionForParameterizedJUnitTest[0]");
        TestDescriptionDecorator testMethodID1 = new TestDescriptionDecorator(description1);
        Description description2 = Description.createTestDescription(this.getClass(), "shouldGetDescriptionForParameterizedJUnitTest[1]");
        TestDescriptionDecorator testMethodID2 = new TestDescriptionDecorator(description2);

        assertEquals(testMethodID1, testMethodID2);
    }
}
