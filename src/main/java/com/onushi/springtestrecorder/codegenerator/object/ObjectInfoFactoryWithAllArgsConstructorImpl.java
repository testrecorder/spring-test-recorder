package com.onushi.springtestrecorder.codegenerator.object;

import com.onushi.springtestrecorder.analyzer.classInfo.ClassInfoService;
import com.onushi.springtestrecorder.analyzer.classInfo.MatchingConstructor;
import com.onushi.springtestrecorder.analyzer.object.ObjectCreationAnalyzerService;
import com.onushi.springtestrecorder.codegenerator.template.StringGenerator;

import java.util.List;
import java.util.stream.Collectors;

public class ObjectInfoFactoryWithAllArgsConstructorImpl extends ObjectInfoFactory {
    private final ObjectInfoFactoryManager objectInfoFactoryManager;
    private final ObjectCreationAnalyzerService objectCreationAnalyzerService;
    private final ClassInfoService classInfoService;

    public ObjectInfoFactoryWithAllArgsConstructorImpl(ObjectInfoFactoryManager objectInfoFactoryManager,
                                                       ObjectCreationAnalyzerService objectCreationAnalyzerService,
                                                       ClassInfoService classInfoService) {
        this.objectInfoFactoryManager = objectInfoFactoryManager;
        this.objectCreationAnalyzerService = objectCreationAnalyzerService;
        this.classInfoService = classInfoService;
    }

    @Override
    public ObjectInfo createObjectInfo(ObjectInfoCreationContext context) {
        List<MatchingConstructor> matchingAllArgsConstructors = objectCreationAnalyzerService.getMatchingAllArgsConstructors(
                context.getObject(), context.getObjectState(), context.isObjectInSamePackageWithTest());
        if (matchingAllArgsConstructors.size() > 0) {
            MatchingConstructor matchingConstructor = matchingAllArgsConstructors.get(0);
            boolean moreConstructorsAvailable = matchingAllArgsConstructors.size() > 1;


            ObjectInfo objectInfo = new ObjectInfo(context.getObject(), context.getObjectName(), context.getObjectName());

            List<ObjectInfo> args = matchingConstructor.getArgsInOrder().stream()
                    .map(argument -> objectInfoFactoryManager.getCommonObjectInfo(context.getTestGenerator(), argument.getValue()))
                    .collect(Collectors.toList());

            String argsInlineCode = args.stream()
                    .map(ObjectInfo::getInlineCode).collect(Collectors.joining(", "));

            boolean addCheckOrderOfArgs = matchingConstructor.isFieldsCouldHaveDifferentOrder() || moreConstructorsAvailable;
            objectInfo.initCode = new StringGenerator()
                    .setTemplate("{{commentLine}}{{shortClassName}} {{objectName}} = new {{shortClassName}}({{argsInlineCode}});\n")
                    .addAttribute("shortClassName", context.getObject().getClass().getSimpleName())
                    .addAttribute("argsInlineCode", argsInlineCode)
                    .addAttribute("objectName", context.getObjectName())
                    .addAttribute("commentLine", addCheckOrderOfArgs ? "// TODO Check order of arguments\n" : "")
                    .generate();

            objectInfo.initDependencies = args.stream()
                    .distinct()
                    .collect(Collectors.toList());

            objectInfo.declareRequiredImports.add(context.getObject().getClass().getName());

            setVisiblePropertiesForUnknown(objectInfo, context, objectInfoFactoryManager, classInfoService);

            return objectInfo;
        } else {
            return null;
        }
    }
}
