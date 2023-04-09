/*
 *
 * Copyright (c) 2021 spring-test-recorder contributors
 * This program is made available under the terms of the MIT License.
 *
 */

package org.sample.services;

import org.springtestrecorder.aspect.RecordTest;
import org.sample.model.*;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@SuppressWarnings("ALL")
@Component
public class SampleService {
    public int add(int x, int y) {
        return addInternal(x, y);
    }

    public int addInternal(int x, int y) {
        return x + y;
    }

    public PersonRepository returnNull() {
        return null;
    }

    public float addFloats(float x, float y) {
        return x + y;
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
                .firstName("Tom")
                .lastName("Richardson")
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

    public int processMap(Map<String, List<String>> map) {
        return 42;
    }


    public float processSet(Set<Double> set) {
        return 42.42f;
    }

    @RecordTest
    public void modifyEmployee(Employee employee) {
        employee.setSalaryParam1(employee.getSalaryParam1() * 2f);
        employee.setSalaryParam2(employee.getSalaryParam2() * 3f);
        Department department = employee.getDepartment();
        department.setName(department.getName() + " New");
    }

    // TODO if i rename this to getEmployee tests are failing. Solve this
    public Employee createEmployee() {
        return Employee.builder()
                .id(1)
                .firstName("John")
                .lastName("Doe")
                .salaryParam1(1000)
                .salaryParam2(1500)
                .teamColor(Color.BLUE)
                .department(Department.builder()
                        .id(100)
                        .name("IT")
                        .build())
                .build();
    }

    public List<List<Person>> createListOfPersonList() throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date dateOfBirth1 = simpleDateFormat.parse("1980-01-02");
        Date dateOfBirth2 = simpleDateFormat.parse("1970-02-03");
        Person paul = Person.builder()
                .firstName("Paul")
                .lastName("Thompson")
                .dateOfBirth(dateOfBirth1)
                .build();

        Person tom = Person.builder()
                .firstName("Tom")
                .lastName("Richardson")
                .dateOfBirth(dateOfBirth2)
                .build();

        List<Person> personList = Arrays.asList(paul, tom);
        return Arrays.asList(personList, null);
    }

    public List<Integer> createList() {
        return Arrays.asList(1, 2, 3);
    }

    public int processCyclicObjects(CyclicParent cyclicParent) {
        return 42;
    }

    public CyclicParent createCyclicObjects() throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        CyclicParent cyclicParent = new CyclicParent();
        CyclicChild cyclicChild = new CyclicChild();
        cyclicChild.parent = cyclicParent;
        cyclicChild.date = simpleDateFormat.parse("1980-01-02");
        cyclicParent.id = 1;
        cyclicParent.childList = Collections.singletonList(cyclicChild);
        return cyclicParent;
    }

    public Set<Person> createPersonHashSet() throws ParseException {
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

        Set<Person> personSet = new HashSet<>();
        personSet.add(marco);
        personSet.add(tom);

        return personSet;
    }

    public Map<Person, Person> createPersonHashMap() throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date dateOfBirth1 = simpleDateFormat.parse("1970-01-01");
        Date dateOfBirth2 = simpleDateFormat.parse("1971-01-01");
        Date dateOfBirth3 = simpleDateFormat.parse("1920-01-01");
        Date dateOfBirth4 = simpleDateFormat.parse("1921-01-01");
        Person marco = Person.builder()
                .firstName("Marco")
                .lastName("Polo")
                .dateOfBirth(dateOfBirth1)
                .build();

        Person marcosFather = Person.builder()
                .firstName("Marco'")
                .lastName("Father")
                .dateOfBirth(dateOfBirth3)
                .build();

        Person tom = Person.builder()
                .firstName("Tom")
                .lastName("Richardson")
                .dateOfBirth(dateOfBirth2)
                .build();

        Person tomsFather = Person.builder()
                .firstName("Tom's")
                .lastName("Father")
                .dateOfBirth(dateOfBirth4)
                .build();

        Map<Person, Person> personMap = new HashMap<>();
        personMap.put(marco, marcosFather);
        personMap.put(tom, tomsFather);

        return personMap;
    }

    public int changeArrayList(List<Float> floatList) {
        floatList.add(1, 1f);
        return 42;
    }

    public int changeDate(Date date) {
        date.setTime(date.getTime() + 60*60*1000);
        return 42;
    }

    public Integer changeArray(char[] array) {
        array[0] = 'b';
        return 42;
    }

    public Integer changeHashMap(Map<Person, Person> personsHashMap) throws ParseException {
        personsHashMap.clear();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date dateOfBirth1 = simpleDateFormat.parse("1970-02-02");
        Date dateOfBirth2 = simpleDateFormat.parse("1970-03-03");
        Person son = Person.builder()
                .firstName("FN")
                .lastName("LN")
                .dateOfBirth(dateOfBirth1)
                .build();

        Person father = Person.builder()
                .firstName("FN1'")
                .lastName("LN1")
                .dateOfBirth(dateOfBirth2)
                .build();

        personsHashMap.put(son, father);
        return 42;
    }

    public Integer changeHashSet(Set<Person> hashSet) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date dateOfBirth = simpleDateFormat.parse("1980-01-03");
        Person person = Person.builder()
                .firstName("FN")
                .lastName("LN")
                .dateOfBirth(dateOfBirth)
                .build();

        hashSet.add(person);

        return 42;
    }

    public Integer processLists(List<String> arrayList1, List<Object> arrayList2) {
        return 42;
    }

    public List<Person> someFunction(List<Person> personsList, Person[] personArray) {
        return personsList;
    }

    public void changeFloat(Float f1) {
        f1 = 5.0f;
    }

    public void changeString(String string) {
        string = "new string";
    }

    public void changeEnum(Color color) {
        color = Color.GREEN;
    }
}
