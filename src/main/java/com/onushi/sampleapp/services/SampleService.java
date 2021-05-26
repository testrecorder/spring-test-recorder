package com.onushi.sampleapp.services;

import com.onushi.sampleapp.model.*;
import com.onushi.testrecording.aspect.RecordTest;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class SampleService {
    public int testField = 0;

    public int getTestField() {
        return testField;
    }

    public SampleService setTestField(int testField) {
        this.testField = testField;
        return this;
    }

    @RecordTest
    public int add(int x, int y) {
        return addInternal(x, y);
    }

    public int addInternal(int x, int y) {
        return x + y;
    }

    public int negate(int x) {
        return -x;
    }

    public int returnZero() {
        return 0;
    }

    public Person returnNull() {
        return null;
    }

    public float addFloats(float x, float y) {
        return x + y;
    }

    public Integer addStrings(String a, String b) {
        return Integer.parseInt(a) + Integer.parseInt(b);
    }

    public boolean logicalAnd(boolean a, boolean b) {
        return a && b;
    }

    public String toYYYY_MM_DD_T_HH_MM_SS_Z(Date date, Date date1) {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        df.setTimeZone(tz);
        return df.format(date);
    }

    public int testTypes(short s, byte b, int a, boolean b1, char c, double d) {
        return 5;
    }

    public Date minDate(Date date1, Date date2) {
        return date1.before(date2) ? date1 : date2;
    }

    public String getFirstName(Person person) {
        return person.getFirstName();
    }

    public String testException(int x) {
        throw new IllegalArgumentException("x");
    }

    public void doNothing() {

    }

    public int processArrays(boolean[] boolArray, byte[] byteArray, char[] charArray,
                             double[] doubleArray, float[] floatArray, int[] intArray,
                             long[] longArray, short[] shortArray, String[] stringArray,
                             Object[] objectArray) {
        return 42;
    }

    public int[] returnIntArray() {
        return new int[]{3, 4};
    }


    public int repeatedArgs(int[] intArray, List<Float> floatList) {
        return 42;
    }

    public Person returnPerson() throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date dateOfBirth = simpleDateFormat.parse("2021-01-01");

        return Person.builder()
                .firstName("Gica")
                .lastName("Fulgerica")
                .dateOfBirth(dateOfBirth)
                .build();
    }


    public  List<Integer> getIntersection(List<Integer> list1, List<Integer> list2) {
        List<Integer> result = new ArrayList<>();

        for (Integer value : list1) {
            if(list2.contains(value)) {
                result.add(value);
            }
        }

        return result;
    }

    public void processStudents(StudentWithDefaultInitFields student1, StudentWithBuilder student2) {
    }

    public int demoFunction(List<Person> personList, OtherEmployee[] otherEmployeeArray) {
        return 42;
    }

    @RecordTest
    public int doSomething(String a, String b) {
        return 42;
    }

    public int testEnum(Color color) {
        return 2;
    }

    public int testUUID(UUID uuid) {
        return 2;
    }

    public int processMap(Map<String, List<String>> map) {
        return 42;
    }

    @RecordTest
    public float processSet(Set<Double> set) {
        return 42.42f;
    }
}
