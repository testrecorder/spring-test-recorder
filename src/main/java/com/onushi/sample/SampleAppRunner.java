package com.onushi.sample;

import com.onushi.sample.model.*;
import com.onushi.sample.services.PersonService;
import com.onushi.sample.services.SalaryService;
import com.onushi.sample.services.SampleService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class SampleAppRunner implements CommandLineRunner {
    private final SampleService sampleService;

    public SampleAppRunner(SampleService sampleService) {
        this.sampleService = sampleService;
    }

    @Override
    public void run(String... args) throws Exception {
         demoSideEffects();
    }


    // TODO IB !!!! improve this demo to include complex args, mocking and complex return
    private void demo1() throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date dateOfBirth1 = simpleDateFormat.parse("1980-01-02");
        Date dateOfBirth2 = simpleDateFormat.parse("1970-02-03");
        Person marco = Person.builder()
                .firstName("Marco")
                .lastName("Polo")
                .dateOfBirth(dateOfBirth1)
                .build();

        Person tom = Person.builder()
                .firstName("Tom")
                .lastName("Richardson")
                .dateOfBirth(dateOfBirth2)
                .build();

        OtherEmployee otherEmployee = new OtherEmployee()
                .setName("Jack Norton")
                .setAge(23)
                .setStudent(true);

        OtherEmployee[] otherEmployeeArray = {otherEmployee};
        this.sampleService.demoFunction(Arrays.asList(tom, marco), otherEmployeeArray);
    }

    private void demoSideEffects() {
        Employee employee = Employee.builder()
                .id(1)
                .firstName("John")
                .lastName("Doe")
                .salaryParam1(1000)
                .salaryParam2(1500)
                .department(Department.builder()
                        .id(100)
                        .name("IT")
                        .build())
                .build();

        sampleService.modifyEmployee(employee);
    }
}
