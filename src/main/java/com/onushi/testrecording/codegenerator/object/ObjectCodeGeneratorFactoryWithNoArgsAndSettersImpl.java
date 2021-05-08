package com.onushi.testrecording.codegenerator.object;

import com.onushi.testrecording.analyzer.classInfo.ClassInfoService;
import com.onushi.testrecording.analyzer.object.FieldValue;
import com.onushi.testrecording.analyzer.object.ObjectCreationAnalyzerService;
import com.onushi.testrecording.analyzer.object.ObjectStateReaderService;
import com.onushi.testrecording.analyzer.object.SetterInfo;

import java.util.Map;

public class ObjectCodeGeneratorFactoryWithNoArgsAndSettersImpl implements ObjectCodeGeneratorFactory {
    private final ObjectCodeGeneratorFactoryManager objectCodeGeneratorFactoryManager;
    private final ClassInfoService classInfoService;
    private final ObjectStateReaderService objectStateReaderService;
    private final ObjectCreationAnalyzerService objectCreationAnalyzerService;

    public ObjectCodeGeneratorFactoryWithNoArgsAndSettersImpl(ObjectCodeGeneratorFactoryManager objectCodeGeneratorFactoryManager,
                                                              ClassInfoService classInfoService,
                                                              ObjectStateReaderService objectStateReaderService,
                                                              ObjectCreationAnalyzerService objectCreationAnalyzerService) {
        this.classInfoService = classInfoService;
        this.objectCodeGeneratorFactoryManager = objectCodeGeneratorFactoryManager;
        this.objectStateReaderService = objectStateReaderService;
        this.objectCreationAnalyzerService = objectCreationAnalyzerService;
    }

    @Override
    public ObjectCodeGenerator createObjectCodeGenerator(ObjectCodeGeneratorCreationContext context) {
        if (context.getObject() == null) {
            return null;
        }
        if (!classInfoService.hasPublicNoArgsConstructor(context.getObject().getClass())) {
            return null;
        }
        // TODO IB !!!! duplicate code in many places
        Map<String, FieldValue> objectState = objectStateReaderService.getObjectState(context.getObject());
        Map<String, SetterInfo> settersForFields = objectCreationAnalyzerService.getSettersForFields(context.getObject(), objectState);
        if (objectState.values().size() != settersForFields.values().size()) {
            return null;
        }

        // TODO IB !!!! implement
        ObjectCodeGenerator objectCodeGenerator = new ObjectCodeGenerator(context.getObject(), context.getObjectName(), context.getObjectName());



        objectCodeGenerator.requiredImports.add(context.getObject().getClass().getName());
        return objectCodeGenerator;
    }
}
