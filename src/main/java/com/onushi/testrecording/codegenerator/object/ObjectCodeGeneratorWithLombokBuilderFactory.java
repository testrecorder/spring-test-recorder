package com.onushi.testrecording.codegenerator.object;

import com.onushi.testrecording.analizer.classInfo.ClassInfoService;
import com.onushi.testrecording.analizer.object.ObjectStateReaderService;
import com.onushi.testrecording.codegenerator.template.StringGenerator;
import com.onushi.testrecording.codegenerator.test.TestGenerator;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

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

        setInitCode(testGenerator, objectCodeGenerator, object, objectName);
        return objectCodeGenerator;
    }

    private void setInitCode(TestGenerator testGenerator, ObjectCodeGenerator objectCodeGenerator, Object object, String objectName) {
        StringGenerator stringGenerator = new StringGenerator();
        stringGenerator.setTemplate(
                "{{shortClassName}} {{objectName}} = {{shortClassName}}.builder()\n" +
                getSettersCodeForInit(testGenerator, object) +
                "    .build();");
        stringGenerator.addAttribute("shortClassName", object.getClass().getSimpleName());
        stringGenerator.addAttribute("objectName", objectName);

        objectCodeGenerator.initCode = stringGenerator.generate();
    }

    private String getSettersCodeForInit(TestGenerator testGenerator, Object object) {
        List<Method> lombokBuilderSetters = classInfoService.getLombokBuilderSetters(object.getClass());
        Map<String, Object> objectState = objectStateReaderService.readObjectState(object);

        StringBuilder settersCode = new StringBuilder();
        for (Method setter: lombokBuilderSetters) {
            String fieldName = setter.getName();
            StringGenerator stringGenerator = new StringGenerator();
            stringGenerator.setTemplate("    .{{fieldName}}({{fieldValue}})\n");
            stringGenerator.addAttribute("fieldName", fieldName);
            if (objectState.containsKey(fieldName)) {
                // TODO IB !!!! add in dependencies
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
