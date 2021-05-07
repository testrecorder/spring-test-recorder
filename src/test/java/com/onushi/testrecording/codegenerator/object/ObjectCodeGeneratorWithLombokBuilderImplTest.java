package com.onushi.testrecording.codegenerator.object;

import com.onushi.testrecording.analyzer.classInfo.ClassInfoService;
import com.onushi.testrecording.analyzer.object.ObjectCreationAnalyzerService;
import com.onushi.testrecording.analyzer.object.ObjectStateReaderService;
import com.onushi.testrecording.codegenerator.test.ObjectNameGenerator;
import com.onushi.sampleapp.StudentWithBuilder;
import com.onushi.testrecording.codegenerator.test.TestGenerator;
import com.onushi.testrecording.utils.StringUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class ObjectCodeGeneratorWithLombokBuilderImplTest {
    @Test
    void testCodeGeneratorWithBuilder() {
        StudentWithBuilder studentWithBuilder1 = StudentWithBuilder.builder()
                .firstName("John")
                .lastName("Michael")
                .age(35)
                .build();

        ObjectNameGenerator objectNameGenerator = new ObjectNameGenerator();
        TestGenerator testGenerator = mock(TestGenerator.class);
        ClassInfoService classInfoService = new ClassInfoService();
        ObjectStateReaderService objectStateReaderService = new ObjectStateReaderService();
        ObjectCodeGeneratorFactoryManager objectCodeGeneratorFactoryManager = new ObjectCodeGeneratorFactoryManager(
                classInfoService,
                objectStateReaderService,
                objectNameGenerator,
                new ObjectCreationAnalyzerService(classInfoService, objectStateReaderService));
        ObjectCodeGenerator objectCodeGenerator = objectCodeGeneratorFactoryManager.createObjectCodeGenerator(testGenerator, studentWithBuilder1, "studentWithBuilder1");
        assertEquals(StringUtils.trimAndIgnoreCRDiffs(
                        "StudentWithBuilder studentWithBuilder1 = StudentWithBuilder.builder()\n" +
                        "    .age(35)\n" +
                        "    .firstName(\"John\")\n" +
                        "    .lastName(\"Michael\")\n" +
                        "    .build();"),
                StringUtils.trimAndIgnoreCRDiffs(objectCodeGenerator.getInitCode()));

    }
}