package com.onushi.sampleapp;

import com.onushi.sampleapp.model.OtherEmployee;
import com.onushi.sampleapp.model.Person;
import com.onushi.sampleapp.services.PersonService;
import com.onushi.sampleapp.services.SalaryService;
import com.onushi.sampleapp.services.SampleService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class SampleAppRunner implements CommandLineRunner {
    private final SampleService sampleService;
    private final PersonService personService;
    private final SalaryService salaryService;

    public SampleAppRunner(SampleService sampleService, PersonService personService, SalaryService salaryService) {
        this.sampleService = sampleService;
        this.personService = personService;
        this.salaryService = salaryService;
    }

    @Override
    public void run(String... args) throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        Date d1 = simpleDateFormat.parse("2021-01-01");
//        Date min = sampleService.minDate(d1, d1);
//
//        Date d3 = simpleDateFormat.parse("2021-01-02");
//        Date d4 = simpleDateFormat.parse("2021-02-03");
//        Date min1 = sampleService.minDate(d3, d4);
//
//        sampleService.addInternal(5, 6);
//        sampleService.add(5, 6);
//        sampleService.addFloats(2f, 3f);
//        sampleService.logicalAnd(true, true);
//        sampleService.toYYYY_MM_DD_T_HH_MM_SS_Z(new Date(), new Date());
//        sampleService.testTypes((short)6, (byte)4, 5, true, 'c', 1.5);
//
//         personService.loadPerson(1);
//
//         sampleService.testException(5);
//
//        Person person = Person.builder()
//                .isActor(true)
//                .build();
//
//        boolean[] boolArray = {true, false};
//        byte[] byteArray = {1, 2};
//        char[] charArray = {'a', 'z'};
//        double[] doubleArray = {1.0, 2.0};
//        float[] floatArray = {1.0f, 2.0f};
//        int[] intArray = {3, 4};
//        long[] longArray = {3L, 4L};
//        short[] shortArray = {3, 4};
//        String[] stringArray = {"a", "z"};
//        Object[] objectArray = {"a", 2};
//        sampleService.processArrays(boolArray, byteArray, charArray, doubleArray, floatArray, intArray, longArray, shortArray, stringArray, objectArray);
//
//         sampleService.returnPerson();
//
//        List<Float> floatList = Arrays.asList(3.0f, 3.0f);
//        int[] intArray = {3, 4, 3};
//        sampleService.repeatedArgs(intArray, floatList);
//
//        StudentWithDefaultInitFields student1 = new StudentWithDefaultInitFields();
//        StudentWithBuilder student2 = StudentWithBuilder.builder()
//                .firstName("John")
//                .lastName("Wayne")
//                .age(60)
//                .build();
//        sampleService.processStudents(student1, student2);
//
//        sampleService.setTestField(5);
//        float result = sampleService.addFloats(1.0f, 2.0f);

        // demo1();
        // demo3();

        // sampleService.getIntersection(Arrays.asList(10, 20, 5), Arrays.asList(1, 2, 3, 4, 5));

        // int a = sampleService.testEnum(Color.BLUE);

        // int a = sampleService.testUUID(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));

//        Map<String, List<String>> map = new HashMap<>();
//        map.put("1", Arrays.asList("0", "1"));
//        map.put("2", Arrays.asList("0", "1", "2"));
//        map.put("3", Arrays.asList("0", "1", "2", "3"));
//        int a = sampleService.processMap(map);

        Set<Double> set = new HashSet<>();
        set.add(null);
        set.add(1.2);
        set.add(2.6);
        sampleService.processSet(set);
    }

    private void demo1() throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date dateOfBirth1 = simpleDateFormat.parse("1980-01-02");
        Date dateOfBirth2 = simpleDateFormat.parse("1970-02-03");
        Person marco = Person.builder()
                .firstName("Marco")
                .lastName("Polo")
                .dateOfBirth(dateOfBirth1)
                .build();

        Person gica = Person.builder()
                .firstName("Gica")
                .lastName("Fulgerica")
                .dateOfBirth(dateOfBirth2)
                .build();

        OtherEmployee otherEmployee = new OtherEmployee()
                .setName("Jack Norton")
                .setAge(23)
                .setStudent(true);

        OtherEmployee[] otherEmployeeArray = {otherEmployee};
        this.sampleService.demoFunction(Arrays.asList(gica, marco), otherEmployeeArray);
    }

    private void demo2() throws Exception {
        String personFirstName = this.personService.getPersonFirstName(2);
    }

    private void demo3() throws Exception {
        salaryService.computeEmployeeSalary(1);
    }
}
