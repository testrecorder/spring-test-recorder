package com.onushi.testrecording.codegenerator.object;

import com.onushi.testrecording.analyzer.classInfo.MatchingConstructor;
import com.onushi.testrecording.analyzer.object.ObjectCreationAnalyzerService;
import com.onushi.testrecording.codegenerator.template.StringGenerator;

import java.util.List;
import java.util.stream.Collectors;

public class ObjectInfoFactoryWithAllArgsConstructorImpl extends ObjectInfoFactory {
    private final ObjectInfoFactoryManager objectInfoFactoryManager;
    private final ObjectCreationAnalyzerService objectCreationAnalyzerService;

    public ObjectInfoFactoryWithAllArgsConstructorImpl(ObjectInfoFactoryManager objectInfoFactoryManager,
                                                       ObjectCreationAnalyzerService objectCreationAnalyzerService) {
        this.objectInfoFactoryManager = objectInfoFactoryManager;
        this.objectCreationAnalyzerService = objectCreationAnalyzerService;
    }

    @Override
    public ObjectInfo createObjectCodeGenerator(ObjectInfoCreationContext context) {
        List<MatchingConstructor> matchingAllArgsConstructors =
                objectCreationAnalyzerService.getMatchingAllArgsConstructors(context.getObject(), context.getObjectState());
        if (matchingAllArgsConstructors.size() > 0) {
            MatchingConstructor matchingConstructor = matchingAllArgsConstructors.get(0);
            boolean moreConstructorsAvailable = matchingAllArgsConstructors.size() > 1;


            ObjectInfo objectInfo = new ObjectInfo(context.getObject(), context.getObjectName(), context.getObjectName(), false);

            List<ObjectInfo> args = matchingConstructor.getArgsInOrder().stream()
                    .map(argument -> objectInfoFactoryManager.getCommonObjectCodeGenerator(context.getTestGenerator(), argument.getValue()))
                    .collect(Collectors.toList());

            String argsInlineCode = args.stream()
                    .map(ObjectInfo::getInlineCode).collect(Collectors.joining(", "));

            boolean addCheckOrderOfArgs = matchingConstructor.isFieldsCouldHaveDifferentOrder() || moreConstructorsAvailable;
            objectInfo.initCode = new StringGenerator()
                    .setTemplate("{{commentLine}}{{shortClassName}} {{objectName}} = new {{shortClassName}}({{argsInlineCode}});")
                    .addAttribute("shortClassName", context.getObject().getClass().getSimpleName())
                    .addAttribute("argsInlineCode", argsInlineCode)
                    .addAttribute("objectName", context.getObjectName())
                    .addAttribute("commentLine", addCheckOrderOfArgs ? "// TODO Check order of arguments\n" : "")
                    .generate();

            objectInfo.dependencies = args.stream()
                    .distinct()
                    .collect(Collectors.toList());

            objectInfo.requiredImports.add(context.getObject().getClass().getName());
            return objectInfo;
        } else {
            return null;
        }
    }
}
