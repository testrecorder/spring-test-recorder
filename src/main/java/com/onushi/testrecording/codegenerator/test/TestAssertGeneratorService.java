package com.onushi.testrecording.codegenerator.test;

import com.onushi.testrecording.analyzer.classInfo.ClassInfoService;
import com.onushi.testrecording.codegenerator.codetree.CodeBlock;
import com.onushi.testrecording.codegenerator.codetree.CodeNode;
import com.onushi.testrecording.codegenerator.codetree.CodeStatement;
import com.onushi.testrecording.codegenerator.object.ObjectInfo;
import com.onushi.testrecording.codegenerator.object.PropertyValue;
import com.onushi.testrecording.codegenerator.object.VisibleProperty;
import com.onushi.testrecording.codegenerator.template.StringGenerator;
import com.onushi.testrecording.codegenerator.template.StringService;
import org.aspectj.apache.bcel.classfile.Code;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TestAssertGeneratorService {
    private final StringService stringService;
    private final ClassInfoService classInfoService;
    private final TestObjectsInitGeneratorService testObjectsInitGeneratorService;

    public TestAssertGeneratorService(StringService stringService,
                                      ClassInfoService classInfoService,
                                      TestObjectsInitGeneratorService testObjectsInitGeneratorService) {
        this.stringService = stringService;
        this.classInfoService = classInfoService;
        this.testObjectsInitGeneratorService = testObjectsInitGeneratorService;
    }

    public String getAssertCode(TestGenerator testGenerator) {
        if (testGenerator.getExpectedException() != null) {
            return "";
        } else if (testGenerator.getResultDeclareClassName().equals("void")) {
            return "";
        } else {
            return "        // Assert\n" +
                getAssertCode(testGenerator.getExpectedResultObjectInfo(), "result");
        }
    }

    private String getAssertCode(ObjectInfo objectInfo, String assertPath) {
        StringBuilder stringBuilder = new StringBuilder();
        if (objectInfo.getObject() != null &&
                classInfoService.hasEquals(objectInfo.getObject().getClass()) &&
                !objectInfo.isInlineOnly() &&
                objectInfo.isInitAdded()) {
            stringBuilder.append(getAssertEqualsForObject(objectInfo, assertPath).getCode());
        } else {
            for (Map.Entry<String, VisibleProperty> entry : objectInfo.getVisibleProperties().entrySet()) {
                VisibleProperty visibleProperty = entry.getValue();
                PropertyValue finalValue = visibleProperty.getFinalValue();
                String composedPath = assertPath + entry.getKey();

                CodeNode objectsInit = null;
                if (visibleProperty.getFinalDependencies() != null) {
                    objectsInit = new CodeStatement(testObjectsInitGeneratorService.getObjectsInit(visibleProperty.getFinalDependencies()).stream()
                            .map(x -> stringService.addPrefixOnAllLines(x, "        ") + "\n").collect(Collectors.joining("")));
                }

                if (finalValue.getString() != null && finalValue.getString().equals("null")) {
                    stringBuilder.append(getAssertNull(composedPath, objectsInit).getCode());
                } else if (finalValue.getString() != null && finalValue.getString().equals("true")) {
                    stringBuilder.append(getAssertTrue(composedPath, objectsInit).getCode());
                } else {
                    if (finalValue.getObjectInfo() != null) {
                        if (objectsInit != null) {
                            stringBuilder.append(objectsInit.getCode());
                        }
                        stringBuilder.append(getAssertCode(finalValue.getObjectInfo(), composedPath));
                    } else if (finalValue.getString() != null) {
                        stringBuilder.append(getAssertEqualsForString(visibleProperty, composedPath, objectsInit).getCode());
                    }
                }
            }
        }
        // TODO IB !!!! apply CodeTree here
        return stringBuilder.toString();
    }

    private CodeNode getAssertEqualsForObject(ObjectInfo objectInfo, String assertPath) {
        String statement = new StringGenerator()
                .setTemplate("        assertEquals({{objectInfoName}}, {{assertPath}});\n")
                .addAttribute("objectInfoName", objectInfo.getObjectName())
                .addAttribute("assertPath", assertPath)
                .generate();
        return new CodeStatement(statement);
    }

    private CodeNode getAssertNull(String composedPath, CodeNode objectsInit) {
        CodeBlock result = new CodeBlock();
        if (objectsInit != null) {
            result.addPrerequisite(objectsInit);
        }
        String statement = new StringGenerator()
                .setTemplate("        assertNull({{composedPath}});\n")
                .addAttribute("composedPath", composedPath)
                .generate();
        result.addChild(new CodeStatement(statement));
        return result;
    }

    private CodeNode getAssertTrue(String composedPath, CodeNode objectsInit) {
        CodeBlock result = new CodeBlock();
        if (objectsInit != null) {
            result.addPrerequisite(objectsInit);
        }
        String statement = new StringGenerator()
                .setTemplate("        assertTrue({{composedPath}});\n")
                .addAttribute("composedPath", composedPath)
                .generate();
        result.addChild(new CodeStatement(statement));
        return result;
    }

    private CodeNode getAssertEqualsForString(VisibleProperty visibleProperty, String composedPath, CodeNode objectsInit) {
        CodeBlock result = new CodeBlock();
        if (objectsInit != null) {
            result.addPrerequisite(objectsInit);
        }
        String statement = new StringGenerator()
                .setTemplate("        assertEquals({{expected}}, {{composedPath}});\n")
                .addAttribute("expected", visibleProperty.getFinalValue().getString())
                .addAttribute("composedPath", composedPath)
                .generate();
        result.addChild(new CodeStatement(statement));
        return result;
    }
}
