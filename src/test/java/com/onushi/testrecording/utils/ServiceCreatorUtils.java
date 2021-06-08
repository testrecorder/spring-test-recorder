package com.onushi.testrecording.utils;

import com.onushi.testrecording.analyzer.classInfo.ClassInfoService;
import com.onushi.testrecording.analyzer.object.ObjectCreationAnalyzerService;
import com.onushi.testrecording.analyzer.object.ObjectStateReaderService;
import com.onushi.testrecording.codegenerator.object.CglibService;
import com.onushi.testrecording.codegenerator.object.ObjectInfoFactoryManager;
import com.onushi.testrecording.codegenerator.template.StringService;
import com.onushi.testrecording.codegenerator.test.*;


public class ServiceCreatorUtils {
    public static ObjectNameGenerator createObjectNameGenerator() {
        return new ObjectNameGenerator(new StringService());
    }

    public static TestGeneratorService createTestGeneratorService() {
        return new TestGeneratorService(new TestImportsGeneratorService(),
                new TestArrangeGeneratorService(
                        new TestHelperObjectsGeneratorService(),
                        new StringService(),
                        new TestObjectsInitGeneratorService(new StringService())),
                new TestActGeneratorService(),
                new TestAssertGeneratorService(
                        new StringService(),
                        new ClassInfoService(),
                        new TestObjectsInitGeneratorService(new StringService())));
    }

    public static ObjectCreationAnalyzerService createObjectCreationAnalyzerService() {
        return new ObjectCreationAnalyzerService(
                new StringService(),
                new ClassInfoService());
    }

    public static ObjectInfoFactoryManager createObjectInfoFactoryManager() {
        return new ObjectInfoFactoryManager(
                new ClassInfoService(),
                new ObjectStateReaderService(),
                createObjectNameGenerator(),
                createObjectCreationAnalyzerService(),
                new CglibService(),
                new StringService());
    }

    public static TestGeneratorFactory createTestGeneratorFactory() {
        return new TestGeneratorFactory(
                createObjectNameGenerator(),
                createObjectInfoFactoryManager());
    }
}
