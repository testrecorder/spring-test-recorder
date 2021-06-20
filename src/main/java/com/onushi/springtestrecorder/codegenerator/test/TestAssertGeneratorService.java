package com.onushi.springtestrecorder.codegenerator.test;

import com.onushi.springtestrecorder.analyzer.classInfo.ClassInfoService;
import com.onushi.springtestrecorder.codegenerator.codetree.CodeBlock;
import com.onushi.springtestrecorder.codegenerator.codetree.CodeNode;
import com.onushi.springtestrecorder.codegenerator.codetree.CodeStatement;
import com.onushi.springtestrecorder.codegenerator.object.ObjectInfo;
import com.onushi.springtestrecorder.codegenerator.object.PropertyValue;
import com.onushi.springtestrecorder.codegenerator.object.VisibleProperty;
import com.onushi.springtestrecorder.codegenerator.object.VisiblePropertySnapshot;
import com.onushi.springtestrecorder.codegenerator.template.StringGenerator;
import com.onushi.springtestrecorder.codegenerator.template.StringService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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

    public String getResultAssertCode(TestGenerator testGenerator) {
        if (testGenerator.getExpectedException() != null) {
            return "";
        } else if (testGenerator.getResultDeclareClassName().equals("void")) {
            return "";
        } else {
            return "        // Assert\n" +
                getAssertCode(testGenerator.getExpectedResultObjectInfo(), "result", false).getCode();
        }
    }

    public String getSideEffectsAssertCode(TestGenerator testGenerator) {
        List<ObjectInfo> objectsToCheck = new ArrayList<>(testGenerator.argumentObjectInfos);
        objectsToCheck.add(testGenerator.targetObjectInfo);

        CodeBlock sideEffectsCodeBlock = new CodeBlock();
        for (ObjectInfo objectInfo : objectsToCheck) {
            sideEffectsCodeBlock.addChild(getAssertCode(objectInfo, objectInfo.getInlineCode(), true));
        }

        String sideEffectsCode = sideEffectsCodeBlock.getCode();
        if (sideEffectsCode.equals("")) {
            return "";
        } else {
            return "\n" +
                    "        // Side Effects\n" +
                    sideEffectsCode;
        }
    }

    private CodeNode getAssertCode(ObjectInfo objectInfo, String assertPath, boolean onlySideEffects) {
        CodeBlock result = new CodeBlock();

        if (objectInfo.getObject() != null &&
                classInfoService.hasEquals(objectInfo.getObject().getClass()) &&
                objectInfo.hasInitCode() &&
                objectInfo.isInitAdded() &&
                !onlySideEffects) {
            result.addChild(getAssertEqualsForObject(objectInfo, assertPath));
        } else {
            for (Map.Entry<String, VisibleProperty> entry : objectInfo.getVisibleProperties().entrySet()) {
                VisibleProperty visibleProperty = entry.getValue();
                VisiblePropertySnapshot firstSnapshot = visibleProperty.getFirstSnapshot();
                VisiblePropertySnapshot lastSnapshot = visibleProperty.getLastSnapshot();
                PropertyValue finalValue = lastSnapshot.getValue();
                String composedPath = assertPath + entry.getKey();

                if (onlySideEffects) {
                    if (!isSideEffectDetected(visibleProperty, firstSnapshot, lastSnapshot)) {
                        continue;
                    }
                }

                CodeNode objectsInit = null;
                if (visibleProperty.getLastSnapshot().getOtherDependencies() != null) {
                    objectsInit = new CodeStatement(
                            testObjectsInitGeneratorService.getObjectsInit(visibleProperty.getLastSnapshot().getOtherDependencies()).getCode());
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
                        // if we detect side effects for an object, assert it's children fully
                        result.addChild(getAssertCode(finalValue.getObjectInfo(), composedPath, false));
                    } else if (finalValue.getString() != null) {
                        result.addChild(getAssertEqualsForString(visibleProperty, composedPath, objectsInit));
                    }
                }
            }
        }
        return result;
    }

    private boolean isSideEffectDetected(VisibleProperty visibleProperty, VisiblePropertySnapshot firstSnapshot, VisiblePropertySnapshot lastSnapshot) {
        if (visibleProperty.getSnapshots().values().size() > 1) {
            return !firstSnapshot.getValue().isSameValue(lastSnapshot.getValue());
        } else {
            return visibleProperty.hasAfterMethodRunSnapshot();
        }
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
                .addAttribute("expected", visibleProperty.getLastSnapshot().getValue().getString())
                .addAttribute("composedPath", composedPath)
                .generate();
        result.addChild(new CodeStatement(statement));
        return result;
    }
}
