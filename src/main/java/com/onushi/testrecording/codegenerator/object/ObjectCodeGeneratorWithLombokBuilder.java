package com.onushi.testrecording.codegenerator.object;

import com.onushi.testrecording.codegenerator.template.StringGenerator;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

// TODO IB !!!! too many params
public class ObjectCodeGeneratorWithLombokBuilder extends ObjectCodeGenerator {
    protected ObjectCodeGeneratorWithLombokBuilder(Object object, String objectName,
                                                   String packageName, String shortClassName,
                                                   List<Method> lombokBuilderSetters,
                                                   Map<String, Object> objectState,
                                                   ObjectCodeGeneratorFactory objectCodeGeneratorFactory) {
        super(object, objectName, false, objectName);

        this.requiredImports.add(packageName);

        setInitCode(object, objectName, shortClassName, lombokBuilderSetters, objectState, objectCodeGeneratorFactory);
    }

    private void setInitCode(Object object, String objectName, String shortClassName,
                             List<Method> lombokBuilderSetters, Map<String, Object> objectState,
                             ObjectCodeGeneratorFactory objectCodeGeneratorFactory) {
        getSettersCodeForInit(lombokBuilderSetters, objectState, objectCodeGeneratorFactory);

        StringGenerator stringGenerator = new StringGenerator();
        stringGenerator.setTemplate(
                "{{shortClassName}} {{objectName}} = {{shortClassName}}.builder()\n" +
                getSettersCodeForInit(lombokBuilderSetters, objectState, objectCodeGeneratorFactory) +
                "    .build();");
        stringGenerator.addAttribute("shortClassName", shortClassName);
        stringGenerator.addAttribute("objectName", objectName);

        this.initCode = stringGenerator.generate();
    }

    private String getSettersCodeForInit(List<Method> lombokBuilderSetters, Map<String, Object> objectState,
                                         ObjectCodeGeneratorFactory objectCodeGeneratorFactory) {
        StringBuilder setttersCode = new StringBuilder();
        for (Method setter: lombokBuilderSetters) {
            String fieldName = setter.getName();
            StringGenerator stringGenerator = new StringGenerator();
            stringGenerator.setTemplate("    .{{fieldName}}({{fieldValue}})\n");
            stringGenerator.addAttribute("fieldName", fieldName);
            if (objectState.containsKey(fieldName)) {
                // TODO IB !!!! we should also use the initCode for all the objects
                ObjectCodeGenerator objectCodeGenerator = objectCodeGeneratorFactory.createObjectCodeGenerator(objectState.get(fieldName), "ignored");
                stringGenerator.addAttribute("fieldValue", objectCodeGenerator.inlineCode);
            } else {
                stringGenerator.addAttribute("fieldValue", "???");
            }
            setttersCode.append(stringGenerator.generate());
        }
        return setttersCode.toString();
    }
}
