package com.onushi.testrecording.codegenerator.object;

import com.onushi.testrecording.analizer.classInfo.ClassInfoService;
import com.onushi.testrecording.sampleclasses.PersonWithBuilder;
import com.onushi.testrecording.utils.StringUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ObjectCodeGeneratorWithBuilderTest {
    @Test
    void testCodeGeneratorWithBuilder() {
        PersonWithBuilder personWithBuilder1 = PersonWithBuilder.builder()
                .firstName("John")
                .lastName("Michael")
                .age(35)
                .build();

        ObjectCodeGeneratorFactory objectCodeGeneratorFactory = new ObjectCodeGeneratorFactory(new ClassInfoService());
        ObjectCodeGenerator objectCodeGenerator = objectCodeGeneratorFactory.createObjectCodeGenerator(personWithBuilder1, "personWithBuilder1");
        assertEquals(StringUtils.trimAndIgnoreCRDiffs(objectCodeGenerator.getInitCode()),
                StringUtils.trimAndIgnoreCRDiffs(
                        "PersonWithBuilder personWithBuilder1 = PersonWithBuilder.builder()\n" +
                        "    .firstName(\"John\")\n" +
                        "    .lastName(\"Michael\")\n" +
                        "    .age(35)\n" +
                        "    .build();"));

    }
}
