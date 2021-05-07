package com.onushi.testrecording.codegenerator.object;

import com.onushi.testrecording.analyzer.classInfo.MatchingConstructor;
import com.onushi.testrecording.analyzer.object.ObjectCreationAnalyzerService;
import com.onushi.testrecording.codegenerator.template.StringGenerator;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

public class ObjectCodeGeneratorFactoryWithAllArgsConstructorImpl implements ObjectCodeGeneratorFactory {
    private final ObjectCodeGeneratorFactoryManager objectCodeGeneratorFactoryManager;
    private final ObjectCreationAnalyzerService objectCreationAnalyzerService;

    public ObjectCodeGeneratorFactoryWithAllArgsConstructorImpl(ObjectCodeGeneratorFactoryManager objectCodeGeneratorFactoryManager,
                                                                ObjectCreationAnalyzerService objectCreationAnalyzerService) {
        this.objectCodeGeneratorFactoryManager = objectCodeGeneratorFactoryManager;
        this.objectCreationAnalyzerService = objectCreationAnalyzerService;
    }

    @Override
    public ObjectCodeGenerator createObjectCodeGenerator(ObjectCodeGeneratorCreationContext context) {
        List<MatchingConstructor> matchingAllArgsConstructors = objectCreationAnalyzerService.getMatchingAllArgsConstructors(context.getObject());
        if (matchingAllArgsConstructors.size() > 0) {
            MatchingConstructor matchingConstructor = matchingAllArgsConstructors.get(0);
            boolean moreConstructorsAvailable = matchingAllArgsConstructors.size() > 1;


            ObjectCodeGenerator objectCodeGenerator = new ObjectCodeGenerator(context.getObject(), context.getObjectName(), context.getObjectName());

            List<ObjectCodeGenerator> args = matchingConstructor.getArgsInOrder().stream()
                    .map(argument -> objectCodeGeneratorFactoryManager.getCommonObjectCodeGenerator(context.getTestGenerator(), argument.getValue()))
                    .collect(Collectors.toList());

            String argsInlineCode = args.stream()
                    .map(ObjectCodeGenerator::getInlineCode).collect(Collectors.joining(", "));

            boolean addCheckOrderOfArgs = matchingConstructor.isFieldsCouldHaveDifferentOrder() || moreConstructorsAvailable;
            objectCodeGenerator.initCode = new StringGenerator()
                    .setTemplate("{{commentLine}}{{shortClassName}} {{objectName}} = new {{shortClassName}}({{argsInlineCode}});")
                    .addAttribute("shortClassName", context.getObject().getClass().getSimpleName())
                    .addAttribute("argsInlineCode", argsInlineCode)
                    .addAttribute("objectName", context.getObjectName())
                    .addAttribute("commentLine", addCheckOrderOfArgs ? "// TODO Check order of arguments\n" : "")
                    .generate();

            objectCodeGenerator.dependencies = args.stream()
                    .distinct()
                    .collect(Collectors.toList());

            objectCodeGenerator.requiredImports.add(context.getObject().getClass().getName());
            return objectCodeGenerator;
        } else {
            return null;
        }
    }
}
