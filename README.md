## spring-test-recorder 
#### A Spring tool that creates unit/integration tests from runtime calls

![GitHub](https://img.shields.io/github/license/testrecorder/spring-test-recorder)
![GitHub Workflow Status](https://img.shields.io/github/workflow/status/testrecorder/spring-test-recorder/Java%20CI%20with%20Gradle)

It works in projects using Spring Boot and generates JUnit tests for selected methods, marked with an annotation.  
For these tests, it can also generate mocks using Mockito for dependencies marked with another annotation.  


The tool will handle most of the cases and leave TODOs in places where the code could not be generated automatically. Solving these TODOs is mandatory in order to make the tests work. Also refactoring and polishing the generated tests is highly recommended.


This tool is still work in progress, but I estimate that it already automates 80% of the test writing work. 
So even if the tool cannot completely automate test generation, it will provide a good productivity boost.


## When is this tool needed?
- when you want to change code that does not have unit tests and want to be sure you don't break the existing functionality
- when you need to quickly add a failing unit test before fixing a bug
- when the tests and mocks are long and hard to write, so you could use a jump start
- when you are doing big changes in the design, and a lot of the tests need to be rewritten
- when you want to record a functional test with real data from UI 


## Usage
Download this project. Open a terminal in the spring-test-recorder folder and run: 
```
gradlew publishToMavenLocal
```

In your project, if you use Maven, add the following dependency in pom.xml:
```XML
<dependency>
    <groupId>org.springtestrecorder</groupId>
    <artifactId>spring-test-recorder</artifactId>
    <version>0.2.0</version>
</dependency>
```

In your project, if you use Gradle, add the following dependency in build.gradle:
```
implementation 'org.springtestrecorder:spring-test-recorder:0.2.0'
```

In your project, add "org.springtestrecorder" to @ComponentScan in your Spring Boot configuration. 

```Java
@ComponentScan(basePackages={"...", "org.springtestrecorder"})
```

- Mark a method in a Spring components with **@RecordTest** annotation. The marks the method for test generation.  
- Optional: Mark injected components that you want to mock with **@RecordMockForTest** annotation.  
- Run the project and interact with the User Interface or API in order to call the annotated method.  
  

## Example

Let's start with something simple to illustrate the usage. Let's say we have a function that adds floats.

```Java
public float addFloats(float x, float y) {
    return x + y;
}
```

To generate unit tests for this method we add **@RecordTest** before it.

```Java
@RecordTest
public float addFloats(float x, float y) {
    return x + y;
}
```

We run the application and addFloats is called with parameters 2f and 3f.
In the console we will get the following:

```Java
BEGIN GENERATED TEST =========

package org.springtestrecorder.sample.services;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SampleServiceTest {
    //TODO rename the test to describe the use case
    //TODO refactor the generated code to make it easier to understand
    @Test
    void addFloats() throws Exception {
        // Arrange
        SampleService sampleService = new SampleService();
    
        // Act
        Float result = sampleService.addFloats(2.0f, 3.0f);
    
        // Assert
        assertEquals(5.0f, result);
    }
}

END GENERATED TEST =========
```


## Another example
Let's try something less straightforward.
We have a method for calculating Employee salary that does not have unit tests yet.  
We add **@RecordTest** to computeEmployeeSalary method to mark that we want generated tests.

```Java
public class SalaryService {
    // ...
    @RecordTest
    public double computeEmployeeSalary(int employeeId) throws Exception {
        // ...
        Employee employee = employeeRepository.getEmployee(employeeId);
        // ...
        return salary;
    }
    //...
}
```
	
The method calls getEmployee from EmployeeRepository, an injected component.  
We add **@RecordMockForTest** to EmployeeRepository class to mark that we want this class mocked in the tests.

```Java
@RecordMockForTest
public class EmployeeRepository {
    public Employee getEmployee(int id) throws Exception {
        // ...
        // Get Employee data from DB
        // ...
        return employee;
    }
}
```

We interact with the UI/API and the computeEmployeeSalary method is called with real data.
The generated test is something like this (depending on the real data):

```Java
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.sampleapp.model.Department;
import com.sampleapp.model.Employee;
import com.sampleapp.services.EmployeeRepository;
import static org.mockito.Mockito.*;

class SalaryServiceTest {
    @Test
    void computeEmployeeSalary() throws Exception {
        // Arrange
        Department department1 = Department.builder()
            .id(100)
            .name("IT")
            .build();

        Employee employee1 = Employee.builder()
            .id(1)
            .firstName("Doe")
            .lastName("John")
            .department(department1)
            .salaryParam1(1000.0)
            .salaryParam2(1500.0)
            .salaryParam3(200.0)
            .build();

        EmployeeRepository employeeRepository = mock(EmployeeRepository.class);
        when(employeeRepository.getEmployee(1)).thenReturn(employee1);
        SalaryService salaryService = new SalaryService(employeeRepository);

        // Act
        double result = salaryService.computeEmployeeSalary(1);

        // Assert
        assertEquals(4000.0, result);
    }
}
```

Now all we need to do is refactor a little the generated code, add a test description, and the unit test is done.

You can check [spring-test-recorder-demo](https://github.com/testrecorder/spring-test-recorder-demo) to see a running project with examples.


## How does it work?
For each execution of a Spring component method annotated with **@RecordTest**, the following steps are executed:
- Target object and method arguments for the annotated methods are retrieved using Aspect Oriented Programing (AOP).  
- Java primitives, arrays, enums and commonly used Java classes like Date, ArrayList, HashMap, HashSet are recognised and handled accordingly.  
  For other objects the tool will use Java Reflection to detect and use constructors, setters, fields or Lombok.  
  If the tool cannot create an object, a TODO will be generated.
- The result object is retrieved also by using AOP. Asserts are generated based on the result and result's dependencies.
- The tool will also detect side effects in the objects involved and will generate asserts.
- The generated test is written in the console.
  
A similar approach is used to generate mocks for methods in a Spring component annotated with **@RecordMockForTest**.


## Questions? Problems? Suggestions?
To report a bug or request a feature, create a [GitHub issue](https://github.com/testrecorder/spring-test-recorder/issues/new/choose). 
Please ensure someone else hasn’t created an issue for the same topic.







