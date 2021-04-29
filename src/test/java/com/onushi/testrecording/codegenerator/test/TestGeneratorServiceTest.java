package com.onushi.testrecording.codegenerator.test;

import com.onushi.sampleapp.Person;
import com.onushi.sampleapp.SampleService;
import com.onushi.testrecording.analizer.classInfo.ClassInfoService;
import com.onushi.testrecording.analizer.methodrun.MethodRunInfo;
import com.onushi.testrecording.analizer.object.ObjectStateReaderService;
import com.onushi.testrecording.codegenerator.object.ObjectCodeGeneratorFactory;
import com.onushi.testrecording.codegenerator.template.StringService;
import com.onushi.sampleapp.Student;
import com.onushi.testrecording.utils.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TestGeneratorServiceTest {
    TestGeneratorFactory testGeneratorFactory;
    TestGeneratorService testGeneratorService;

    @BeforeEach
    void setUp() {
        ClassInfoService classInfoService = new ClassInfoService();
        ObjectNameGenerator objectNameGenerator = new ObjectNameGenerator();
        ObjectCodeGeneratorFactory objectCodeGeneratorFactory = new ObjectCodeGeneratorFactory(classInfoService,
                new ObjectStateReaderService(objectNameGenerator),
                objectNameGenerator);
        testGeneratorFactory = new TestGeneratorFactory(objectNameGenerator, objectCodeGeneratorFactory);
        testGeneratorService = new TestGeneratorService(new StringService());
    }

    @Test
    void generateTestForAddFloats() throws Exception {
        // Arrange
        MethodRunInfo methodRunInfo = MethodRunInfo.builder()
                .target(new SampleService())
                .methodName("addFloats")
                .arguments(Arrays.asList(2f, 3f))
                .result(5f)
                .build();
        TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(methodRunInfo);

        // Act
        String testString = testGeneratorService.generateTestCode(testGenerator);

        // Assert
        assertEquals(StringUtils.trimAndIgnoreCRDiffs("BEGIN GENERATED TEST =========\n" +
                        "\n" +
                        "package com.onushi.sampleapp;\n" +
                        "\n" +
                        "import org.junit.jupiter.api.Test;\n" +
                        "import static org.junit.jupiter.api.Assertions.*;\n" +
                        "\n" +
                        "class SampleServiceTest {\n" +
                        "    @Test\n" +
                        "    void addFloats() throws Exception {\n" +
                        "        // Arrange\n" +
                        "        SampleService sampleService = new SampleService();\n" +
                        "\n" +
                        "        // Act\n" +
                        "        float result = sampleService.addFloats(2.0f, 3.0f);\n" +
                        "\n" +
                        "        // Assert\n" +
                        "        assertEquals(5.0f, result);\n" +
                        "    }\n" +
                        "}\n" +
                        "\n" +
                        "END GENERATED TEST ========="),
                StringUtils.trimAndIgnoreCRDiffs(testString));
    }

    @Test
    void generateTestForMinDate() throws Exception {
        // Arrange
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date d1 = simpleDateFormat.parse("2021-01-01");
        Date d2 = simpleDateFormat.parse("2021-02-02");
        MethodRunInfo methodRunInfo = MethodRunInfo.builder()
                .target(new SampleService())
                .methodName("minDate")
                .arguments(Arrays.asList(d1, d2))
                .result(d1)
                .build();
        TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(methodRunInfo);

        // Act
        String testString = testGeneratorService.generateTestCode(testGenerator);

        // Assert
        assertEquals(StringUtils.trimAndIgnoreCRDiffs("BEGIN GENERATED TEST =========\n" +
                        "\n" +
                        "package com.onushi.sampleapp;\n" +
                        "\n" +
                        "import org.junit.jupiter.api.Test;\n" +
                        "import static org.junit.jupiter.api.Assertions.*;\n" +
                        "import java.text.SimpleDateFormat;\n" +
                        "import java.util.Date;\n" +
                        "\n" +
                        "class SampleServiceTest {\n" +
                        "    @Test\n" +
                        "    void minDate() throws Exception {\n" +
                        "        // Arrange\n" +
                        "        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss.SSS\");\n" +
                        "        Date date1 = simpleDateFormat.parse(\"2021-01-01 00:00:00.000\");\n" +
                        "        Date date2 = simpleDateFormat.parse(\"2021-02-02 00:00:00.000\");\n" +
                        "        SampleService sampleService = new SampleService();\n" +
                        "\n" +
                        "        // Act\n" +
                        "        Date result = sampleService.minDate(date1, date2);\n" +
                        "\n" +
                        "        // Assert\n" +
                        "        Date expectedResult = simpleDateFormat.parse(\"2021-01-01 00:00:00.000\");\n" +
                        "        assertEquals(expectedResult, result);\n" +
                        "    }\n" +
                        "}\n" +
                        "\n" +
                        "END GENERATED TEST ========="),
                StringUtils.trimAndIgnoreCRDiffs(testString));
    }

    @Test
    void generateTestForReturnNull() throws Exception {
        // Arrange
        MethodRunInfo methodRunInfo = MethodRunInfo.builder()
                .target(new SampleService())
                .methodName("returnNull")
                .arguments(Collections.emptyList())
                .result(null)
                .fallBackResultType(Student.class)
                .build();
        TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(methodRunInfo);

        // Act
        String testString = testGeneratorService.generateTestCode(testGenerator);

        // Assert
        assertEquals(StringUtils.trimAndIgnoreCRDiffs("BEGIN GENERATED TEST =========\n" +
                        "\n" +
                        "package com.onushi.sampleapp;\n" +
                        "\n" +
                        "import org.junit.jupiter.api.Test;\n" +
                        "import static org.junit.jupiter.api.Assertions.*;\n" +
                        "\n" +
                        "class SampleServiceTest {\n" +
                        "    @Test\n" +
                        "    void returnNull() throws Exception {\n" +
                        "        // Arrange\n" +
                        "        SampleService sampleService = new SampleService();\n" +
                        "\n" +
                        "        // Act\n" +
                        "        Student result = sampleService.returnNull();\n" +
                        "\n" +
                        "        // Assert\n" +
                        "        assertEquals(null, result);\n" +
                        "    }\n" +
                        "}\n" +
                        "\n" +
                        "END GENERATED TEST ========="),
                StringUtils.trimAndIgnoreCRDiffs(testString));

    }

    @Test
    void generateTestForWhenHavingObjectAsArgument() throws Exception {
        // Arrange
        Person person = Person.builder()
                .firstName("Mary")
                .lastName("Poe")
                .build();
        MethodRunInfo methodRunInfo = MethodRunInfo.builder()
                .target(new SampleService())
                .methodName("getFirstName")
                .arguments(Collections.singletonList(person))
                .result("Mary")
                .build();
        TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(methodRunInfo);

        // Act
        String testString = testGeneratorService.generateTestCode(testGenerator);

        // Assert
        assertEquals(StringUtils.trimAndIgnoreCRDiffs("BEGIN GENERATED TEST =========\n" +
                        "\n" +
                        "package com.onushi.sampleapp;\n" +
                        "\n" +
                        "import org.junit.jupiter.api.Test;\n" +
                        "import static org.junit.jupiter.api.Assertions.*;\n" +
                        "import com.onushi.sampleapp.Person;\n" +
                        "\n" +
                        "class SampleServiceTest {\n" +
                        "    @Test\n" +
                        "    void getFirstName() throws Exception {\n" +
                        "        // Arrange\n" +
                        "        Person person1 = Person.builder()\n" +
                        "            .dateOfBirth(null)\n" +
                        "            .firstName(\"Mary\")\n" +
                        "            .lastName(\"Poe\")\n" +
                        "            .build();\n" +
                        "        SampleService sampleService = new SampleService();\n" +
                        "\n" +
                        "        // Act\n" +
                        "        String result = sampleService.getFirstName(person1);\n" +
                        "\n" +
                        "        // Assert\n" +
                        "        assertEquals(\"Mary\", result);\n" +
                        "    }\n" +
                        "}\n" +
                        "\n" +
                        "END GENERATED TEST ========="),
                StringUtils.trimAndIgnoreCRDiffs(testString));
    }

    @Test
    void generateTestWhenExceptionIsThrown() throws Exception {
        // Arrange
        MethodRunInfo methodRunInfo = MethodRunInfo.builder()
                .target(new SampleService())
                .methodName("testException")
                .arguments(Collections.singletonList(5))
                .result(null)
                .fallBackResultType(String.class)
                .exception(new IllegalArgumentException("x"))
                .build();
        TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(methodRunInfo);

        // Act
        String testString = testGeneratorService.generateTestCode(testGenerator);

        // Assert
        assertEquals(StringUtils.trimAndIgnoreCRDiffs("BEGIN GENERATED TEST =========\n" +
                        "\n" +
                        "package com.onushi.sampleapp;\n" +
                        "\n" +
                        "import org.junit.jupiter.api.Test;\n" +
                        "import static org.junit.jupiter.api.Assertions.*;\n" +
                        "\n" +
                        "class SampleServiceTest {\n" +
                        "    @Test\n" +
                        "    void testException() throws Exception {\n" +
                        "        // Arrange\n" +
                        "        SampleService sampleService = new SampleService();\n" +
                        "\n" +
                        "        // Act & Assert\n" +
                        "        assertThrows(java.lang.IllegalArgumentException.class, () -> sampleService.testException(5));\n" +
                        "    }\n" +
                        "}\n" +
                        "\n" +
                        "END GENERATED TEST ========="),
                StringUtils.trimAndIgnoreCRDiffs(testString));
    }

    @Test
    void generateTestWhenResultIsVoid() throws Exception {
        // Arrange
        MethodRunInfo methodRunInfo = MethodRunInfo.builder()
                .target(new SampleService())
                .methodName("doNothing")
                .arguments(Collections.emptyList())
                .result(null)
                .fallBackResultType(void.class)
                .exception(null)
                .build();
        TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(methodRunInfo);

        // Act
        String testString = testGeneratorService.generateTestCode(testGenerator);

        // Assert
        assertEquals(StringUtils.trimAndIgnoreCRDiffs("BEGIN GENERATED TEST =========\n" +
                        "\n" +
                        "package com.onushi.sampleapp;\n" +
                        "\n" +
                        "import org.junit.jupiter.api.Test;\n" +
                        "import static org.junit.jupiter.api.Assertions.*;\n" +
                        "\n" +
                        "class SampleServiceTest {\n" +
                        "    @Test\n" +
                        "    void doNothing() throws Exception {\n" +
                        "        // Arrange\n" +
                        "        SampleService sampleService = new SampleService();\n" +
                        "\n" +
                        "        // Act\n" +
                        "        sampleService.doNothing();\n" +
                        "\n" +
                        "    }\n" +
                        "}\n" +
                        "\n" +
                        "END GENERATED TEST ========="),
                StringUtils.trimAndIgnoreCRDiffs(testString));
    }

    @Test
    void generateTestForArrays() throws Exception {
        // Arrange
        boolean[] boolArray = {true, false};
        byte[] byteArray = {1, 2};
        char[] charArray = {'a', 'z'};
        double[] doubleArray = {1.0, 2.0};
        float[] floatArray = {1.0f, 2.0f};
        int[] intArray = {3, 4};
        long[] longArray = {3L, 4L};
        short[] shortArray = {3, 4};
        String[] stringArray = {"a", "z"};
        Object[] objectArray = {"a", 2};
        MethodRunInfo methodRunInfo = MethodRunInfo.builder()
                .target(new SampleService())
                .methodName("processArrays")
                .arguments(Arrays.asList(boolArray, byteArray, charArray, doubleArray,
                        floatArray, intArray, longArray, shortArray, stringArray, objectArray))
                .result(42)
                .exception(null)
                .build();
        TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(methodRunInfo);

        // Act
        String testString = testGeneratorService.generateTestCode(testGenerator);

        // Assert
        assertEquals(StringUtils.trimAndIgnoreCRDiffs("BEGIN GENERATED TEST =========\n" +
                        "\n" +
                        "package com.onushi.sampleapp;\n" +
                        "\n" +
                        "import org.junit.jupiter.api.Test;\n" +
                        "import static org.junit.jupiter.api.Assertions.*;\n" +
                        "\n" +
                        "class SampleServiceTest {\n" +
                        "    @Test\n" +
                        "    void processArrays() throws Exception {\n" +
                        "        // Arrange\n" +
                        "        boolean[] array1 = {true, false};\n" +
                        "        byte[] array2 = {(byte)1, (byte)2};\n" +
                        "        char[] array3 = {'a', 'z'};\n" +
                        "        double[] array4 = {1.0, 2.0};\n" +
                        "        float[] array5 = {1.0f, 2.0f};\n" +
                        "        int[] array6 = {3, 4};\n" +
                        "        long[] array7 = {3L, 4L};\n" +
                        "        short[] array8 = {(short)3, (short)4};\n" +
                        "        String[] array9 = {\"a\", \"z\"};\n" +
                        "        Object[] array10 = {\"a\", 2};\n" +
                        "        SampleService sampleService = new SampleService();\n" +
                        "\n" +
                        "        // Act\n" +
                        "        int result = sampleService.processArrays(array1, array2, array3, array4, array5, array6, array7, array8, array9, array10);\n" +
                        "\n" +
                        "        // Assert\n" +
                        "        assertEquals(42, result);\n" +
                        "    }\n" +
                        "}\n" +
                        "\n" +
                        "END GENERATED TEST ========="),
                StringUtils.trimAndIgnoreCRDiffs(testString));
    }

    @Test
    void generateTestForArrayLists() throws Exception {
        // Arrange
        List<String> stringList = Arrays.asList("a", "b");
        List<Object> objectList = Arrays.asList(1, "b", null);
        MethodRunInfo methodRunInfo = MethodRunInfo.builder()
                .target(new SampleService())
                .methodName("processLists")
                .arguments(Arrays.asList(stringList, objectList))
                .result(42)
                .build();
        TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(methodRunInfo);

        // Act
        String testString = testGeneratorService.generateTestCode(testGenerator);

        // Assert
        assertEquals(StringUtils.trimAndIgnoreCRDiffs("BEGIN GENERATED TEST =========\n" +
                        "\n" +
                        "package com.onushi.sampleapp;\n" +
                        "\n" +
                        "import org.junit.jupiter.api.Test;\n" +
                        "import static org.junit.jupiter.api.Assertions.*;\n" +
                        "import java.util.List;\n" +
                        "import java.util.Arrays;\n" +
                        "\n" +
                        "class SampleServiceTest {\n" +
                        "    @Test\n" +
                        "    void processLists() throws Exception {\n" +
                        "        // Arrange\n" +
                        "        List<String> arrayList1 =  Arrays.asList(\"a\", \"b\");\n" +
                        "        List<Object> arrayList2 =  Arrays.asList(1, \"b\", null);\n" +
                        "        SampleService sampleService = new SampleService();\n" +
                        "\n" +
                        "        // Act\n" +
                        "        int result = sampleService.processLists(arrayList1, arrayList2);\n" +
                        "\n" +
                        "        // Assert\n" +
                        "        assertEquals(42, result);\n" +
                        "    }\n" +
                        "}\n" +
                        "\n" +
                        "END GENERATED TEST =========\n"),
                StringUtils.trimAndIgnoreCRDiffs(testString));
    }





    @Test
    void testObjectCaching() throws Exception {
        // Arrange
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date d1 = simpleDateFormat.parse("2021-01-01");
        MethodRunInfo methodRunInfo = MethodRunInfo.builder()
                .target(new SampleService())
                .methodName("minDate")
                .arguments(Arrays.asList(d1, d1))
                .result(d1)
                .build();
        TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(methodRunInfo);

        // Act
        String testString = testGeneratorService.generateTestCode(testGenerator);

        // Assert
        assertEquals(StringUtils.trimAndIgnoreCRDiffs("BEGIN GENERATED TEST =========\n" +
                        "\n" +
                        "package com.onushi.sampleapp;\n" +
                        "\n" +
                        "import org.junit.jupiter.api.Test;\n" +
                        "import static org.junit.jupiter.api.Assertions.*;\n" +
                        "import java.text.SimpleDateFormat;\n" +
                        "import java.util.Date;\n" +
                        "\n" +
                        "class SampleServiceTest {\n" +
                        "    @Test\n" +
                        "    void minDate() throws Exception {\n" +
                        "        // Arrange\n" +
                        "        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss.SSS\");\n" +
                        "        Date date1 = simpleDateFormat.parse(\"2021-01-01 00:00:00.000\");\n" +
                        "        SampleService sampleService = new SampleService();\n" +
                        "\n" +
                        "        // Act\n" +
                        "        Date result = sampleService.minDate(date1, date1);\n" +
                        "\n" +
                        "        // Assert\n" +
                        "        Date expectedResult = simpleDateFormat.parse(\"2021-01-01 00:00:00.000\");\n" +
                        "        assertEquals(expectedResult, result);\n" +
                        "    }\n" +
                        "}\n" +
                        "\n" +
                        "END GENERATED TEST ========="),
                StringUtils.trimAndIgnoreCRDiffs(testString));
    }

    @Test
    void testDependenciesOfDependencies() throws Exception {
        // Arrange
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

        List<Person> personList = Arrays.asList(paul, gica);
        Person[] personArray = {paul, gica};

        MethodRunInfo methodRunInfo = MethodRunInfo.builder()
                .target(new SampleService())
                .methodName("someFunction")
                .arguments(Arrays.asList(personList, personArray))
                .result(personList)
                .build();
        TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(methodRunInfo);

        // Act
        String testString = testGeneratorService.generateTestCode(testGenerator);

        // Assert
        assertEquals(StringUtils.trimAndIgnoreCRDiffs("package com.onushi.sampleapp;\n" +
                    "\n" +
                    "import org.junit.jupiter.api.Test;\n" +
                    "import static org.junit.jupiter.api.Assertions.*;\n" +
                    "import java.text.SimpleDateFormat;\n" +
                    "import java.util.Date;\n" +
                    "import java.util.List;\n" +
                    "import java.util.Arrays;\n" +
                    "import com.onushi.sampleapp.Person;\n" +
                    "\n" +
                    "class SampleServiceTest {\n" +
                    "    @Test\n" +
                    "    void someFunction() throws Exception {\n" +
                    "        // Arrange\n" +
                    "        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss.SSS\");\n" +
                    "        Date date1 = simpleDateFormat.parse(\"1980-01-02 00:00:00.000\");\n" +
                    "        Person person1 = Person.builder()\n" +
                    "            .dateOfBirth(date1)\n" +
                    "            .firstName(\"Paul\")\n" +
                    "            .lastName(\"Marculescu\")\n" +
                    "            .build();\n" +
                    "        Date date2 = simpleDateFormat.parse(\"1970-02-03 00:00:00.000\");\n" +
                    "        Person person2 = Person.builder()\n" +
                    "            .dateOfBirth(date2)\n" +
                    "            .firstName(\"Gica\")\n" +
                    "            .lastName(\"Fulgerica\")\n" +
                    "            .build();\n" +
                    "        List<Person> arrayList1 =  Arrays.asList(person1, person2);\n" +
                    "        Person[] array1 = {person1, person2};\n" +
                    "        SampleService sampleService = new SampleService();\n" +
                    "\n" +
                    "        // Act\n" +
                    "        List<Person> result = sampleService.someFunction(arrayList1, array1);\n" +
                    "\n" +
                    "        // Assert\n" +
                    "        List<Person> expectedResult =  Arrays.asList(person1, person2);\n" +
                    "        assertEquals(expectedResult, result);\n" +
                    "    }\n" +
                    "}"),
                StringUtils.trimAndIgnoreCRDiffs(testString));
    }



// TODO IB activate after we implemented mocking
//    @Test
//    void generateTestForResultCreatedWithBuilder() throws Exception {
//        // Arrange
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        Person person = Person.builder()
//                .firstName("Chuck")
//                .lastName("Norris")
//                .dateOfBirth(simpleDateFormat.parse("1940-03-10"))
//                .build();
//        MethodRunInfo methodRunInfo = MethodRunInfo.builder()
//                .target(new PersonService(new PersonRepositoryImpl()))
//                .methodName("loadPerson")
//                .arguments(Collections.singletonList(1))
//                .result(person)
//                .resultType(Person.class)
//                .build();
//
//        TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(methodRunInfo);
//
//        // Act
//        String testString = testGeneratorService.generateTestCode(testGenerator);
//
//        // Assert
//        assertEquals(StringUtils.trimAndIgnoreCRDiffs(""),
//                StringUtils.trimAndIgnoreCRDiffs(testString));
//    }
}
