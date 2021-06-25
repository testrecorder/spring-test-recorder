## spring-test-recorder

A tool for Spring that records JUnit + Mockito unit/integration tests from runtime calls

## Relevant Technologies

Java, Spring Boot, JUnit, Mockito

## When is this tool needed?
Test-driven development (TDD) is a great way to write software, but there are cases when generating a test from existing code might be needed:
- when you want to change code that does not have unit tests and want to be sure you don't introduce new bugs
- when you need to quickly add a failing unit test before fixing a bug
- when the tests and mocks are long and hard to write, so you could use a jump start
- when you are doing big changes in the design, and a lot of the tests need to be rewritten
- when you want to record a functional test with real data from UI
- when in your company there is no time/budget/culture/wiliness to use TDD

## Preparations
Add this dependency to pom.xml

    <dependency>
        <groupId>com.onushi</groupId>
        <artifactId>spring-test-recorder</artifactId>
        <version>0.1.0</version>
    </dependency>

Add "com.onushi.springtestrecorder" to @ComponentScan in your Spring Boot configuration 
    
    @ComponentScan(basePackages={"...", "com.onushi.springtestrecorder"})

## Usage
- Mark methods in Spring components with @RecordTest annotation.  
- Mark injected components that you want to mock with @RecordMockForTest annotation.  
- Run the project and interact with the User Interface or API in order to call the annotated methods.  
- Context, arguments and results are retrieved using Aspect Oriented Programing and Reflection. 
- For each execution of a method annotated with @RecordTest a test is generated in the console.  

## Code example
Let's say we have a method for calculating Employee salary that does not have unit tests yet:

	public class SalaryService {
		public double computeEmployeeSalary(int employeeId) throws Exception {
			// ...
			Employee employee = employeeRepository.getEmployee(employeeId);
			// ...
			return salary;
		}
	}
	
The function calls getEmployee from EmployeeRepository, an injected component.

	public class EmployeeRepository {
		public Employee getEmployee(int id) throws Exception {
			// ...
			// Get Employee data from DB
			// ...
			return employee;
		}
	}

We add @RecordTest to computeEmployeeSalary method to mark that we want tests generated from the runtime calls to this function.
We add @RecordMockForTest to EmployeeRepository class to mark that we want this class mocked in the tests.
We interact with the UI/API and the computeEmployeeSalary function is called with real data.

The resulted test is this:

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

## Questions? Problems? Suggestions?
To report a bug or request a feature, create a [GitHub issue](https://github.com/ibreaz/spring-test-recorder/issues/new/choose). 
Please ensure someone else hasn’t created an issue for the same topic.


