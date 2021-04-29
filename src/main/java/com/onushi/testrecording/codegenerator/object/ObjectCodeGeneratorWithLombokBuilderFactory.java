package com.onushi.testrecording.codegenerator.object;

import com.onushi.testrecording.analizer.classInfo.ClassInfoService;
import com.onushi.testrecording.analizer.object.ObjectStateReaderService;
import com.onushi.testrecording.codegenerator.template.StringGenerator;
import com.onushi.testrecording.codegenerator.test.TestGenerator;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
        ObjectCodeGenerator objectCodeGenerator = new ObjectCodeGenerator(object, objectName, false, objectName);

        objectCodeGenerator.requiredImports.add(object.getClass().getName());

        Map<String, Object> objectState = objectStateReaderService.readObjectState(object);
        objectCodeGenerator.dependencies = objectState.values().stream()
                .distinct()
                .map(fieldValue -> objectCodeGeneratorFactory.getCommonObjectCodeGenerator(testGenerator, fieldValue))
                .collect(Collectors.toList());
        objectCodeGenerator.initCode = getInitCode(testGenerator, object, objectName, objectState);
        return objectCodeGenerator;
    }

    private String getInitCode(TestGenerator testGenerator, Object object, String objectName, Map<String, Object> objectState) {
        StringGenerator stringGenerator = new StringGenerator();
        stringGenerator.setTemplate(
                "{{shortClassName}} {{objectName}} = {{shortClassName}}.builder()\n" +
                getSettersCodeForInit(testGenerator, object, objectState) +
                "    .build();");
        stringGenerator.addAttribute("shortClassName", object.getClass().getSimpleName());
        stringGenerator.addAttribute("objectName", objectName);

        return stringGenerator.generate();
    }

    private String getSettersCodeForInit(TestGenerator testGenerator, Object object, Map<String, Object> objectState) {
        List<Method> lombokBuilderSetters = classInfoService.getLombokBuilderSetters(object.getClass());

        StringBuilder settersCode = new StringBuilder();
        for (Method setter: lombokBuilderSetters) {
            String fieldName = setter.getName();
            StringGenerator stringGenerator = new StringGenerator();
            stringGenerator.setTemplate("    .{{fieldName}}({{fieldValue}})\n");
            stringGenerator.addAttribute("fieldName", fieldName);
            if (objectState.containsKey(fieldName)) {
                // this will be found in the cache since dependencies were calculated
                ObjectCodeGenerator objectCodeGenerator = objectCodeGeneratorFactory.getCommonObjectCodeGenerator(testGenerator, objectState.get(fieldName));
                stringGenerator.addAttribute("fieldValue", objectCodeGenerator.inlineCode);
            } else {
                stringGenerator.addAttribute("fieldValue", "???");
            }
            settersCode.append(stringGenerator.generate());
        }
        return settersCode.toString();
    }
}
