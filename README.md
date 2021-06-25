## spring-test-recorder

is a tool for Spring that records JUnit + Mockito unit/integration tests from runtime calls

## Relevant Technologies

Java, Spring Boot, JUnit, Mockito

## How to use it
Add this dependency to pom.xml

    <dependency>
        <groupId>com.onushi</groupId>
        <artifactId>spring-test-recorder</artifactId>
        <version>0.1.0</version>
    </dependency>

Add "com.onushi.springtestrecorder" to @ComponentScan in your Spring Boot configuration 
    
    @ComponentScan(basePackages={"...", "com.onushi.springtestrecorder"})

Mark a method in a Spring component with an @RecordTest annotation.  
Mark some injected dependencies with @RecordMockForTest annotation.  
Run the project and interact with UI/API.  
Context, arguments and results are retrieved using Aspect Oriented Programing and Reflection and used to generate a test for each function call in the console or to a disk file.  

Test-driven development is a great way to write software, but there are cases when generating a test from existing code might still be needed for various reasons:
- when you want to change code that does not have unit tests and want to be sure you don't introduce new bugs
- when for some reason in your company there is no time/budget to use TDD, but having tests generated after writing the code is still better than no tests
- when the tests and mocks are long and hard to write, so you could use a jump start
- when you are doing big changes in the design, and a lot of the tests need to be rewritten
- when you want to record a functional test with real data from UI
- when you need to add a failing unit test before fixing a bug  


Example:
Let's say you have some function for calculating Employee salary that does not have unit tests yet:

	public class SalaryService {
		public double computeEmployeeSalary(int employeeId) throws Exception {
			// ...
			Employee employee = employeeRepository.getEmployee(employeeId);
			// ...
			return salary;
		}
	}
	
	public class EmployeeRepository {
		public Employee getEmployee(int id) throws Exception {
			// ...
			// Get Employee data from DB
			// ...
			return employee;
		}
	}


You add @RecordTest to computeEmployeeSalary function to mark that you want tests generated from the runtime calls to this function.
You add @RecordMockForTest to EmployeeRepository class to mark that you want this class mocked in the tests.
You interact with the UI/API and the computeEmployeeSalary function is called with real data.

The resulted test will be like this (depending on the real data):

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




