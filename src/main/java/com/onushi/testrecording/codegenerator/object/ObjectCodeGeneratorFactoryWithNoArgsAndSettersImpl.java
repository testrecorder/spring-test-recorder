package com.onushi.testrecording.codegenerator.object;

import com.onushi.testrecording.analyzer.object.ObjectCreationAnalyzerService;
import com.onushi.testrecording.analyzer.object.ObjectStateReaderService;

public class ObjectCodeGeneratorFactoryWithNoArgsAndSettersImpl implements ObjectCodeGeneratorFactory {
    private final ObjectCodeGeneratorFactoryManager objectCodeGeneratorFactoryManager;
    private final ObjectStateReaderService objectStateReaderService;
    private final ObjectCreationAnalyzerService objectCreationAnalyzerService;

    public ObjectCodeGeneratorFactoryWithNoArgsAndSettersImpl(ObjectCodeGeneratorFactoryManager objectCodeGeneratorFactoryManager,
                                                              ObjectStateReaderService objectStateReaderService,
                                                              ObjectCreationAnalyzerService objectCreationAnalyzerService) {
        this.objectCodeGeneratorFactoryManager = objectCodeGeneratorFactoryManager;
        this.objectStateReaderService = objectStateReaderService;
        this.objectCreationAnalyzerService = objectCreationAnalyzerService;
    }

    @Override
    public ObjectCodeGenerator createObjectCodeGenerator(ObjectCodeGeneratorCreationContext context) {
        // TODO IB !!!! implement
        return null;
    }
}
