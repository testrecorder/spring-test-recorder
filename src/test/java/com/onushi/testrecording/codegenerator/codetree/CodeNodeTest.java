package com.onushi.testrecording.codegenerator.codetree;

import com.onushi.testrecording.utils.StringUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CodeNodeTest {
    @Test
    void createCodeTree() {

        CodeNode root = new CodeBlock()
                .addChild(new CodeStatement("assertEquals(2, result.size());\n"))
                .addChild(new CodeBlock()
                        .addPrerequisite(new CodeBlock()
                                .addPrerequisite(new CodeStatement("Date date1 = simpleDateFormat.parse(\"1970-01-02 00:00:00.000\");\n"))
                                .addChild(new CodeStatement(
                                        "Person person1 = Person.builder()\n" +
                                        "    .dateOfBirth(date1)\n" +
                                        "    .firstName(\"Marco\")\n" +
                                        "    .lastName(\"Polo\")\n" +
                                        "    .build();\n")))
                        .addChild(new CodeStatement("assertEquals(simpleDateFormat.parse(\"1920-02-03 00:00:00.000\"), result.get(person1).getDateOfBirth());\n"))
                        .addChild(new CodeStatement("assertEquals(\"Marco'\", result.get(person1).getFirstName());\n"))
                        .addChild(new CodeStatement("assertEquals(\"Father\", result.get(person1).getLastName());\n")))
                .addChild(new CodeBlock()
                        .addPrerequisite(new CodeBlock()
                                .addChild(new CodeStatement(
                                        "Person person2 = Person.builder()\n" +
                                        "    .dateOfBirth(date1)\n" +
                                        "    .firstName(\"Tom\")\n" +
                                        "    .lastName(\"Richardson\")\n" +
                                        "    .build();\n")))
                        .addChild(new CodeStatement("assertEquals(simpleDateFormat.parse(\"1920-02-04 00:00:00.000\"), result.get(person2).getDateOfBirth());\n"))
                        .addChild(new CodeStatement("assertEquals(\"Tom's\", result.get(person2).getFirstName());\n"))
                        .addChild(new CodeStatement("assertEquals(\"Father\", result.get(person2).getLastName());\n")));

        String code = root.getCode();

        assertEquals(18, root.getRawLinesCount());

        assertEquals(StringUtils.prepareForCompareNoTrim(
                      "assertEquals(2, result.size());\n" +
                           "\n" +
                           "Date date1 = simpleDateFormat.parse(\"1970-01-02 00:00:00.000\");\n" +
                           "Person person1 = Person.builder()\n" +
                           "    .dateOfBirth(date1)\n" +
                           "    .firstName(\"Marco\")\n" +
                           "    .lastName(\"Polo\")\n" +
                           "    .build();\n" +
                           "assertEquals(simpleDateFormat.parse(\"1920-02-03 00:00:00.000\"), result.get(person1).getDateOfBirth());\n" +
                           "assertEquals(\"Marco'\", result.get(person1).getFirstName());\n" +
                           "assertEquals(\"Father\", result.get(person1).getLastName());\n" +
                           "\n" +
                           "Person person2 = Person.builder()\n" +
                           "    .dateOfBirth(date1)\n" +
                           "    .firstName(\"Tom\")\n" +
                           "    .lastName(\"Richardson\")\n" +
                           "    .build();\n" +
                           "assertEquals(simpleDateFormat.parse(\"1920-02-04 00:00:00.000\"), result.get(person2).getDateOfBirth());\n" +
                           "assertEquals(\"Tom's\", result.get(person2).getFirstName());\n" +
                           "assertEquals(\"Father\", result.get(person2).getLastName());\n"),
                StringUtils.prepareForCompareNoTrim(code));
    }
}
