package com.onushi.testrecording.codegenerator.object;

import com.onushi.testrecording.analyzer.classInfo.MatchingConstructor;
import com.onushi.testrecording.analyzer.object.ObjectCreationAnalyzerService;
import com.onushi.testrecording.codegenerator.template.StringGenerator;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ObjectCodeGeneratorFactoryWithAllArgsConstructor {
    private final ObjectCodeGeneratorFactory objectCodeGeneratorFactory;
    private final ObjectCreationAnalyzerService objectCreationAnalyzerService;

    public ObjectCodeGeneratorFactoryWithAllArgsConstructor(ObjectCodeGeneratorFactory objectCodeGeneratorFactory,
                                                            ObjectCreationAnalyzerService objectCreationAnalyzerService) {
        this.objectCodeGeneratorFactory = objectCodeGeneratorFactory;
        this.objectCreationAnalyzerService = objectCreationAnalyzerService;
    }


    ObjectCodeGenerator createObjectCodeGenerator(ObjectCodeGeneratorCreationContext context) {
        List<MatchingConstructor> matchingAllArgsConstructors = objectCreationAnalyzerService.getMatchingAllArgsConstructors(context.getObject());
        if (matchingAllArgsConstructors.size() > 0) {
            MatchingConstructor matchingConstructor = matchingAllArgsConstructors.get(0);
            boolean moreConstructorsAvailable = matchingAllArgsConstructors.size() > 1;


            ObjectCodeGenerator objectCodeGenerator = new ObjectCodeGenerator(context.getObject(), context.getObjectName(), context.getObjectName());

            List<ObjectCodeGenerator> args = matchingConstructor.getArgsInOrder().stream()
                    .map(argument -> objectCodeGeneratorFactory.getCommonObjectCodeGenerator(context.getTestGenerator(), argument.getValue()))
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
