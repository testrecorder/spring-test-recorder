package com.onushi.testrecording.codegenerator.object;

import com.onushi.testrecording.analizer.classInfo.ClassInfoService;
import com.onushi.testrecording.analizer.object.ObjectStateReaderService;
import com.onushi.testrecording.codegenerator.test.ObjectNameGenerator;
import com.onushi.testrecording.sampleclasses.StudentWithBuilder;
import com.onushi.testrecording.utils.StringUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ObjectCodeGeneratorWithLombokBuilderTest {
    @Test
    void testCodeGeneratorWithBuilder() {
        StudentWithBuilder studentWithBuilder1 = StudentWithBuilder.builder()
                .firstName("John")
                .lastName("Michael")
                .age(35)
                .build();

        ObjectNameGenerator objectNameGenerator = new ObjectNameGenerator();
        ObjectCodeGeneratorFactory objectCodeGeneratorFactory = new ObjectCodeGeneratorFactory(new ClassInfoService(), new ObjectStateReaderService(objectNameGenerator));
        ObjectCodeGenerator objectCodeGenerator = objectCodeGeneratorFactory.createObjectCodeGenerator(studentWithBuilder1, "studentWithBuilder1");
        assertEquals(StringUtils.trimAndIgnoreCRDiffs(
                        "StudentWithBuilder studentWithBuilder1 = StudentWithBuilder.builder()\n" +
                        "    .age(35)\n" +
                        "    .firstName(\"John\")\n" +
                        "    .lastName(\"Michael\")\n" +
                        "    .build();"),
                StringUtils.trimAndIgnoreCRDiffs(objectCodeGenerator.getInitCode()));

    }
}
