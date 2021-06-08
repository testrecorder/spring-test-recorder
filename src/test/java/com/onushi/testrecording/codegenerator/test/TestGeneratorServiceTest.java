package com.onushi.testrecording.codegenerator.test;

import com.onushi.sample.model.*;
import com.onushi.sample.services.PersonRepository;
import com.onushi.sample.services.PersonRepositoryImpl;
import com.onushi.sample.services.PersonService;
import com.onushi.sample.services.SampleService;
import com.onushi.testrecording.analyzer.methodrun.DependencyMethodRunInfo;
import com.onushi.testrecording.analyzer.methodrun.RecordedMethodRunInfo;
import com.onushi.testrecording.utils.ServiceCreatorUtils;
import com.onushi.testrecording.utils.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class TestGeneratorServiceTest {
    TestGeneratorFactory testGeneratorFactory;
    TestGeneratorService testGeneratorService;

    @BeforeEach
    void setUp() {
        testGeneratorFactory = ServiceCreatorUtils.createTestGeneratorFactory();
        testGeneratorService = ServiceCreatorUtils.createTestGeneratorService();
    }

    @Test
    void generateTestForAddFloats() {
        // Arrange
        RecordedMethodRunInfo recordedMethodRunInfo = RecordedMethodRunInfo.builder()
                .target(new SampleService())
                .methodName("addFloats")
                .arguments(Arrays.asList(2f, 3f))
                .result(5f)
                .dependencyMethodRuns(new ArrayList<>())
                .build();
        TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(recordedMethodRunInfo);

        // Act
        String testString = testGeneratorService.generateTestCode(testGenerator);

        // Assert
        assertEquals(StringUtils.prepareForCompare("BEGIN GENERATED TEST =========\n" +
                        "\n" +
                        "package com.onushi.sample.services;\n" +
                        "\n" +
                        "import org.junit.jupiter.api.Test;\n" +
                        "import static org.junit.jupiter.api.Assertions.*;\n" +
                        "\n" +
                        "class SampleServiceTest {\n" +
                        testGeneratorService.COMMENT_BEFORE_TEST +
                        "    @Test\n" +
                        "    void addFloats() throws Exception {\n" +
                        "        // Arrange\n" +
                        "        SampleService sampleService = new SampleService();\n" +
                        "\n" +
                        "        // Act\n" +
                        "        Float result = sampleService.addFloats(2.0f, 3.0f);\n" +
                        "\n" +
                        "        // Assert\n" +
                        "        assertEquals(5.0f, result);\n" +
                        "    }\n" +
                        "}\n" +
                        "\n" +
                        "END GENERATED TEST ========="),
                StringUtils.prepareForCompare(testString));
    }

    @Test
    void generateTestForMinDate() throws Exception {
        // Arrange
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date d1 = simpleDateFormat.parse("2021-01-01");
        Date d2 = simpleDateFormat.parse("2021-02-02");
        RecordedMethodRunInfo recordedMethodRunInfo = RecordedMethodRunInfo.builder()
                .target(new SampleService())
                .methodName("minDate")
                .arguments(Arrays.asList(d1, d2))
                .result(d1)
                .dependencyMethodRuns(new ArrayList<>())
                .build();
        TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(recordedMethodRunInfo);

        // Act
        String testString = testGeneratorService.generateTestCode(testGenerator);

        // Assert
        assertEquals(StringUtils.prepareForCompare("BEGIN GENERATED TEST =========\n" +
                        "\n" +
                        "package com.onushi.sample.services;\n" +
                        "\n" +
                        "import org.junit.jupiter.api.Test;\n" +
                        "import static org.junit.jupiter.api.Assertions.*;\n" +
                        "import java.util.Date;\n" +
                        "import java.text.SimpleDateFormat;\n" +
                        "\n" +
                        "class SampleServiceTest {\n" +
                        testGeneratorService.COMMENT_BEFORE_TEST +
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
                        "        assertEquals(date1, result);\n" +
                        "    }\n" +
                        "}\n" +
                        "\n" +
                        "END GENERATED TEST ========="),
                StringUtils.prepareForCompare(testString));
    }

    @Test
    void generateTestForReturnNull() {
        // Arrange
        RecordedMethodRunInfo recordedMethodRunInfo = RecordedMethodRunInfo.builder()
                .target(new SampleService())
                .methodName("returnNull")
                .arguments(Collections.emptyList())
                .result(null)
                .fallBackResultType(PersonRepository.class)
                .dependencyMethodRuns(new ArrayList<>())
                .build();
        TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(recordedMethodRunInfo);

        // Act
        String testString = testGeneratorService.generateTestCode(testGenerator);

        // Assert
        assertEquals(StringUtils.prepareForCompare("BEGIN GENERATED TEST =========\n" +
                        "\n" +
                        "package com.onushi.sample.services;\n" +
                        "\n" +
                        "import org.junit.jupiter.api.Test;\n" +
                        "import static org.junit.jupiter.api.Assertions.*;\n" +
                        "\n" +
                        "class SampleServiceTest {\n" +
                        testGeneratorService.COMMENT_BEFORE_TEST +
                        "    @Test\n" +
                        "    void returnNull() throws Exception {\n" +
                        "        // Arrange\n" +
                        "        SampleService sampleService = new SampleService();\n" +
                        "\n" +
                        "        // Act\n" +
                        "        PersonRepository result = sampleService.returnNull();\n" +
                        "\n" +
                        "        // Assert\n" +
                        "        assertNull(result);\n" +
                        "    }\n" +
                        "}\n" +
                        "\n" +
                        "END GENERATED TEST ========="),
                StringUtils.prepareForCompare(testString));

    }

    @Test
    void generateTestForWhenHavingObjectAsArgument() {
        // Arrange
        Person person = Person.builder()
                .firstName("Mary")
                .lastName("Poe")
                .build();
        RecordedMethodRunInfo recordedMethodRunInfo = RecordedMethodRunInfo.builder()
                .target(new SampleService())
                .methodName("getFirstName")
                .arguments(Collections.singletonList(person))
                .result("Mary")
                .dependencyMethodRuns(new ArrayList<>())
                .build();
        TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(recordedMethodRunInfo);

        // Act
        String testString = testGeneratorService.generateTestCode(testGenerator);

        // Assert
        assertEquals(StringUtils.prepareForCompare("BEGIN GENERATED TEST =========\n" +
                        "\n" +
                        "package com.onushi.sample.services;\n" +
                        "\n" +
                        "import org.junit.jupiter.api.Test;\n" +
                        "import static org.junit.jupiter.api.Assertions.*;\n" +
                        "import com.onushi.sample.model.Person;\n" +
                        "\n" +
                        "class SampleServiceTest {\n" +
                        testGeneratorService.COMMENT_BEFORE_TEST +
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
                StringUtils.prepareForCompare(testString));
    }

    @Test
    void generateTestWhenExceptionIsThrown() {
        // Arrange
        RecordedMethodRunInfo recordedMethodRunInfo = RecordedMethodRunInfo.builder()
                .target(new SampleService())
                .methodName("testException")
                .arguments(Collections.singletonList(5))
                .result(null)
                .fallBackResultType(String.class)
                .exception(new IllegalArgumentException("x"))
                .dependencyMethodRuns(new ArrayList<>())
                .build();
        TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(recordedMethodRunInfo);

        // Act
        String testString = testGeneratorService.generateTestCode(testGenerator);

        // Assert
        assertEquals(StringUtils.prepareForCompare("BEGIN GENERATED TEST =========\n" +
                        "\n" +
                        "package com.onushi.sample.services;\n" +
                        "\n" +
                        "import org.junit.jupiter.api.Test;\n" +
                        "import static org.junit.jupiter.api.Assertions.*;\n" +
                        "\n" +
                        "class SampleServiceTest {\n" +
                        testGeneratorService.COMMENT_BEFORE_TEST +
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
                StringUtils.prepareForCompare(testString));
    }

    @Test
    void generateTestWhenResultIsVoid() {
        // Arrange
        RecordedMethodRunInfo recordedMethodRunInfo = RecordedMethodRunInfo.builder()
                .target(new SampleService())
                .methodName("doNothing")
                .arguments(Collections.emptyList())
                .result(null)
                .fallBackResultType(void.class)
                .exception(null)
                .dependencyMethodRuns(new ArrayList<>())
                .build();
        TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(recordedMethodRunInfo);

        // Act
        String testString = testGeneratorService.generateTestCode(testGenerator);

        // Assert
        assertEquals(StringUtils.prepareForCompare("BEGIN GENERATED TEST =========\n" +
                        "\n" +
                        "package com.onushi.sample.services;\n" +
                        "\n" +
                        "import org.junit.jupiter.api.Test;\n" +
                        "import static org.junit.jupiter.api.Assertions.*;\n" +
                        "\n" +
                        "class SampleServiceTest {\n" +
                        testGeneratorService.COMMENT_BEFORE_TEST +
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
                StringUtils.prepareForCompare(testString));
    }

    @Test
    void generateTestForArrays() {
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
        RecordedMethodRunInfo recordedMethodRunInfo = RecordedMethodRunInfo.builder()
                .target(new SampleService())
                .methodName("processArrays")
                .arguments(Arrays.asList(boolArray, byteArray, charArray, doubleArray,
                        floatArray, intArray, longArray, shortArray, stringArray, objectArray))
                .result(42)
                .exception(null)
                .dependencyMethodRuns(new ArrayList<>())
                .build();
        TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(recordedMethodRunInfo);

        // Act
        String testString = testGeneratorService.generateTestCode(testGenerator);

        // Assert
        assertEquals(StringUtils.prepareForCompare("BEGIN GENERATED TEST =========\n" +
                        "\n" +
                        "package com.onushi.sample.services;\n" +
                        "\n" +
                        "import org.junit.jupiter.api.Test;\n" +
                        "import static org.junit.jupiter.api.Assertions.*;\n" +
                        "\n" +
                        "class SampleServiceTest {\n" +
                        testGeneratorService.COMMENT_BEFORE_TEST +
                        "    @Test\n" +
                        "    void processArrays() throws Exception {\n" +
                        "        // Arrange\n" +
                        "        boolean[] array1 = {true, false};\n" +
                        "\n" +
                        "        byte[] array2 = {(byte)1, (byte)2};\n" +
                        "\n" +
                        "        char[] array3 = {'a', 'z'};\n" +
                        "\n" +
                        "        double[] array4 = {1.0, 2.0};\n" +
                        "\n" +
                        "        float[] array5 = {1.0f, 2.0f};\n" +
                        "\n" +
                        "        int[] array6 = {3, 4};\n" +
                        "\n" +
                        "        long[] array7 = {3L, 4L};\n" +
                        "\n" +
                        "        short[] array8 = {(short)3, (short)4};\n" +
                        "\n" +
                        "        String[] array9 = {\"a\", \"z\"};\n" +
                        "\n" +
                        "        Object[] array10 = {\"a\", 2};\n" +
                        "\n" +
                        "        SampleService sampleService = new SampleService();\n" +
                        "\n" +
                        "        // Act\n" +
                        "        Integer result = sampleService.processArrays(array1, array2, array3, array4, array5, array6, array7, array8, array9, array10);\n" +
                        "\n" +
                        "        // Assert\n" +
                        "        assertEquals(42, result);\n" +
                        "    }\n" +
                        "}\n" +
                        "\n" +
                        "END GENERATED TEST ========="),
                StringUtils.prepareForCompare(testString));
    }

    @Test
    void generateTestForArrayLists() {
        // Arrange
        List<String> stringList = Arrays.asList("a", "b");
        List<Object> objectList = Arrays.asList(1, "b", null);
        RecordedMethodRunInfo recordedMethodRunInfo = RecordedMethodRunInfo.builder()
                .target(new SampleService())
                .methodName("processLists")
                .arguments(Arrays.asList(stringList, objectList))
                .result(42)
                .dependencyMethodRuns(new ArrayList<>())
                .build();
        TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(recordedMethodRunInfo);

        // Act
        String testString = testGeneratorService.generateTestCode(testGenerator);

        // Assert
        assertEquals(StringUtils.prepareForCompare("BEGIN GENERATED TEST =========\n" +
                        "\n" +
                        "package com.onushi.sample.services;\n" +
                        "\n" +
                        "import org.junit.jupiter.api.Test;\n" +
                        "import static org.junit.jupiter.api.Assertions.*;\n" +
                        "import java.util.List;\n" +
                        "import java.util.Arrays;\n" +
                        "\n" +
                        "class SampleServiceTest {\n" +
                        testGeneratorService.COMMENT_BEFORE_TEST +
                        "    @Test\n" +
                        "    void processLists() throws Exception {\n" +
                        "        // Arrange\n" +
                        "        List<String> arrayList1 = Arrays.asList(\"a\", \"b\");\n" +
                        "        List<Object> arrayList2 = Arrays.asList(1, \"b\", null);\n" +
                        "        SampleService sampleService = new SampleService();\n" +
                        "\n" +
                        "        // Act\n" +
                        "        Integer result = sampleService.processLists(arrayList1, arrayList2);\n" +
                        "\n" +
                        "        // Assert\n" +
                        "        assertEquals(42, result);\n" +
                        "    }\n" +
                        "}\n" +
                        "\n" +
                        "END GENERATED TEST =========\n"),
                StringUtils.prepareForCompare(testString));
    }

    @Test
    void testDependenciesOfDependencies() throws Exception {
        // Arrange
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
        Person[] personArray = {paul, tom};

        RecordedMethodRunInfo recordedMethodRunInfo = RecordedMethodRunInfo.builder()
                .target(new SampleService())
                .methodName("someFunction")
                .arguments(Arrays.asList(personList, personArray))
                .result(personList)
                .dependencyMethodRuns(new ArrayList<>())
                .build();
        TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(recordedMethodRunInfo);

        // Act
        String testString = testGeneratorService.generateTestCode(testGenerator);

        // Assert
        assertEquals(StringUtils.prepareForCompare("BEGIN GENERATED TEST =========\n" +
                        "\n" +
                        "package com.onushi.sample.services;\n" +
                        "\n" +
                        "import org.junit.jupiter.api.Test;\n" +
                        "import static org.junit.jupiter.api.Assertions.*;\n" +
                        "import java.util.List;\n" +
                        "import com.onushi.sample.model.Person;\n" +
                        "import java.util.Arrays;\n" +
                        "import java.util.Date;\n" +
                        "import java.text.SimpleDateFormat;\n" +
                        "\n" +
                        "class SampleServiceTest {\n" +
                        testGeneratorService.COMMENT_BEFORE_TEST +
                        "    @Test\n" +
                        "    void someFunction() throws Exception {\n" +
                        "        // Arrange\n" +
                        "        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss.SSS\");\n" +
                        "        Date date1 = simpleDateFormat.parse(\"1980-01-02 00:00:00.000\");\n" +
                        "        Person person1 = Person.builder()\n" +
                        "            .dateOfBirth(date1)\n" +
                        "            .firstName(\"Paul\")\n" +
                        "            .lastName(\"Thompson\")\n" +
                        "            .build();\n" +
                        "\n" +
                        "        Date date2 = simpleDateFormat.parse(\"1970-02-03 00:00:00.000\");\n" +
                        "        Person person2 = Person.builder()\n" +
                        "            .dateOfBirth(date2)\n" +
                        "            .firstName(\"Tom\")\n" +
                        "            .lastName(\"Richardson\")\n" +
                        "            .build();\n" +
                        "\n" +
                        "        List<Person> arrayList1 = Arrays.asList(person1, person2);\n" +
                        "\n" +
                        "        Person[] array1 = {person1, person2};\n" +
                        "\n" +
                        "        SampleService sampleService = new SampleService();\n" +
                        "\n" +
                        "        // Act\n" +
                        "        List<Person> result = sampleService.someFunction(arrayList1, array1);\n" +
                        "\n" +
                        "        // Assert\n" +
                        "        assertEquals(2, result.size());\n" +
                        "        assertEquals(person1, result.get(0));\n" +
                        "        assertEquals(person2, result.get(1));\n" +
                        "    }\n" +
                        "}\n" +
                        "\n" +
                        "END GENERATED TEST ========="),
                StringUtils.prepareForCompare(testString));
    }

    @Test
    void testListOfIntegers() {
        // Arrange
        RecordedMethodRunInfo recordedMethodRunInfo = RecordedMethodRunInfo.builder()
                .target(new SampleService())
                .methodName("createList")
                .arguments(Collections.emptyList())
                .result(Arrays.asList(1, 2, 3))
                .dependencyMethodRuns(new ArrayList<>())
                .build();
        TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(recordedMethodRunInfo);

        // Act
        String testString = testGeneratorService.generateTestCode(testGenerator);

        // Assert
        assertEquals(StringUtils.prepareForCompare("BEGIN GENERATED TEST =========\n" +
                        "\n" +
                        "package com.onushi.sample.services;\n" +
                        "\n" +
                        "import org.junit.jupiter.api.Test;\n" +
                        "import static org.junit.jupiter.api.Assertions.*;\n" +
                        "import java.util.List;\n" +
                        "\n" +
                        "class SampleServiceTest {\n" +
                        testGeneratorService.COMMENT_BEFORE_TEST +
                        "    @Test\n" +
                        "    void createList() throws Exception {\n" +
                        "        // Arrange\n" +
                        "        SampleService sampleService = new SampleService();\n" +
                        "\n" +
                        "        // Act\n" +
                        "        List<Integer> result = sampleService.createList();\n" +
                        "\n" +
                        "        // Assert\n" +
                        "        assertEquals(3, result.size());\n" +
                        "        assertEquals(1, result.get(0));\n" +
                        "        assertEquals(2, result.get(1));\n" +
                        "        assertEquals(3, result.get(2));\n" +
                        "    }\n" +
                        "}\n" +
                        "\n" +
                        "END GENERATED TEST ========="),
                StringUtils.prepareForCompare(testString));
    }



    @Test
    void generateTestForMethodThatReturnsArray() {
        // Arrange
        int[] result = {3, 4};
        RecordedMethodRunInfo recordedMethodRunInfo = RecordedMethodRunInfo.builder()
                .target(new SampleService())
                .methodName("returnIntArray")
                .arguments(Collections.emptyList())
                .result(result)
                .dependencyMethodRuns(new ArrayList<>())
                .build();
        TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(recordedMethodRunInfo);

        // Act
        String testString = testGeneratorService.generateTestCode(testGenerator);

        // Assert
        assertEquals(StringUtils.prepareForCompare("BEGIN GENERATED TEST =========\n" +
                        "\n" +
                        "package com.onushi.sample.services;\n" +
                        "\n" +
                        "import org.junit.jupiter.api.Test;\n" +
                        "import static org.junit.jupiter.api.Assertions.*;\n" +
                        "\n" +
                        "class SampleServiceTest {\n" +
                        testGeneratorService.COMMENT_BEFORE_TEST +
                        "    @Test\n" +
                        "    void returnIntArray() throws Exception {\n" +
                        "        // Arrange\n" +
                        "        SampleService sampleService = new SampleService();\n" +
                        "\n" +
                        "        // Act\n" +
                        "        int[] result = sampleService.returnIntArray();\n" +
                        "\n" +
                        "        // Assert\n" +
                        "        assertEquals(2, result.length);\n" +
                        "        assertEquals(3, result[0]);\n" +
                        "        assertEquals(4, result[1]);\n" +
                        "    }\n" +
                        "}\n" +
                        "\n" +
                        "END GENERATED TEST ========="),
                StringUtils.prepareForCompare(testString));
    }


    @Test
    void generateTestForMethodThatReturnsPerson() throws Exception {
        // Arrange
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date dateOfBirth = simpleDateFormat.parse("2021-01-01");

        Person person = Person.builder()
                .firstName("Tom")
                .lastName("Richardson")
                .dateOfBirth(dateOfBirth)
                .build();
        RecordedMethodRunInfo recordedMethodRunInfo = RecordedMethodRunInfo.builder()
                .target(new SampleService())
                .methodName("returnPerson")
                .arguments(Collections.emptyList())
                .result(person)
                .dependencyMethodRuns(new ArrayList<>())
                .build();
        TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(recordedMethodRunInfo);

        // Act
        String testString = testGeneratorService.generateTestCode(testGenerator);

        // Assert
        assertEquals(StringUtils.prepareForCompare("BEGIN GENERATED TEST =========\n" +
                        "\n" +
                        "package com.onushi.sample.services;\n" +
                        "\n" +
                        "import org.junit.jupiter.api.Test;\n" +
                        "import static org.junit.jupiter.api.Assertions.*;\n" +
                        "import com.onushi.sample.model.Person;\n" +
                        "import java.text.SimpleDateFormat;\n" +
                        "\n" +
                        "class SampleServiceTest {\n" +
                        testGeneratorService.COMMENT_BEFORE_TEST +
                        "    @Test\n" +
                        "    void returnPerson() throws Exception {\n" +
                        "        // Arrange\n" +
                        "        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss.SSS\");\n" +
                        "        SampleService sampleService = new SampleService();\n" +
                        "\n" +
                        "        // Act\n" +
                        "        Person result = sampleService.returnPerson();\n" +
                        "\n" +
                        "        // Assert\n" +
                        "        assertEquals(simpleDateFormat.parse(\"2021-01-01 00:00:00.000\"), result.getDateOfBirth());\n" +
                        "        assertEquals(\"Tom\", result.getFirstName());\n" +
                        "        assertEquals(\"Richardson\", result.getLastName());\n" +
                        "    }\n" +
                        "}\n" +
                        "\n" +
                        "END GENERATED TEST ========="),
                StringUtils.prepareForCompare(testString));
    }


    @Test
    void generateTestWithRepeatedArgs() {
        // Arrange
        int[] intArray = {3, 4, 3};
        List<Float> floatList = Arrays.asList(3.0f, 3.0f);
        RecordedMethodRunInfo recordedMethodRunInfo = RecordedMethodRunInfo.builder()
                .target(new SampleService())
                .methodName("repeatedArgs")
                .arguments(Arrays.asList(intArray, floatList))
                .result(42)
                .dependencyMethodRuns(new ArrayList<>())
                .build();
        TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(recordedMethodRunInfo);

        // Act
        String testString = testGeneratorService.generateTestCode(testGenerator);

        // Assert
        assertEquals(StringUtils.prepareForCompare("BEGIN GENERATED TEST =========\n" +
                        "\n" +
                        "package com.onushi.sample.services;\n" +
                        "\n" +
                        "import org.junit.jupiter.api.Test;\n" +
                        "import static org.junit.jupiter.api.Assertions.*;\n" +
                        "import java.util.List;\n" +
                        "import java.util.Arrays;\n" +
                        "\n" +
                        "class SampleServiceTest {\n" +
                        testGeneratorService.COMMENT_BEFORE_TEST +
                        "    @Test\n" +
                        "    void repeatedArgs() throws Exception {\n" +
                        "        // Arrange\n" +
                        "        int[] array1 = {3, 4, 3};\n" +
                        "        List<Float> arrayList1 = Arrays.asList(3.0f, 3.0f);\n" +
                        "        SampleService sampleService = new SampleService();\n" +
                        "\n" +
                        "        // Act\n" +
                        "        Integer result = sampleService.repeatedArgs(array1, arrayList1);\n" +
                        "\n" +
                        "        // Assert\n" +
                        "        assertEquals(42, result);\n" +
                        "    }\n" +
                        "}\n" +
                        "\n" +
                        "END GENERATED TEST =========\n"),
                StringUtils.prepareForCompare(testString));
    }

    @Test
    void generateTestForNoArgsConstructor() {
        // Arrange
        StudentWithDefaultInitFields student1 = new StudentWithDefaultInitFields();
        StudentWithBuilder student2 = StudentWithBuilder.builder()
                .firstName("John")
                .lastName("Wayne")
                .age(60)
                .build();
        RecordedMethodRunInfo recordedMethodRunInfo = RecordedMethodRunInfo.builder()
                .target(new SampleService())
                .methodName("processStudents")
                .arguments(Arrays.asList(student1, student2))
                .result(null)
                .fallBackResultType(void.class)
                .exception(null)
                .dependencyMethodRuns(new ArrayList<>())
                .build();
        TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(recordedMethodRunInfo);

        // Act
        String testString = testGeneratorService.generateTestCode(testGenerator);

        // Assert
        assertEquals(StringUtils.prepareForCompare("BEGIN GENERATED TEST =========\n" +
                        "\n" +
                        "package com.onushi.sample.services;\n" +
                        "\n" +
                        "import org.junit.jupiter.api.Test;\n" +
                        "import static org.junit.jupiter.api.Assertions.*;\n" +
                        "import com.onushi.sample.model.StudentWithDefaultInitFields;\n" +
                        "import com.onushi.sample.model.StudentWithBuilder;\n" +
                        "\n" +
                        "class SampleServiceTest {\n" +
                        testGeneratorService.COMMENT_BEFORE_TEST +
                        "    @Test\n" +
                        "    void processStudents() throws Exception {\n" +
                        "        // Arrange\n" +
                        "        StudentWithDefaultInitFields studentWithDefaultInitFields1 = new StudentWithDefaultInitFields();\n" +
                        "\n" +
                        "        StudentWithBuilder studentWithBuilder1 = StudentWithBuilder.builder()\n" +
                        "            .age(60)\n" +
                        "            .firstName(\"John\")\n" +
                        "            .lastName(\"Wayne\")\n" +
                        "            .build();\n" +
                        "\n" +
                        "        SampleService sampleService = new SampleService();\n" +
                        "\n" +
                        "        // Act\n" +
                        "        sampleService.processStudents(studentWithDefaultInitFields1, studentWithBuilder1);\n" +
                        "\n" +
                        "    }\n" +
                        "}\n" +
                        "\n" +
                        "END GENERATED TEST ========="),
                StringUtils.prepareForCompare(testString));
    }

    @Test
    void generateTestTargetWithDependencies() throws Exception {
        // Arrange
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date date1 = simpleDateFormat.parse("1940-11-27 00:00:00.000");
        RecordedMethodRunInfo recordedMethodRunInfo = RecordedMethodRunInfo.builder()
                .target(new PersonService(new PersonRepositoryImpl()))
                .methodName("loadPerson")
                .arguments(Collections.singletonList(2))
                .result(Person.builder()
                        .dateOfBirth(date1)
                        .firstName("Bruce")
                        .lastName("Lee")
                        .build())
                .dependencyMethodRuns(new ArrayList<>())
                .build();
        TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(recordedMethodRunInfo);

        // Act
        String testString = testGeneratorService.generateTestCode(testGenerator);

        // Assert
        assertEquals(StringUtils.prepareForCompare("BEGIN GENERATED TEST =========\n" +
                        "\n" +
                        "package com.onushi.sample.services;\n" +
                        "\n" +
                        "import org.junit.jupiter.api.Test;\n" +
                        "import static org.junit.jupiter.api.Assertions.*;\n" +
                        "import com.onushi.sample.model.Person;\n" +
                        "import java.text.SimpleDateFormat;\n" +
                        "\n" +
                        "class PersonServiceTest {\n" +
                        testGeneratorService.COMMENT_BEFORE_TEST +
                        "    @Test\n" +
                        "    void loadPerson() throws Exception {\n" +
                        "        // Arrange\n" +
                        "        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss.SSS\");\n" +
                        "        PersonRepositoryImpl personRepositoryImpl1 = new PersonRepositoryImpl();\n" +
                        "        PersonService personService = new PersonService(personRepositoryImpl1);\n" +
                        "\n" +
                        "        // Act\n" +
                        "        Person result = personService.loadPerson(2);\n" +
                        "\n" +
                        "        // Assert\n" +
                        "        assertEquals(simpleDateFormat.parse(\"1940-11-27 00:00:00.000\"), result.getDateOfBirth());\n" +
                        "        assertEquals(\"Bruce\", result.getFirstName());\n" +
                        "        assertEquals(\"Lee\", result.getLastName());\n" +
                        "    }\n" +
                        "}\n" +
                        "\n" +
                        "END GENERATED TEST ========="),
                StringUtils.prepareForCompare(testString));
    }

    @Test
    void generateTestWithMock() throws Exception {
        // Arrange
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date date1 = simpleDateFormat.parse("1940-11-27 00:00:00.000");
        PersonRepositoryImpl personRepositoryImpl = new PersonRepositoryImpl();
        DependencyMethodRunInfo dependencyMethodRunInfo = DependencyMethodRunInfo.builder()
                .target(personRepositoryImpl)
                .methodName("getPersonFromDB")
                .arguments(Collections.singletonList(2))
                .result(Person.builder()
                        .dateOfBirth(date1)
                        .firstName("Bruce")
                        .lastName("Lee")
                        .build())
                .build();

        RecordedMethodRunInfo recordedMethodRunInfo = RecordedMethodRunInfo.builder()
                .target(new PersonService(personRepositoryImpl))
                .methodName("getPersonFirstName")
                .arguments(Collections.singletonList(2))
                .dependencyMethodRuns(Collections.singletonList(dependencyMethodRunInfo))
                .result("Bruce")
                .build();
        TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(recordedMethodRunInfo);

        // Act
        String testString = testGeneratorService.generateTestCode(testGenerator);

        // Assert
        assertEquals(StringUtils.prepareForCompare("BEGIN GENERATED TEST =========\n" +
                        "\n" +
                        "package com.onushi.sample.services;\n" +
                        "\n" +
                        "import org.junit.jupiter.api.Test;\n" +
                        "import static org.junit.jupiter.api.Assertions.*;\n" +
                        "import static org.mockito.Mockito.*;\n" +
                        "import com.onushi.sample.model.Person;\n" +
                        "import java.util.Date;\n" +
                        "import java.text.SimpleDateFormat;\n" +
                        "\n" +
                        "class PersonServiceTest {\n" +
                        testGeneratorService.COMMENT_BEFORE_TEST +
                        "    @Test\n" +
                        "    void getPersonFirstName() throws Exception {\n" +
                        "        // Arrange\n" +
                        "        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss.SSS\");\n" +
                        "        Date date1 = simpleDateFormat.parse(\"1940-11-27 00:00:00.000\");\n" +
                        "        Person person1 = Person.builder()\n" +
                        "            .dateOfBirth(date1)\n" +
                        "            .firstName(\"Bruce\")\n" +
                        "            .lastName(\"Lee\")\n" +
                        "            .build();\n" +
                        "\n" +
                        "        PersonRepositoryImpl personRepositoryImpl1 = mock(PersonRepositoryImpl.class);\n" +
                        "        when(personRepositoryImpl1.getPersonFromDB(2)).thenReturn(person1);\n" +
                        "\n" +
                        "        PersonService personService = new PersonService(personRepositoryImpl1);\n" +
                        "\n" +
                        "        // Act\n" +
                        "        String result = personService.getPersonFirstName(2);\n" +
                        "\n" +
                        "        // Assert\n" +
                        "        assertEquals(\"Bruce\", result);\n" +
                        "    }\n" +
                        "}\n" +
                        "\n" +
                        "END GENERATED TEST ========="),
                StringUtils.prepareForCompare(testString));
    }

    @Test
    void generateTestWith2MockCalls() throws Exception {
        // Arrange
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        PersonRepositoryImpl personRepositoryImpl = new PersonRepositoryImpl();
        DependencyMethodRunInfo dependencyMethodRunInfo1 = DependencyMethodRunInfo.builder()
                .target(personRepositoryImpl)
                .methodName("getPersonsCountFromDB")
                .arguments(Arrays.asList("a", null))
                .result(2)
                .build();
        Date date1 = simpleDateFormat.parse("1940-11-27 00:00:00.000");
        DependencyMethodRunInfo dependencyMethodRunInfo2 = DependencyMethodRunInfo.builder()
                .target(personRepositoryImpl)
                .methodName("getPersonFromDB")
                .arguments(Collections.singletonList(2))
                .result(Person.builder()
                        .dateOfBirth(date1)
                        .firstName("Bruce")
                        .lastName("Lee")
                        .build())
                .build();
        RecordedMethodRunInfo recordedMethodRunInfo = RecordedMethodRunInfo.builder()
                .target(new PersonService(personRepositoryImpl))
                .methodName("getPersonFirstName")
                .arguments(Collections.singletonList(2))
                .dependencyMethodRuns(Arrays.asList(dependencyMethodRunInfo1, dependencyMethodRunInfo2))
                .result("Bruce")
                .build();
        TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(recordedMethodRunInfo);

        // Act
        String testString = testGeneratorService.generateTestCode(testGenerator);

        // Assert
        assertEquals(StringUtils.prepareForCompare("BEGIN GENERATED TEST =========\n" +
                        "\n" +
                        "package com.onushi.sample.services;\n" +
                        "\n" +
                        "import org.junit.jupiter.api.Test;\n" +
                        "import static org.junit.jupiter.api.Assertions.*;\n" +
                        "import static org.mockito.Mockito.*;\n" +
                        "import com.onushi.sample.model.Person;\n" +
                        "import java.util.Date;\n" +
                        "import java.text.SimpleDateFormat;\n" +
                        "\n" +
                        "class PersonServiceTest {\n" +
                        testGeneratorService.COMMENT_BEFORE_TEST +
                        "    @Test\n" +
                        "    void getPersonFirstName() throws Exception {\n" +
                        "        // Arrange\n" +
                        "        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss.SSS\");\n" +
                        "        Date date1 = simpleDateFormat.parse(\"1940-11-27 00:00:00.000\");\n" +
                        "        Person person1 = Person.builder()\n" +
                        "            .dateOfBirth(date1)\n" +
                        "            .firstName(\"Bruce\")\n" +
                        "            .lastName(\"Lee\")\n" +
                        "            .build();\n" +
                        "\n" +
                        "        PersonRepositoryImpl personRepositoryImpl1 = mock(PersonRepositoryImpl.class);\n" +
                        "        when(personRepositoryImpl1.getPersonsCountFromDB(\"a\", null)).thenReturn(2);\n" +
                        "        when(personRepositoryImpl1.getPersonFromDB(2)).thenReturn(person1);\n" +
                        "\n" +
                        "        PersonService personService = new PersonService(personRepositoryImpl1);\n" +
                        "\n" +
                        "        // Act\n" +
                        "        String result = personService.getPersonFirstName(2);\n" +
                        "\n" +
                        "        // Assert\n" +
                        "        assertEquals(\"Bruce\", result);\n" +
                        "    }\n" +
                        "}\n" +
                        "\n" +
                        "END GENERATED TEST ========="),
                StringUtils.prepareForCompare(testString));
    }

    @Test
    void generateTestWithExceptionThrownByMockCall() {
        // Arrange
        PersonRepositoryImpl personRepositoryImpl = new PersonRepositoryImpl();
        DependencyMethodRunInfo dependencyMethodRunInfo1 = DependencyMethodRunInfo.builder()
                .target(personRepositoryImpl)
                .methodName("getPersonsCountFromDB")
                .arguments(Arrays.asList("a", null))
                .result(2)
                .build();
        DependencyMethodRunInfo dependencyMethodRunInfo2 = DependencyMethodRunInfo.builder()
                .target(personRepositoryImpl)
                .methodName("getPersonFromDB")
                .arguments(Collections.singletonList(3))
                .result(null)
                .exception(new NoSuchElementException())
                .build();
        RecordedMethodRunInfo recordedMethodRunInfo = RecordedMethodRunInfo.builder()
                .target(new PersonService(personRepositoryImpl))
                .methodName("getPersonFirstName")
                .arguments(Collections.singletonList(3))
                .dependencyMethodRuns(Arrays.asList(dependencyMethodRunInfo1, dependencyMethodRunInfo2))
                .result(null)
                .build();
        TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(recordedMethodRunInfo);

        // Act
        String testString = testGeneratorService.generateTestCode(testGenerator);

        // Assert
        assertEquals(StringUtils.prepareForCompare("BEGIN GENERATED TEST =========\n" +
                        "\n" +
                        "package com.onushi.sample.services;\n" +
                        "\n" +
                        "import org.junit.jupiter.api.Test;\n" +
                        "import static org.junit.jupiter.api.Assertions.*;\n" +
                        "import static org.mockito.Mockito.*;\n" +
                        "\n" +
                        "class PersonServiceTest {\n" +
                        testGeneratorService.COMMENT_BEFORE_TEST +
                        "    @Test\n" +
                        "    void getPersonFirstName() throws Exception {\n" +
                        "        // Arrange\n" +
                        "        PersonRepositoryImpl personRepositoryImpl1 = mock(PersonRepositoryImpl.class);\n" +
                        "        when(personRepositoryImpl1.getPersonsCountFromDB(\"a\", null)).thenReturn(2);\n" +
                        "        doThrow(NoSuchElementException.class)\n" +
                        "            .when(personRepositoryImpl1).getPersonFromDB(3);\n" +
                        "        PersonService personService = new PersonService(personRepositoryImpl1);\n" +
                        "\n" +
                        "        // Act\n" +
                        "        Object result = personService.getPersonFirstName(3);\n" +
                        "\n" +
                        "        // Assert\n" +
                        "        assertNull(result);\n" +
                        "    }\n" +
                        "}\n" +
                        "\n" +
                        "END GENERATED TEST ========="),
                StringUtils.prepareForCompare(testString));
    }

    @Test
    void generateTestForHashMaps() {
        // Arrange
        Map<String, List<String>> map = new HashMap<>();
        map.put("1", Arrays.asList("0", "1"));
        map.put("2", Arrays.asList("0", "1", "2"));
        map.put("3", Arrays.asList("0", "1", "2", "3"));
        RecordedMethodRunInfo recordedMethodRunInfo = RecordedMethodRunInfo.builder()
                .target(new SampleService())
                .methodName("processMap")
                .arguments(Collections.singletonList(map))
                .result(42)
                .dependencyMethodRuns(new ArrayList<>())
                .build();
        TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(recordedMethodRunInfo);

        // Act
        String testString = testGeneratorService.generateTestCode(testGenerator);

        // Assert
        assertEquals(StringUtils.prepareForCompare("BEGIN GENERATED TEST =========\n" +
                        "\n" +
                        "package com.onushi.sample.services;\n" +
                        "\n" +
                        "import org.junit.jupiter.api.Test;\n" +
                        "import static org.junit.jupiter.api.Assertions.*;\n" +
                        "import java.util.Map;\n" +
                        "import java.util.List;\n" +
                        "import java.util.HashMap;\n" +
                        "import java.util.Arrays;\n" +
                        "\n" +
                        "class SampleServiceTest {\n" +
                        testGeneratorService.COMMENT_BEFORE_TEST +
                        "    @Test\n" +
                        "    void processMap() throws Exception {\n" +
                        "        // Arrange\n" +
                        "        List<String> arrayList1 = Arrays.asList(\"0\", \"1\");\n" +
                        "\n" +
                        "        List<String> arrayList2 = Arrays.asList(\"0\", \"1\", \"2\");\n" +
                        "\n" +
                        "        List<String> arrayList3 = Arrays.asList(\"0\", \"1\", \"2\", \"3\");\n" +
                        "\n" +
                        "        Map<String, List<String>> hashMap1 = new HashMap<>();\n" +
                        "        hashMap1.put(\"1\", arrayList1);\n" +
                        "        hashMap1.put(\"2\", arrayList2);\n" +
                        "        hashMap1.put(\"3\", arrayList3);\n" +
                        "\n" +
                        "        SampleService sampleService = new SampleService();\n" +
                        "\n" +
                        "        // Act\n" +
                        "        Integer result = sampleService.processMap(hashMap1);\n" +
                        "\n" +
                        "        // Assert\n" +
                        "        assertEquals(42, result);\n" +
                        "    }\n" +
                        "}\n" +
                        "\n" +
                        "END GENERATED TEST ========="),
                StringUtils.prepareForCompare(testString));
    }

    @Test
    void generateTestForHashSets() {
        // Arrange
        Set<Double> set = new HashSet<>();
        set.add(null);
        set.add(1.2);
        set.add(2.6);
        RecordedMethodRunInfo recordedMethodRunInfo = RecordedMethodRunInfo.builder()
                .target(new SampleService())
                .methodName("processSet")
                .arguments(Collections.singletonList(set))
                .result(42.42f)
                .dependencyMethodRuns(new ArrayList<>())
                .build();
        TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(recordedMethodRunInfo);

        // Act
        String testString = testGeneratorService.generateTestCode(testGenerator);

        // Assert
        assertEquals(StringUtils.prepareForCompare("BEGIN GENERATED TEST =========\n" +
                        "\n" +
                        "package com.onushi.sample.services;\n" +
                        "\n" +
                        "import org.junit.jupiter.api.Test;\n" +
                        "import static org.junit.jupiter.api.Assertions.*;\n" +
                        "import java.util.Set;\n" +
                        "import java.util.HashSet;\n" +
                        "\n" +
                        "class SampleServiceTest {\n" +
                        testGeneratorService.COMMENT_BEFORE_TEST +
                        "    @Test\n" +
                        "    void processSet() throws Exception {\n" +
                        "        // Arrange\n" +
                        "        Set<Double> hashSet1 = new HashSet<>();\n" +
                        "        hashSet1.add(null);\n" +
                        "        hashSet1.add(1.2);\n" +
                        "        hashSet1.add(2.6);\n" +
                        "        SampleService sampleService = new SampleService();\n" +
                        "\n" +
                        "        // Act\n" +
                        "        Float result = sampleService.processSet(hashSet1);\n" +
                        "\n" +
                        "        // Assert\n" +
                        "        assertEquals(42.42f, result);\n" +
                        "    }\n" +
                        "}\n" +
                        "\n" +
                        "END GENERATED TEST =========\n"),
                StringUtils.prepareForCompare(testString));
    }

    @Test
    void generateAssertTestForListOfListOfPerson() throws Exception {
        // Arrange
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

        RecordedMethodRunInfo recordedMethodRunInfo = RecordedMethodRunInfo.builder()
                .target(new SampleService())
                .methodName("createListOfPersonList")
                .arguments(Collections.emptyList())
                .result(Arrays.asList(personList, null))
                .dependencyMethodRuns(new ArrayList<>())
                .build();
        TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(recordedMethodRunInfo);

        // Act
        String testString = testGeneratorService.generateTestCode(testGenerator);

        // Assert
        assertEquals(StringUtils.prepareForCompare("BEGIN GENERATED TEST =========\n" +
                        "\n" +
                        "package com.onushi.sample.services;\n" +
                        "\n" +
                        "import org.junit.jupiter.api.Test;\n" +
                        "import static org.junit.jupiter.api.Assertions.*;\n" +
                        "import java.util.List;\n" +
                        "import com.onushi.sample.model.Person;\n" +
                        "import java.text.SimpleDateFormat;\n" +
                        "\n" +
                        "class SampleServiceTest {\n" +
                        testGeneratorService.COMMENT_BEFORE_TEST +
                        "    @Test\n" +
                        "    void createListOfPersonList() throws Exception {\n" +
                        "        // Arrange\n" +
                        "        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss.SSS\");\n" +
                        "        SampleService sampleService = new SampleService();\n" +
                        "\n" +
                        "        // Act\n" +
                        "        List<List<Person>> result = sampleService.createListOfPersonList();\n" +
                        "\n" +
                        "        // Assert\n" +
                        "        assertEquals(2, result.size());\n" +
                        "\n" +
                        "        assertEquals(2, result.get(0).size());\n" +
                        "\n" +
                        "        assertEquals(simpleDateFormat.parse(\"1980-01-02 00:00:00.000\"), result.get(0).get(0).getDateOfBirth());\n" +
                        "        assertEquals(\"Paul\", result.get(0).get(0).getFirstName());\n" +
                        "        assertEquals(\"Thompson\", result.get(0).get(0).getLastName());\n" +
                        "\n" +
                        "        assertEquals(simpleDateFormat.parse(\"1970-02-03 00:00:00.000\"), result.get(0).get(1).getDateOfBirth());\n" +
                        "        assertEquals(\"Tom\", result.get(0).get(1).getFirstName());\n" +
                        "        assertEquals(\"Richardson\", result.get(0).get(1).getLastName());\n" +
                        "\n" +
                        "        assertNull(result.get(1));\n" +
                        "    }\n" +
                        "}\n" +
                        "\n" +
                        "END GENERATED TEST ========="),
                StringUtils.prepareForCompare(testString));
    }

    @Test
    void generateAssertTestForObjectsWithGetters() {
        // Arrange
        Employee employee = Employee.builder()
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

        RecordedMethodRunInfo recordedMethodRunInfo = RecordedMethodRunInfo.builder()
                .target(new SampleService())
                .methodName("createEmployee")
                .arguments(Collections.emptyList())
                .result(employee)
                .dependencyMethodRuns(new ArrayList<>())
                .build();
        TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(recordedMethodRunInfo);

        // Act
        String testString = testGeneratorService.generateTestCode(testGenerator);

        // Assert
        assertEquals(StringUtils.prepareForCompare("BEGIN GENERATED TEST =========\n" +
                        "\n" +
                        "package com.onushi.sample.services;\n" +
                        "\n" +
                        "import org.junit.jupiter.api.Test;\n" +
                        "import static org.junit.jupiter.api.Assertions.*;\n" +
                        "import com.onushi.sample.model.Employee;\n" +
                        "import com.onushi.sample.model.Color;\n" +
                        "\n" +
                        "class SampleServiceTest {\n" +
                        testGeneratorService.COMMENT_BEFORE_TEST +
                        "    @Test\n" +
                        "    void createEmployee() throws Exception {\n" +
                        "        // Arrange\n" +
                        "        SampleService sampleService = new SampleService();\n" +
                        "\n" +
                        "        // Act\n" +
                        "        Employee result = sampleService.createEmployee();\n" +
                        "\n" +
                        "        // Assert\n" +
                        "        assertEquals(100, result.getDepartment().getId());\n" +
                        "        assertEquals(\"IT\", result.getDepartment().getName());\n" +
                        "\n" +
                        "        assertEquals(\"John\", result.getFirstName());\n" +
                        "\n" +
                        "        assertEquals(1, result.getId());\n" +
                        "\n" +
                        "        assertEquals(\"Doe\", result.getLastName());\n" +
                        "\n" +
                        "        assertEquals(1000.0, result.getSalaryParam1());\n" +
                        "\n" +
                        "        assertEquals(1500.0, result.getSalaryParam2());\n" +
                        "\n" +
                        "        assertEquals(0.0, result.getSalaryParam3());\n" +
                        "\n" +
                        "        assertEquals(Color.BLUE, result.getTeamColor());\n" +
                        "\n" +
                        "        assertEquals(Color.BLUE, result.teamColor);\n" +
                        "    }\n" +
                        "}\n" +
                        "\n" +
                        "END GENERATED TEST ========="),
                StringUtils.prepareForCompare(testString));
    }

    @Test
    void testCyclicDependenciesInArgs() throws Exception {
        // Arrange
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        CyclicParent cyclicParent = new CyclicParent();
        CyclicChild cyclicChild = new CyclicChild();
        cyclicChild.parent = cyclicParent;
        cyclicChild.date = simpleDateFormat.parse("1980-01-02");
        cyclicParent.id = 1;
        cyclicParent.childList = Collections.singletonList(cyclicChild);

        RecordedMethodRunInfo recordedMethodRunInfo = RecordedMethodRunInfo.builder()
                .target(new SampleService())
                .methodName("processCyclicObjects")
                .arguments(Collections.singletonList(cyclicParent))
                .result(42)
                .dependencyMethodRuns(new ArrayList<>())
                .build();
        TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(recordedMethodRunInfo);

        // Act
        String testString = testGeneratorService.generateTestCode(testGenerator);

        // Assert
        assertEquals(StringUtils.prepareForCompare("BEGIN GENERATED TEST =========\n" +
                        "\n" +
                        "package com.onushi.sample.services;\n" +
                        "\n" +
                        "import org.junit.jupiter.api.Test;\n" +
                        "import static org.junit.jupiter.api.Assertions.*;\n" +
                        "import com.onushi.sample.model.CyclicParent;\n" +
                        "import java.util.List;\n" +
                        "import com.onushi.sample.model.CyclicChild;\n" +
                        "import java.util.Arrays;\n" +
                        "import java.util.Date;\n" +
                        "import java.text.SimpleDateFormat;\n" +
                        "\n" +
                        "class SampleServiceTest {\n" +
                        testGeneratorService.COMMENT_BEFORE_TEST +
                        "    @Test\n" +
                        "    void processCyclicObjects() throws Exception {\n" +
                        "        // Arrange\n" +
                        "        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss.SSS\");\n" +
                        "        Date date1 = simpleDateFormat.parse(\"1980-01-02 00:00:00.000\");\n" +
                        "        // TODO Solve initialisation for cyclic dependency in com.onushi.sample.model.CyclicParent\n" +
                        "        CyclicChild cyclicChild1 = new CyclicChild();\n" +
                        "        cyclicChild1.date = date1;\n" +
                        "        cyclicChild1.parent = ...;\n" +
                        "        List<CyclicChild> singletonList1 = Arrays.asList(cyclicChild1);\n" +
                        "\n" +
                        "        CyclicParent cyclicParent1 = new CyclicParent();\n" +
                        "        cyclicParent1.childList = singletonList1;\n" +
                        "        cyclicParent1.id = 1;\n" +
                        "\n" +
                        "        SampleService sampleService = new SampleService();\n" +
                        "\n" +
                        "        // Act\n" +
                        "        Integer result = sampleService.processCyclicObjects(cyclicParent1);\n" +
                        "\n" +
                        "        // Assert\n" +
                        "        assertEquals(42, result);\n" +
                        "    }\n" +
                        "}\n" +
                        "\n" +
                        "END GENERATED TEST ========="),
                StringUtils.prepareForCompare(testString));
    }

    @Test
    void testCyclicDependenciesInResult() throws Exception {
        // Arrange
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        CyclicParent cyclicParent = new CyclicParent();
        CyclicChild cyclicChild = new CyclicChild();
        cyclicChild.parent = cyclicParent;
        cyclicChild.date = simpleDateFormat.parse("1980-01-02");
        cyclicParent.id = 1;
        cyclicParent.childList = Collections.singletonList(cyclicChild);

        RecordedMethodRunInfo recordedMethodRunInfo = RecordedMethodRunInfo.builder()
                .target(new SampleService())
                .methodName("createCyclicObjects")
                .arguments(Collections.emptyList())
                .result(cyclicParent)
                .dependencyMethodRuns(new ArrayList<>())
                .build();
        TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(recordedMethodRunInfo);

        // Act
        String testString = testGeneratorService.generateTestCode(testGenerator);

        // Assert
        assertEquals(StringUtils.prepareForCompare("BEGIN GENERATED TEST =========\n" +
                        "\n" +
                        "package com.onushi.sample.services;\n" +
                        "\n" +
                        "import org.junit.jupiter.api.Test;\n" +
                        "import static org.junit.jupiter.api.Assertions.*;\n" +
                        "import com.onushi.sample.model.CyclicParent;\n" +
                        "import java.text.SimpleDateFormat;\n" +
                        "\n" +
                        "class SampleServiceTest {\n" +
                        testGeneratorService.COMMENT_BEFORE_TEST +
                        "    @Test\n" +
                        "    void createCyclicObjects() throws Exception {\n" +
                        "        // Arrange\n" +
                        "        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss.SSS\");\n" +
                        "        SampleService sampleService = new SampleService();\n" +
                        "\n" +
                        "        // Act\n" +
                        "        CyclicParent result = sampleService.createCyclicObjects();\n" +
                        "\n" +
                        "        // Assert\n" +
                        "        assertEquals(1, result.childList.size());\n" +
                        "        assertEquals(simpleDateFormat.parse(\"1980-01-02 00:00:00.000\"), result.childList.get(0).date);\n" +
                        "        assertEquals(1, result.id);\n" +
                        "    }\n" +
                        "}\n" +
                        "\n" +
                        "END GENERATED TEST ========="),
                StringUtils.prepareForCompare(testString));
    }

    @Test
    void testHashSetAssert() throws Exception {
        // Arrange
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


        RecordedMethodRunInfo recordedMethodRunInfo = RecordedMethodRunInfo.builder()
                .target(new SampleService())
                .methodName("createPersonHashSet")
                .arguments(Collections.emptyList())
                .result(personSet)
                .dependencyMethodRuns(new ArrayList<>())
                .build();
        TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(recordedMethodRunInfo);

        // Act
        String testString = testGeneratorService.generateTestCode(testGenerator);

        // Assert
        assertEquals(StringUtils.prepareForCompare("BEGIN GENERATED TEST =========\n" +
                        "\n" +
                        "package com.onushi.sample.services;\n" +
                        "\n" +
                        "import org.junit.jupiter.api.Test;\n" +
                        "import static org.junit.jupiter.api.Assertions.*;\n" +
                        "import java.util.Set;\n" +
                        "import com.onushi.sample.model.Person;\n" +
                        "import java.util.Date;\n" +
                        "import java.text.SimpleDateFormat;\n" +
                        "\n" +
                        "class SampleServiceTest {\n" +
                        testGeneratorService.COMMENT_BEFORE_TEST +
                        "    @Test\n" +
                        "    void createPersonHashSet() throws Exception {\n" +
                        "        // Arrange\n" +
                        "        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss.SSS\");\n" +
                        "        SampleService sampleService = new SampleService();\n" +
                        "\n" +
                        "        // Act\n" +
                        "        Set<Person> result = sampleService.createPersonHashSet();\n" +
                        "\n" +
                        "        // Assert\n" +
                        "        assertEquals(2, result.size());\n" +
                        "\n" +
                        "        Date date1 = simpleDateFormat.parse(\"1980-01-02 00:00:00.000\");\n" +
                        "        Person person1 = Person.builder()\n" +
                        "            .dateOfBirth(date1)\n" +
                        "            .firstName(\"Marco\")\n" +
                        "            .lastName(\"Polo\")\n" +
                        "            .build();\n" +
                        "\n" +
                        "        assertTrue(result.contains(person1));\n" +
                        "\n" +
                        "        Date date2 = simpleDateFormat.parse(\"1970-02-03 00:00:00.000\");\n" +
                        "        Person person2 = Person.builder()\n" +
                        "            .dateOfBirth(date2)\n" +
                        "            .firstName(\"Tom\")\n" +
                        "            .lastName(\"Richardson\")\n" +
                        "            .build();\n" +
                        "\n" +
                        "        assertTrue(result.contains(person2));\n" +
                        "    }\n" +
                        "}\n" +
                        "\n" +
                        "END GENERATED TEST ========="),
                StringUtils.prepareForCompare(testString));
    }

    @Test
    void testHashMapAssert() throws Exception {
        // Arrange
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date dateOfBirth1 = simpleDateFormat.parse("1970-01-02");
        Date dateOfBirth2 = simpleDateFormat.parse("1970-01-02");
        Date dateOfBirth3 = simpleDateFormat.parse("1920-02-03");
        Date dateOfBirth4 = simpleDateFormat.parse("1920-02-04");
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

        RecordedMethodRunInfo recordedMethodRunInfo = RecordedMethodRunInfo.builder()
                .target(new SampleService())
                .methodName("createPersonHashMap")
                .arguments(Collections.emptyList())
                .result(personMap)
                .dependencyMethodRuns(new ArrayList<>())
                .build();
        TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(recordedMethodRunInfo);

        // Act
        String testString = testGeneratorService.generateTestCode(testGenerator);

        // Assert
        assertEquals(StringUtils.prepareForCompare("BEGIN GENERATED TEST =========\n" +
                        "\n" +
                        "package com.onushi.sample.services;\n" +
                        "\n" +
                        "import org.junit.jupiter.api.Test;\n" +
                        "import static org.junit.jupiter.api.Assertions.*;\n" +
                        "import java.util.Map;\n" +
                        "import com.onushi.sample.model.Person;\n" +
                        "import java.util.Date;\n" +
                        "import java.text.SimpleDateFormat;\n" +
                        "\n" +
                        "class SampleServiceTest {\n" +
                        testGeneratorService.COMMENT_BEFORE_TEST +
                        "    @Test\n" +
                        "    void createPersonHashMap() throws Exception {\n" +
                        "        // Arrange\n" +
                        "        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss.SSS\");\n" +
                        "        SampleService sampleService = new SampleService();\n" +
                        "\n" +
                        "        // Act\n" +
                        "        Map<Person, Person> result = sampleService.createPersonHashMap();\n" +
                        "\n" +
                        "        // Assert\n" +
                        "        Date date1 = simpleDateFormat.parse(\"1970-01-02 00:00:00.000\");\n" +
                        "        Person person1 = Person.builder()\n" +
                        "            .dateOfBirth(date1)\n" +
                        "            .firstName(\"Marco\")\n" +
                        "            .lastName(\"Polo\")\n" +
                        "            .build();\n" +
                        "\n" +
                        "        Person person2 = Person.builder()\n" +
                        "            .dateOfBirth(date1)\n" +
                        "            .firstName(\"Tom\")\n" +
                        "            .lastName(\"Richardson\")\n" +
                        "            .build();\n" +
                        "\n" +
                        "        assertEquals(2, result.size());\n" +
                        "\n" +
                        "        assertEquals(simpleDateFormat.parse(\"1920-02-03 00:00:00.000\"), result.get(person1).getDateOfBirth());\n" +
                        "        assertEquals(\"Marco'\", result.get(person1).getFirstName());\n" +
                        "        assertEquals(\"Father\", result.get(person1).getLastName());\n" +
                        "\n" +
                        "        assertEquals(simpleDateFormat.parse(\"1920-02-04 00:00:00.000\"), result.get(person2).getDateOfBirth());\n" +
                        "        assertEquals(\"Tom's\", result.get(person2).getFirstName());\n" +
                        "        assertEquals(\"Father\", result.get(person2).getLastName());\n" +
                        "    }\n" +
                        "}\n" +
                        "\n" +
                        "END GENERATED TEST ========="),
                StringUtils.prepareForCompare(testString));
    }
}
