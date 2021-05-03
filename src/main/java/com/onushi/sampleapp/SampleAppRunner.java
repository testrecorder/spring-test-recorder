package com.onushi.sampleapp;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
public class SampleAppRunner implements CommandLineRunner {
    private final SampleService sampleService;
    private final PersonService personService;

    public SampleAppRunner(SampleService sampleService, PersonService personService) {
        this.sampleService = sampleService;
        this.personService = personService;
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

        // personService.loadPerson(1);

        // sampleService.testException(5);

//        Person person = Person.builder()
//                .isActor(true)
//                .build();

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

        // sampleService.returnPerson();

//        List<Float> floatList = Arrays.asList(3.0f, 3.0f);
//        int[] intArray = {3, 4, 3};
//        sampleService.repeatedArgs(intArray, floatList);

//        StudentWithDefaultInitFields student1 = new StudentWithDefaultInitFields();
//        StudentWithBuilder student2 = StudentWithBuilder.builder()
//                .firstName("John")
//                .lastName("Wayne")
//                .age(60)
//                .build();
//        sampleService.processStudents(student1, student2);

//        sampleService.setTestField(5);
//        float result = sampleService.addFloats(1.0f, 2.0f);

        // demo();

    }

    private void demo() throws Exception {
        //
        // ...
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date dateOfBirth1 = simpleDateFormat.parse("1980-01-02");
        Date dateOfBirth2 = simpleDateFormat.parse("1970-02-03");
        Person paul = Person.builder()
                .firstName("Paul")
                .lastName("Marculescu")
                .dateOfBirth(dateOfBirth1)
                .build();

        Person gica = Person.builder()
                .firstName("Gica")
                .lastName("Fulgerica")
                .dateOfBirth(dateOfBirth2)
                .build();

        List<Person> personlList = Arrays.asList(paul, gica);
        Person[] personArray = {paul, gica};

        this.sampleService.someFunction(personlList, personArray);
        // ...
        //
        //
        //
    }
}
