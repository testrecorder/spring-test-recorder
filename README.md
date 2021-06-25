## spring-test-recorder

A Spring tool that creates unit/integration tests from runtime calls

It works in projects using Spring Boot and generates JUnit tests for selected methods.  
It can also generate mocks using Mockito for selected dependencies.  

## When is this tool needed?
 There are cases when generating a test from existing code might be needed:
- when you want to change code that does not have unit tests and want to be sure you don't break the existing functionality
- when you need to quickly add a failing unit test before fixing a bug
- when the tests and mocks are long and hard to write, so you could use a jump start
- when you are doing big changes in the design, and a lot of the tests need to be rewritten
- when you want to record a functional test with real data from UI
- when there is no time or budget to use Test-driven development (TDD)

The tool will handle most of the cases and leave TODOs in places where the code could not be generated automatically.  
Solving these TODOs is mandatory in order to make the test work. Refactoring and polishing the generated tests is highly recommended.

## Preparations
Add this dependency in project's pom.xml:

    <dependency>
        <groupId>com.onushi</groupId>
        <artifactId>spring-test-recorder</artifactId>
        <version>0.1.0</version>
    </dependency>

Add "com.onushi.springtestrecorder" to @ComponentScan in your Spring Boot configuration. 
    
    @ComponentScan(basePackages={"...", "com.onushi.springtestrecorder"})

## Usage
- Mark methods in Spring components with @RecordTest annotation.  
- Mark injected components that you want to mock with @RecordMockForTest annotation.  
- Run the project and interact with the User Interface or API in order to call the annotated methods.  

## Code example
Let's say we have a method for calculating Employee salary that does not have unit tests yet.  
We add @RecordTest to computeEmployeeSalary method to mark that we want generated tests.

	public class SalaryService {
        @RecordTest
		public double computeEmployeeSalary(int employeeId) throws Exception {
			// ...
			Employee employee = employeeRepository.getEmployee(employeeId);
			// ...
			return salary;
		}
	}
	
The method calls getEmployee from EmployeeRepository, an injected component.  
We add @RecordMockForTest to EmployeeRepository class to mark that we want this class mocked in the tests.

    @RecordMockForTest
	public class EmployeeRepository {
		public Employee getEmployee(int id) throws Exception {
			// ...
			// Get Employee data from DB
			// ...
			return employee;
		}
	}

We interact with the UI/API and the computeEmployeeSalary method is called with real data.
The generated test is something like this (depending on the real data):

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

Now all we need to do is refactor a little the generated code, add a test description, and the unit test is done.

## How does it work?
For each execution of a Spring component method annotated with @RecordTest, the following steps are executed:
- Context, arguments and results for the annotated methods are retrieved using Aspect Oriented Programing (AOP).  
- Java Reflection is used to analyse all these objects and their dependencies. 
- Java primitives, Enums and commonly used Java classes like Date, ArrayList, HashMap, HashSet are recognised and handled accordingly.  
  For other objects the tool will detect and use constructors, setters, fields, Lombok builders.  
  If the tool cannot detect how to create an object, a TODO will be generated with object state information.  
- The generated test is written in the console.
  

  A similar approach is used to generate mocks for methods in a Spring component annotated with @RecordMockForTest.  



## Questions? Problems? Suggestions?
To report a bug or request a feature, create a [GitHub issue](https://github.com/ibreaz/spring-test-recorder/issues/new/choose). 
Please ensure someone else hasn’t created an issue for the same topic.







