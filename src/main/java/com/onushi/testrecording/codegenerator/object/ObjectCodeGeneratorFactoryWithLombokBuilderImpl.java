package com.onushi.testrecording.codegenerator.object;

import com.onushi.testrecording.analyzer.classInfo.ClassInfoService;
import com.onushi.testrecording.analyzer.object.FieldValue;
import com.onushi.testrecording.analyzer.object.FieldValueStatus;
import com.onushi.testrecording.analyzer.object.ObjectCreationAnalyzerService;
import com.onushi.testrecording.codegenerator.template.StringGenerator;
import com.onushi.testrecording.codegenerator.test.TestGenerator;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ObjectCodeGeneratorFactoryWithLombokBuilderImpl implements ObjectCodeGeneratorFactory {
    private final ClassInfoService classInfoService;
    private final ObjectCodeGeneratorFactoryManager objectCodeGeneratorFactoryManager;
    private final ObjectCreationAnalyzerService objectCreationAnalyzerService;
    public ObjectCodeGeneratorFactoryWithLombokBuilderImpl(ObjectCodeGeneratorFactoryManager objectCodeGeneratorFactoryManager,
                                                           ClassInfoService classInfoService,
                                                           ObjectCreationAnalyzerService objectCreationAnalyzerService) {
        this.classInfoService = classInfoService;
        this.objectCodeGeneratorFactoryManager = objectCodeGeneratorFactoryManager;
        this.objectCreationAnalyzerService = objectCreationAnalyzerService;
    }

    @Override
    public ObjectCodeGenerator createObjectCodeGenerator(ObjectCodeGeneratorCreationContext context) {
        if (objectCreationAnalyzerService.canBeCreatedWithLombokBuilder(context.getObject())) {

            ObjectCodeGenerator objectCodeGenerator = new ObjectCodeGenerator(context.getObject(),
                    context.getObjectName(),
                    context.getObjectName(),
                    context.getObject().getClass().getSimpleName());

            objectCodeGenerator.requiredImports.add(context.getObject().getClass().getName());

            Map<String, FieldValue> objectState = context.getObjectState();
            objectCodeGenerator.dependencies = objectState.values().stream()
                    .distinct()
                    .filter(fieldValue -> fieldValue.getFieldValueStatus() != FieldValueStatus.COULD_NOT_READ)
                    .map(FieldValue::getValue)
                    .map(fieldValue -> objectCodeGeneratorFactoryManager.getCommonObjectCodeGenerator(context.getTestGenerator(), fieldValue))
                    .collect(Collectors.toList());
            objectCodeGenerator.initCode = getInitCode(context.getTestGenerator(), context.getObject(), context.getObjectName(), objectState);
            return objectCodeGenerator;
        } else {
            return null;
        }
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
                            objectCodeGeneratorFactoryManager.getCommonObjectCodeGenerator(testGenerator, objectState.get(fieldName).getValue());
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
