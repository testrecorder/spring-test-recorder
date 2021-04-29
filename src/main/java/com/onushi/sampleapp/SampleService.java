package com.onushi.sampleapp;

import com.onushi.testrecording.aspect.RecordTest;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@Component
public class SampleService {
    public final int testField = 5;

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

    // TODO !!!! also returns void
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

    public Person[] someFunction(List<Person> personList, Person[] dateArray) throws ParseException {
        // ....
        // ...
        return dateArray;
    }

    public int[] returnIntArray() {
        int[] intArray = {3, 4};
        return intArray;
    }

    @RecordTest
    public Person returnPerson() throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date dateOfBirth = simpleDateFormat.parse("2021-01-01");

        return Person.builder()
                .firstName("Gica")
                .lastName("Fulgerica")
                .dateOfBirth(dateOfBirth)
                .build();

    }

}
