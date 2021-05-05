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

        List<ObjectCodeGenerator> args = matchingConstructor.getArgsInOrder().stream()
                .map(argument -> objectCodeGeneratorFactory.getCommonObjectCodeGenerator(testGenerator, argument.getValue()))
                .collect(Collectors.toList());

        String argsInlineCode = args.stream()
                .map(ObjectCodeGenerator::getInlineCode).collect(Collectors.joining(", "));

        String inlineCode = new StringGenerator()
                .setTemplate("new {{shortClassName}}({{argsInlineCode}})")
                .addAttribute("shortClassName", object.getClass().getSimpleName())
                .addAttribute("argsInlineCode", argsInlineCode)
                .generate();

        ObjectCodeGenerator objectCodeGenerator = new ObjectCodeGenerator(object, objectName, inlineCode);

        objectCodeGenerator.dependencies = args.stream()
                .distinct()
                .collect(Collectors.toList());

        objectCodeGenerator.requiredImports.add(object.getClass().getName());
        return objectCodeGenerator;
    }
}
