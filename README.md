[ ![Download](https://api.bintray.com/packages/avst/TM4J/tm4j-junit-integration/images/download.svg) ](https://bintray.com/avst/TM4J/tm4j-junit-integration/_latestVersion)

# Zephyr Scale Junit Integration

This project is a Zephyr Scale JUnit Integration which aims to generate a file describing the test execution result for each Test Case.

In order to achieve that, you need to annotate the JUnit methods with `@TestCase(key = "JQA-T2")` or `@TestCase(name = "")`.

JUnit methods which are not annotated with `@TestCase` will also be added to the JSON file, but without the Test Case Key property.

JUnit methods which are not annotated with `@TestCase(name = "")` will also be added to the JSON file, but without the Test Case Name property.

## Usage

You can have a look at this [Zephyr Scale JUnit Integration Example](https://bitbucket.org/smartbeartm4j/zephyrscale-junit-integration-example) repository.

You need to add the dependency to your pom file.

```
<dependencies>
    <dependency>
        <groupId>com.smartbear</groupId>
        <artifactId>zephyrscale-junit-integration</artifactId>
        <version>2.0.0</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```

Also, you'll need to register the Zephyr Scale JUnit Listener.

```
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-surefire-plugin</artifactId>
            <version>2.22.0</version>
            <configuration>
                <properties>
                    <property>
                        <name>listener</name>
                        <value>com.smartbear.zephyrscale.junit.ExecutionListener</value>
                    </property>
                </properties>
            </configuration>
        </plugin>
    </plugins>
</build>
```

The next step is to annotate your JUnit tests with `@TestCase` or don't annotate at all, if the Test Case doesn't exist yet.

```
public class CalculatorSumTest {

    @Test
    @TestCase(key = "JQA-T1")
    public void sumTwoNumbersAndPass() {
        Calculator calculator = new Calculator();
        assertEquals(1, calculator.sum(1, 2));
    }

    @Test
    @TestCase(key = "JQA-T2")
    public void sumTwoNumbersAndFail() {
        Calculator calculator = new Calculator();
        assertNotEquals(2, calculator.sum(1, 2));
    }

    @Test
    public void notMappedToTestCaseAndPass() {
        Calculator calculator = new Calculator();
        assertEquals(1, calculator.sum(1, 2));
    }

    @Test
    @TestCase(name = "Mapped to Test Case Name and Pass")
    public void mappedToTestCaseNameAndPass() {
        Calculator calculator = new Calculator();
        assertEquals(1, calculator.sum(1, 2));
    }

}

```

Now, you can run your tests with `mvn test` and the Zephyr Scale test execution result file will be generated in the same execution folder.

### zephyrscale_result.json

```
{
   "version": 1,
   "executions":[
      {
         "source":"CalculatorSumTest.sumTwoNumbersAndPass",
         "result":"Passed",
         "testCase": {
            "key": "JQA-T1"
         }
      },
      {
        "source":"CalculatorSumTest.sumTwoNumbersAndFail",
        "result":"Failed",
         "testCase": {
            "key": "JQA-T2"
         }
      },
      {
        "source":"CalculatorSumTest.notMappedToTestCaseAndPass",
        "result":"Passed"
      },
      {
        "source":"CalculatorSumTest.mappedToTestCaseNameAndPass",
        "result":"Passed",
         "testCase": {
            "name": "Mapped to Test Case Name and Pass"
         }
      }
   ]
}
```

## Support

For any issues or enquiries please get in touch with the Zephyr Scale team at SmartBear using the [support portal](https://support.smartbear.com/zephyr-scale/).
