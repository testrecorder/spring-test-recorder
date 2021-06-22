package com.onushi.springtestrecorder.codegenerator.test;

import com.onushi.springtestrecorder.analyzer.classInfo.ClassInfoService;
import com.onushi.springtestrecorder.codegenerator.codetree.CodeBlock;
import com.onushi.springtestrecorder.codegenerator.codetree.CodeNode;
import com.onushi.springtestrecorder.codegenerator.codetree.CodeStatement;
import com.onushi.springtestrecorder.codegenerator.object.*;
import com.onushi.springtestrecorder.codegenerator.template.StringGenerator;
import com.onushi.springtestrecorder.codegenerator.template.StringService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TestAssertGeneratorService {
    private final ClassInfoService classInfoService;
    private final TestObjectsInitGeneratorService testObjectsInitGeneratorService;
    private final StringService stringService;
    private final ObjectInfoService objectInfoService;

    public TestAssertGeneratorService(ClassInfoService classInfoService,
                                      TestObjectsInitGeneratorService testObjectsInitGeneratorService,
                                      StringService stringService,
                                      ObjectInfoService objectInfoService) {
        this.classInfoService = classInfoService;
        this.testObjectsInitGeneratorService = testObjectsInitGeneratorService;
        this.stringService = stringService;
        this.objectInfoService = objectInfoService;
    }

    public String getResultAssertCode(TestGenerator testGenerator) {
        if (testGenerator.getExpectedException() != null) {
            return "";
        } else if (testGenerator.getResultDeclareClassName().equals("void")) {
            return "";
        } else {
            return "        // Assert\n" +
                getAssertCode(testGenerator.getExpectedResultObjectInfo(), "result", false, new HashSet<>()).getCode();
        }
    }

    public String getSideEffectsAssertCode(TestGenerator testGenerator) {
        List<ObjectInfo> objectsToCheck = new ArrayList<>(testGenerator.argumentObjectInfos);
        objectsToCheck.add(testGenerator.targetObjectInfo);

        CodeBlock sideEffectsCodeBlock = new CodeBlock();
        for (ObjectInfo objectInfo : objectsToCheck) {
            sideEffectsCodeBlock.addChild(getAssertCode(objectInfo, objectInfo.getInlineCode(), true, new HashSet<>()));
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

    private CodeNode getAssertCode(ObjectInfo objectInfo, String assertPath, boolean onlySideEffects, Set<ObjectInfoWithPath> objectsAlreadyAnalysed) {
        CodeBlock result = new CodeBlock();
        String assertPathEnd = getAssertPathEnd(assertPath);
        if (assertPathEnd != null) {
            ObjectInfoWithPath objectInfoWithPath = new ObjectInfoWithPath(objectInfo, assertPathEnd);
            if (objectsAlreadyAnalysed.contains(objectInfoWithPath)) {
                return result;
            }
            objectsAlreadyAnalysed.add(objectInfoWithPath);
        }

        // TODO IB !!!! avoid infinite traversal

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
                PropertyValue lastValue = lastSnapshot.getValue();
                String composedPath = assertPath + entry.getKey();

                if (onlySideEffects) {
                    // this means a property was present, but it's no longer
                    if (!visibleProperty.hasAfterMethodRunSnapshot()) {
                        continue;
                    }

                    // if there are 2 versions, check if they differ
                    if (firstSnapshot.getValue() != lastSnapshot.getValue()) {
                        if (objectInfoService.propertyValuesEquivalent(firstSnapshot.getValue(), TestRecordingMoment.FIRST_SNAPSHOT,
                                lastSnapshot.getValue(), TestRecordingMoment.LAST_SNAPSHOT)) {
                            continue;
                        }
                    }
                }

                CodeNode objectsInit = null;
                if (visibleProperty.getLastSnapshot().getOtherDependencies() != null) {
                    objectsInit = new CodeStatement(
                            testObjectsInitGeneratorService.getObjectsInit(visibleProperty.getLastSnapshot().getOtherDependencies()).getCode());
                }

                if (lastValue.getString() != null && (lastValue.getString().equals("null") ||
                        lastValue.getString().equals("true") ||
                        lastValue.getString().equals("false"))) {
                    result.addChild(getSpecificAssert(composedPath, objectsInit, lastValue.getString()));
                } else {
                    if (lastValue.getObjectInfo() != null) {
                        if (objectsInit != null) {
                            result.addPrerequisite(objectsInit);
                        }
                        result.addChild(getAssertCode(lastValue.getObjectInfo(), composedPath, false, objectsAlreadyAnalysed));
                    } else if (lastValue.getString() != null) {
                        result.addChild(getAssertEqualsForString(visibleProperty, composedPath, objectsInit));
                    }
                }
            }
        }
        return result;
    }

    private String getAssertPathEnd(String assertPath) {
        String assertPathEnd = null;
        int lastIndex = assertPath.lastIndexOf(".");
        if (lastIndex != -1) {
            String assertPath1 = assertPath.substring(0, lastIndex);
            lastIndex = assertPath1.lastIndexOf(".");
            if (lastIndex != -1) {
                assertPathEnd = assertPath.substring(lastIndex);
                lastIndex++;
            }
        }
        return assertPathEnd;
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
