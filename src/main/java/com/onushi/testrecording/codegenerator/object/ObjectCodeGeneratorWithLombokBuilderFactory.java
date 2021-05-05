package com.onushi.testrecording.codegenerator.object;

import com.onushi.testrecording.analyzer.classInfo.ClassInfoService;
import com.onushi.testrecording.analyzer.object.FieldValue;
import com.onushi.testrecording.analyzer.object.FieldValueStatus;
import com.onushi.testrecording.analyzer.object.ObjectStateReaderService;
import com.onushi.testrecording.codegenerator.template.StringGenerator;
import com.onushi.testrecording.codegenerator.test.TestGenerator;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ObjectCodeGeneratorWithLombokBuilderFactory {
    private final ClassInfoService classInfoService;
    private final ObjectStateReaderService objectStateReaderService;
    private final ObjectCodeGeneratorFactory objectCodeGeneratorFactory;

    public ObjectCodeGeneratorWithLombokBuilderFactory(ClassInfoService classInfoService,
                                                       ObjectStateReaderService objectStateReaderService,
                                                       ObjectCodeGeneratorFactory objectCodeGeneratorFactory) {
        this.classInfoService = classInfoService;
        this.objectStateReaderService = objectStateReaderService;
        this.objectCodeGeneratorFactory = objectCodeGeneratorFactory;
    }

    public ObjectCodeGenerator createObjectCodeGenerator(TestGenerator testGenerator, Object object, String objectName) {
        ObjectCodeGenerator objectCodeGenerator = new ObjectCodeGenerator(object, objectName, objectName, object.getClass().getSimpleName());

        objectCodeGenerator.requiredImports.add(object.getClass().getName());

        Map<String, FieldValue> objectState = objectStateReaderService.getObjectState(object);
        objectCodeGenerator.dependencies = objectState.values().stream()
                .distinct()
                .filter(fieldValue -> fieldValue.getFieldValueStatus() != FieldValueStatus.COULD_NOT_READ)
                .map(FieldValue::getValue)
                .map(fieldValue -> objectCodeGeneratorFactory.getCommonObjectCodeGenerator(testGenerator, fieldValue))
                .collect(Collectors.toList());
        objectCodeGenerator.initCode = getInitCode(testGenerator, object, objectName, objectState);
        return objectCodeGenerator;
    }

    private String getInitCode(TestGenerator testGenerator, Object object, String objectName, Map<String, FieldValue> objectState) {
        return new StringGenerator()
            .setTemplate(
                    "{{shortClassName}} {{objectName}} = {{shortClassName}}.builder()\n" +
                    getSettersCodeForInit(testGenerator, object, objectState) +
                    "    .build();")
            .addAttribute("shortClassName", object.getClass().getSimpleName())
            .addAttribute("objectName", objectName)
            .generate();
    }

    private String getSettersCodeForInit(TestGenerator testGenerator, Object object, Map<String, FieldValue> objectState) {
        List<Method> lombokBuilderSetters = classInfoService.getLombokBuilderSetters(object.getClass());

        StringBuilder settersCode = new StringBuilder();
        for (Method setter: lombokBuilderSetters) {
            String fieldName = setter.getName();
            StringGenerator stringGenerator = new StringGenerator();
            stringGenerator.setTemplate("    .{{fieldName}}({{fieldValue}})\n");
            stringGenerator.addAttribute("fieldName", fieldName);
            if (objectState.containsKey(fieldName)) {
                // this will be found in the cache since dependencies were calculated
                FieldValue fieldValue = objectState.get(fieldName);
                if (fieldValue.getFieldValueStatus() == FieldValueStatus.VALUE_READ) {
                    ObjectCodeGenerator objectCodeGenerator =
                            objectCodeGeneratorFactory.getCommonObjectCodeGenerator(testGenerator, objectState.get(fieldName).getValue());
                    stringGenerator.addAttribute("fieldValue", objectCodeGenerator.inlineCode);
                } else {
                    stringGenerator.addAttribute("fieldValue", "??? could not read field");
                }
            } else {
                stringGenerator.addAttribute("fieldValue", "??? could not find field");
            }
            settersCode.append(stringGenerator.generate());
        }
        return settersCode.toString();
    }
}
