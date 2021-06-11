package com.onushi.testrecorder.utils;

import com.onushi.testrecorder.analyzer.classInfo.ClassInfoService;
import com.onushi.testrecorder.analyzer.object.ObjectCreationAnalyzerService;
import com.onushi.testrecorder.analyzer.object.ObjectStateReaderService;
import com.onushi.testrecorder.codegenerator.object.CglibService;
import com.onushi.testrecorder.codegenerator.object.ObjectInfoFactoryManager;
import com.onushi.testrecorder.codegenerator.template.StringService;
import com.onushi.testrecorder.codegenerator.test.*;


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
