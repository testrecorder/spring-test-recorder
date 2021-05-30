package com.onushi.testrecording.codegenerator.test;

import com.onushi.sampleapp.model.Person;
import com.onushi.sampleapp.model.Student;
import com.onushi.sampleapp.model.StudentWithBuilder;
import com.onushi.sampleapp.model.StudentWithDefaultInitFields;
import com.onushi.sampleapp.services.PersonRepositoryImpl;
import com.onushi.sampleapp.services.PersonService;
import com.onushi.sampleapp.services.SampleService;
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
    void generateTestForAddFloats() throws Exception {
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
                        "package com.onushi.sampleapp.services;\n" +
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
                        "package com.onushi.sampleapp.services;\n" +
                        "\n" +
                        "import org.junit.jupiter.api.Test;\n" +
                        "import static org.junit.jupiter.api.Assertions.*;\n" +
                        "import java.text.SimpleDateFormat;\n" +
                        "import java.util.Date;\n" +
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
                        "        Date expectedResult = simpleDateFormat.parse(\"2021-01-01 00:00:00.000\");\n" +
                        "        assertEquals(expectedResult, result);\n" +
                        "    }\n" +
                        "}\n" +
                        "\n" +
                        "END GENERATED TEST ========="),
                StringUtils.prepareForCompare(testString));
    }

    @Test
    void generateTestForReturnNull() throws Exception {
        // Arrange
        RecordedMethodRunInfo recordedMethodRunInfo = RecordedMethodRunInfo.builder()
                .target(new SampleService())
                .methodName("returnNull")
                .arguments(Collections.emptyList())
                .result(null)
                .fallBackResultType(Student.class)
                .dependencyMethodRuns(new ArrayList<>())
                .build();
        TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(recordedMethodRunInfo);

        // Act
        String testString = testGeneratorService.generateTestCode(testGenerator);

        // Assert
        assertEquals(StringUtils.prepareForCompare("BEGIN GENERATED TEST =========\n" +
                        "\n" +
                        "package com.onushi.sampleapp.services;\n" +
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
                        "        Student result = sampleService.returnNull();\n" +
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
    void generateTestForWhenHavingObjectAsArgument() throws Exception {
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
                        "package com.onushi.sampleapp.services;\n" +
                        "\n" +
                        "import org.junit.jupiter.api.Test;\n" +
                        "import static org.junit.jupiter.api.Assertions.*;\n" +
                        "import com.onushi.sampleapp.model.Person;\n" +
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
    void generateTestWhenExceptionIsThrown() throws Exception {
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
                        "package com.onushi.sampleapp.services;\n" +
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
    void generateTestWhenResultIsVoid() throws Exception {
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
                        "package com.onushi.sampleapp.services;\n" +
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
                        "package com.onushi.sampleapp.services;\n" +
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
    void generateTestForArrayLists() throws Exception {
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
                        "package com.onushi.sampleapp.services;\n" +
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
    void testObjectCaching() throws Exception {
        // Arrange
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date d1 = simpleDateFormat.parse("2021-01-01");
        RecordedMethodRunInfo recordedMethodRunInfo = RecordedMethodRunInfo.builder()
                .target(new SampleService())
                .methodName("minDate")
                .arguments(Arrays.asList(d1, d1))
                .result(d1)
                .dependencyMethodRuns(new ArrayList<>())
                .build();
        TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(recordedMethodRunInfo);

        // Act
        String testString = testGeneratorService.generateTestCode(testGenerator);

        // Assert
        assertEquals(StringUtils.prepareForCompare("BEGIN GENERATED TEST =========\n" +
                        "\n" +
                        "package com.onushi.sampleapp.services;\n" +
                        "\n" +
                        "import org.junit.jupiter.api.Test;\n" +
                        "import static org.junit.jupiter.api.Assertions.*;\n" +
                        "import java.text.SimpleDateFormat;\n" +
                        "import java.util.Date;\n" +
                        "\n" +
                        "class SampleServiceTest {\n" +
                        testGeneratorService.COMMENT_BEFORE_TEST +
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
                        "package com.onushi.sampleapp.services;\n" +
                        "\n" +
                        "import org.junit.jupiter.api.Test;\n" +
                        "import static org.junit.jupiter.api.Assertions.*;\n" +
                        "import java.text.SimpleDateFormat;\n" +
                        "import java.util.Date;\n" +
                        "import com.onushi.sampleapp.model.Person;\n" +
                        "import java.util.List;\n" +
                        "import java.util.Arrays;\n" +
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
                        "            .lastName(\"Marculescu\")\n" +
                        "            .build();\n" +
                        "        Date date2 = simpleDateFormat.parse(\"1970-02-03 00:00:00.000\");\n" +
                        "        Person person2 = Person.builder()\n" +
                        "            .dateOfBirth(date2)\n" +
                        "            .firstName(\"Gica\")\n" +
                        "            .lastName(\"Fulgerica\")\n" +
                        "            .build();\n" +
                        "        List<Person> arrayList1 = Arrays.asList(person1, person2);\n" +
                        "        Person[] array1 = {person1, person2};\n" +
                        "        SampleService sampleService = new SampleService();\n" +
                        "\n" +
                        "        // Act\n" +
                        "        List<Person> result = sampleService.someFunction(arrayList1, array1);\n" +
                        "\n" +
                        "        // Assert\n" +
                        "        List<Person> expectedResult = Arrays.asList(person1, person2);\n" +
                        "        assertEquals(expectedResult, result);\n" +
                        "    }\n" +
                        "}\n" +
                        "\n" +
                        "END GENERATED TEST ========="),
                StringUtils.prepareForCompare(testString));
    }

    @Test
    void generateTestForMethodThatReturnsArray() throws Exception {
        // Arrange
        int[] expectedResult = {3, 4};
        RecordedMethodRunInfo recordedMethodRunInfo = RecordedMethodRunInfo.builder()
                .target(new SampleService())
                .methodName("returnIntArray")
                .arguments(Collections.emptyList())
                .result(expectedResult)
                .dependencyMethodRuns(new ArrayList<>())
                .build();
        TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(recordedMethodRunInfo);

        // Act
        String testString = testGeneratorService.generateTestCode(testGenerator);

        // Assert
        assertEquals(StringUtils.prepareForCompare("BEGIN GENERATED TEST =========\n" +
                        "\n" +
                        "package com.onushi.sampleapp.services;\n" +
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
                        "        int[] expectedResult = {3, 4};\n" +
                        "        assertEquals(expectedResult, result);\n" +
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
                .firstName("Gica")
                .lastName("Fulgerica")
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
                        "package com.onushi.sampleapp.services;\n" +
                        "\n" +
                        "import org.junit.jupiter.api.Test;\n" +
                        "import static org.junit.jupiter.api.Assertions.*;\n" +
                        "import java.text.SimpleDateFormat;\n" +
                        "import java.util.Date;\n" +
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
                        "        Date date1 = simpleDateFormat.parse(\"2021-01-01 00:00:00.000\");\n" +
                        "        Person expectedResult = Person.builder()\n" +
                        "            .dateOfBirth(date1)\n" +
                        "            .firstName(\"Gica\")\n" +
                        "            .lastName(\"Fulgerica\")\n" +
                        "            .build();\n" +
                        "        assertEquals(expectedResult, result);\n" +
                        "    }\n" +
                        "}\n" +
                        "\n" +
                        "END GENERATED TEST ========="),
                StringUtils.prepareForCompare(testString));
    }


    @Test
    void generateTestWithRepeatedArgs() throws Exception {
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
                        "package com.onushi.sampleapp.services;\n" +
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
    void generateTestForNoArgsConstructor() throws Exception {
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
                        "package com.onushi.sampleapp.services;\n" +
                        "\n" +
                        "import org.junit.jupiter.api.Test;\n" +
                        "import static org.junit.jupiter.api.Assertions.*;\n" +
                        "import com.onushi.sampleapp.model.StudentWithDefaultInitFields;\n" +
                        "import com.onushi.sampleapp.model.StudentWithBuilder;\n" +
                        "\n" +
                        "class SampleServiceTest {\n" +
                        testGeneratorService.COMMENT_BEFORE_TEST +
                        "    @Test\n" +
                        "    void processStudents() throws Exception {\n" +
                        "        // Arrange\n" +
                        "        StudentWithDefaultInitFields studentWithDefaultInitFields1 = new StudentWithDefaultInitFields();\n" +
                        "        StudentWithBuilder studentWithBuilder1 = StudentWithBuilder.builder()\n" +
                        "            .age(60)\n" +
                        "            .firstName(\"John\")\n" +
                        "            .lastName(\"Wayne\")\n" +
                        "            .build();\n" +
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
                        "package com.onushi.sampleapp.services;\n" +
                        "\n" +
                        "import org.junit.jupiter.api.Test;\n" +
                        "import static org.junit.jupiter.api.Assertions.*;\n" +
                        "import com.onushi.sampleapp.services.PersonRepositoryImpl;\n" +
                        "import java.text.SimpleDateFormat;\n" +
                        "import java.util.Date;\n" +
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
                        "        Date date1 = simpleDateFormat.parse(\"1940-11-27 00:00:00.000\");\n" +
                        "        Person expectedResult = Person.builder()\n" +
                        "            .dateOfBirth(date1)\n" +
                        "            .firstName(\"Bruce\")\n" +
                        "            .lastName(\"Lee\")\n" +
                        "            .build();\n" +
                        "        assertEquals(expectedResult, result);\n" +
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
                        "package com.onushi.sampleapp.services;\n" +
                        "\n" +
                        "import org.junit.jupiter.api.Test;\n" +
                        "import static org.junit.jupiter.api.Assertions.*;\n" +
                        "import java.text.SimpleDateFormat;\n" +
                        "import java.util.Date;\n" +
                        "import com.onushi.sampleapp.model.Person;\n" +
                        "import static org.mockito.Mockito.*;\n" +
                        "import com.onushi.sampleapp.services.PersonRepositoryImpl;\n" +
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
                        "        PersonRepositoryImpl personRepositoryImpl1 = mock(PersonRepositoryImpl.class);\n" +
                        "        when(personRepositoryImpl1.getPersonFromDB(2)).thenReturn(person1);\n" +
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
                        "package com.onushi.sampleapp.services;\n" +
                        "\n" +
                        "import org.junit.jupiter.api.Test;\n" +
                        "import static org.junit.jupiter.api.Assertions.*;\n" +
                        "import java.text.SimpleDateFormat;\n" +
                        "import java.util.Date;\n" +
                        "import com.onushi.sampleapp.model.Person;\n" +
                        "import static org.mockito.Mockito.*;\n" +
                        "import com.onushi.sampleapp.services.PersonRepositoryImpl;\n" +
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
                        "        PersonRepositoryImpl personRepositoryImpl1 = mock(PersonRepositoryImpl.class);\n" +
                        "        when(personRepositoryImpl1.getPersonsCountFromDB(\"a\", null)).thenReturn(2);\n" +
                        "        when(personRepositoryImpl1.getPersonFromDB(2)).thenReturn(person1);\n" +
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
    void generateTestWithExceptionThrownByMockCall() throws Exception {
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
                        "package com.onushi.sampleapp.services;\n" +
                        "\n" +
                        "import org.junit.jupiter.api.Test;\n" +
                        "import static org.junit.jupiter.api.Assertions.*;\n" +
                        "import static org.mockito.Mockito.*;\n" +
                        "import com.onushi.sampleapp.services.PersonRepositoryImpl;\n" +
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
    void generateTestForHashMaps() throws Exception {
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
                        "package com.onushi.sampleapp.services;\n" +
                        "\n" +
                        "import org.junit.jupiter.api.Test;\n" +
                        "import static org.junit.jupiter.api.Assertions.*;\n" +
                        "import java.util.List;\n" +
                        "import java.util.Arrays;\n" +
                        "import java.util.Map;\n" +
                        "import java.util.HashMap;\n" +
                        "\n" +
                        "class SampleServiceTest {\n" +
                        testGeneratorService.COMMENT_BEFORE_TEST +
                        "    @Test\n" +
                        "    void processMap() throws Exception {\n" +
                        "        // Arrange\n" +
                        "        List<String> arrayList1 = Arrays.asList(\"0\", \"1\");\n" +
                        "        List<String> arrayList2 = Arrays.asList(\"0\", \"1\", \"2\");\n" +
                        "        List<String> arrayList3 = Arrays.asList(\"0\", \"1\", \"2\", \"3\");\n" +
                        "        Map<String, List<String>> hashMap1 = new HashMap<>();\n" +
                        "        hashMap1.put(\"1\", arrayList1);\n" +
                        "        hashMap1.put(\"2\", arrayList2);\n" +
                        "        hashMap1.put(\"3\", arrayList3);\n" +
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
    void generateTestForHashSets() throws Exception {
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
                        "package com.onushi.sampleapp.services;\n" +
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

    // TODO IB solve equality when there is no equals defined
//    @Test
//    void returnIntArray() throws Exception {
//        // Arrange
//        SampleService sampleService = new SampleService();
//
//        // Act
//        int[] result = sampleService.returnIntArray();
//
//        // Assert
//        int[] expectedResult = {3, 4};
//        assertEquals(expectedResult, result);
//    }

}
