package com.onushi.springtestrecorder.codegenerator.test;

import com.onushi.springtestrecorder.analyzer.classInfo.ClassInfoService;
import com.onushi.springtestrecorder.codegenerator.codetree.CodeBlock;
import com.onushi.springtestrecorder.codegenerator.codetree.CodeNode;
import com.onushi.springtestrecorder.codegenerator.codetree.CodeStatement;
import com.onushi.springtestrecorder.codegenerator.object.ObjectInfo;
import com.onushi.springtestrecorder.codegenerator.object.PropertyValue;
import com.onushi.springtestrecorder.codegenerator.object.VisibleProperty;
import com.onushi.springtestrecorder.codegenerator.template.StringGenerator;
import com.onushi.springtestrecorder.codegenerator.template.StringService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TestAssertGeneratorService {
    private final ClassInfoService classInfoService;
    private final TestObjectsInitGeneratorService testObjectsInitGeneratorService;
    private final StringService stringService;

    public TestAssertGeneratorService(ClassInfoService classInfoService,
                                      TestObjectsInitGeneratorService testObjectsInitGeneratorService,
                                      StringService stringService) {
        this.classInfoService = classInfoService;
        this.testObjectsInitGeneratorService = testObjectsInitGeneratorService;
        this.stringService = stringService;
    }

    public String getAssertCode(TestGenerator testGenerator) {
        if (testGenerator.getExpectedException() != null) {
            return "";
        } else if (testGenerator.getResultDeclareClassName().equals("void")) {
            return "";
        } else {
            return "        // Assert\n" +
                getAssertCode(testGenerator.getExpectedResultObjectInfo(), "result").getCode();
        }
    }

    private CodeNode getAssertCode(ObjectInfo objectInfo, String assertPath) {
        CodeBlock result = new CodeBlock();
        if (objectInfo.getObject() != null &&
                classInfoService.hasEquals(objectInfo.getObject().getClass()) &&
                objectInfo.hasInitCode() &&
                objectInfo.isInitAdded()) {
            result.addChild(getAssertEqualsForObject(objectInfo, assertPath));
        } else {
            for (Map.Entry<String, VisibleProperty> entry : objectInfo.getVisibleProperties().entrySet()) {
                VisibleProperty visibleProperty = entry.getValue();
                PropertyValue finalValue = visibleProperty.getFinalValue();
                String composedPath = assertPath + entry.getKey();

                CodeNode objectsInit = null;
                if (visibleProperty.getFinalDependencies() != null) {
                    objectsInit = new CodeStatement(testObjectsInitGeneratorService.getObjectsInit(visibleProperty.getFinalDependencies()).getCode());
                }

                if (finalValue.getString() != null && (finalValue.getString().equals("null") ||
                                                        finalValue.getString().equals("true") ||
                                                        finalValue.getString().equals("false"))) {
                    result.addChild(getSpecificAssert(composedPath, objectsInit, finalValue.getString()));
                } else {
                    if (finalValue.getObjectInfo() != null) {
                        if (objectsInit != null) {
                            result.addPrerequisite(objectsInit);
                        }
                        result.addChild(getAssertCode(finalValue.getObjectInfo(), composedPath));
                    } else if (finalValue.getString() != null) {
                        result.addChild(getAssertEqualsForString(visibleProperty, composedPath, objectsInit));
                    }
                }
            }
        }
        return result;
    }

    private CodeNode getAssertEqualsForObject(ObjectInfo objectInfo, String assertPath) {
        String statement = new StringGenerator()
                .setTemplate("        assertEquals({{objectInfoName}}, {{assertPath}});\n")
                .addAttribute("objectInfoName", objectInfo.getObjectName())
                .addAttribute("assertPath", assertPath)
                .generate();
        return new CodeStatement(statement);
    }

    private CodeNode getSpecificAssert(String composedPath, CodeNode objectsInit, String value) {
        CodeBlock result = new CodeBlock();
        if (objectsInit != null) {
            result.addPrerequisite(objectsInit);
        }
        String statement = new StringGenerator()
                .setTemplate("        {{specificAssert}}({{composedPath}});\n")
                .addAttribute("composedPath", composedPath)
                .addAttribute("specificAssert", "assert" + stringService.upperCaseFirstLetter(value))
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
