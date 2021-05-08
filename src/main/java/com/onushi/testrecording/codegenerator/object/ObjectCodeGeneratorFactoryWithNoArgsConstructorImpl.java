package com.onushi.testrecording.codegenerator.object;

import com.onushi.testrecording.analyzer.object.ObjectCreationAnalyzerService;
import com.onushi.testrecording.codegenerator.template.StringGenerator;

public class ObjectCodeGeneratorFactoryWithNoArgsConstructorImpl implements ObjectCodeGeneratorFactory {
    private final ObjectCreationAnalyzerService objectCreationAnalyzerService;

    public ObjectCodeGeneratorFactoryWithNoArgsConstructorImpl(ObjectCreationAnalyzerService objectCreationAnalyzerService) {
        this.objectCreationAnalyzerService = objectCreationAnalyzerService;
    }

    @Override
    public ObjectCodeGenerator createObjectCodeGenerator(ObjectCodeGeneratorCreationContext context) {
        if (objectCreationAnalyzerService.canBeCreatedWithNoArgsConstructor(context.getObject(), context.getObjectState())){
            ObjectCodeGenerator objectCodeGenerator = new ObjectCodeGenerator(context.getObject(), context.getObjectName(), context.getObjectName());
            objectCodeGenerator.initCode = new StringGenerator()
                    .setTemplate("{{shortClassName}} {{objectName}} = new {{shortClassName}}();")
                    .addAttribute("shortClassName", context.getObject().getClass().getSimpleName())
                    .addAttribute("objectName", context.getObjectName())
                    .generate();

            objectCodeGenerator.requiredImports.add(context.getObject().getClass().getName());
            return objectCodeGenerator;
        } else {
            return null;
        }
    }
}
