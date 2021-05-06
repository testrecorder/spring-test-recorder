package com.onushi.testrecording.codegenerator.object;

import com.onushi.testrecording.analyzer.classInfo.MatchingConstructor;
import com.onushi.testrecording.codegenerator.template.StringGenerator;
import com.onushi.testrecording.codegenerator.test.TestGenerator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ObjectCodeGeneratorWithAllArgsConstructorFactory {
    private final ObjectCodeGeneratorFactory objectCodeGeneratorFactory;

    public ObjectCodeGeneratorWithAllArgsConstructorFactory(ObjectCodeGeneratorFactory objectCodeGeneratorFactory) {
        this.objectCodeGeneratorFactory = objectCodeGeneratorFactory;
    }

    ObjectCodeGenerator createObjectCodeGenerator(Object object, String objectName, TestGenerator testGenerator,
                                                  MatchingConstructor matchingConstructor, boolean moreConstructorsAvailable) {

        ObjectCodeGenerator objectCodeGenerator = new ObjectCodeGenerator(object, objectName, objectName);

        List<ObjectCodeGenerator> args = matchingConstructor.getArgsInOrder().stream()
                .map(argument -> objectCodeGeneratorFactory.getCommonObjectCodeGenerator(testGenerator, argument.getValue()))
                .collect(Collectors.toList());

        String argsInlineCode = args.stream()
                .map(ObjectCodeGenerator::getInlineCode).collect(Collectors.joining(", "));

        objectCodeGenerator.initCode = new StringGenerator()
                .setTemplate("{{shortClassName}} {{objectName}} = new {{shortClassName}}({{argsInlineCode}});")
                .addAttribute("shortClassName", object.getClass().getSimpleName())
                .addAttribute("argsInlineCode", argsInlineCode)
                .addAttribute("objectName", objectName)
                .generate();



        objectCodeGenerator.dependencies = args.stream()
                .distinct()
                .collect(Collectors.toList());

        objectCodeGenerator.requiredImports.add(object.getClass().getName());
        return objectCodeGenerator;
    }
}
