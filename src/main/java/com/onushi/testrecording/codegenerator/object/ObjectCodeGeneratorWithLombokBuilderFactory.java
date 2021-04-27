package com.onushi.testrecording.codegenerator.object;

import com.onushi.testrecording.analizer.classInfo.ClassInfoService;
import com.onushi.testrecording.analizer.object.ObjectStateReaderService;
import com.onushi.testrecording.codegenerator.template.StringGenerator;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

@Service
public class ObjectCodeGeneratorWithLombokBuilderFactory {
    private final ClassInfoService classInfoService;
    private final ObjectStateReaderService objectStateReaderService;
    private final ObjectCodeGeneratorFactory objectCodeGeneratorFactory;

    public ObjectCodeGeneratorWithLombokBuilderFactory(ClassInfoService classInfoService, ObjectStateReaderService objectStateReaderService, ObjectCodeGeneratorFactory objectCodeGeneratorFactory) {
        this.classInfoService = classInfoService;
        this.objectStateReaderService = objectStateReaderService;
        this.objectCodeGeneratorFactory = objectCodeGeneratorFactory;
    }

    public ObjectCodeGenerator createObjectCodeGenerator(Object object, String objectName) {
        ObjectCodeGenerator objectCodeGenerator = new ObjectCodeGenerator(object, objectName, false, objectName);

        objectCodeGenerator.requiredImports.add(object.getClass().getName());

        setInitCode(objectCodeGenerator, object, objectName);
        return objectCodeGenerator;
    }

    private void setInitCode(ObjectCodeGenerator objectCodeGenerator, Object object, String objectName) {
        StringGenerator stringGenerator = new StringGenerator();
        stringGenerator.setTemplate(
                "{{shortClassName}} {{objectName}} = {{shortClassName}}.builder()\n" +
                getSettersCodeForInit(object) +
                "    .build();");
        stringGenerator.addAttribute("shortClassName", object.getClass().getSimpleName());
        stringGenerator.addAttribute("objectName", objectName);

        objectCodeGenerator.initCode = stringGenerator.generate();
    }

    private String getSettersCodeForInit(Object object) {
        List<Method> lombokBuilderSetters = classInfoService.getLombokBuilderSetters(object.getClass());
        Map<String, Object> objectState = objectStateReaderService.readObjectState(object);

        StringBuilder settersCode = new StringBuilder();
        for (Method setter: lombokBuilderSetters) {
            String fieldName = setter.getName();
            StringGenerator stringGenerator = new StringGenerator();
            stringGenerator.setTemplate("    .{{fieldName}}({{fieldValue}})\n");
            stringGenerator.addAttribute("fieldName", fieldName);
            if (objectState.containsKey(fieldName)) {
                ObjectCodeGenerator objectCodeGenerator = objectCodeGeneratorFactory.createObjectCodeGenerator(objectState.get(fieldName), "ignored");
                stringGenerator.addAttribute("fieldValue", objectCodeGenerator.inlineCode);
            } else {
                stringGenerator.addAttribute("fieldValue", "???");
            }
            settersCode.append(stringGenerator.generate());
        }
        return settersCode.toString();
    }
}
