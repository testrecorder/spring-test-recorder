package com.onushi.testrecording.utils;

import com.onushi.testrecording.analyzer.classInfo.ClassInfoService;
import com.onushi.testrecording.analyzer.object.ObjectCreationAnalyzerService;
import com.onushi.testrecording.analyzer.object.ObjectReadAnalyzerService;
import com.onushi.testrecording.analyzer.object.ObjectStateReaderService;
import com.onushi.testrecording.codegenerator.object.CglibService;
import com.onushi.testrecording.codegenerator.object.ObjectCodeGeneratorFactoryManager;
import com.onushi.testrecording.codegenerator.template.StringService;
import com.onushi.testrecording.codegenerator.test.ObjectNameGenerator;
import com.onushi.testrecording.codegenerator.test.TestGeneratorFactory;
import com.onushi.testrecording.codegenerator.test.TestGeneratorService;


public class ServiceCreatorUtils {
    public static ObjectNameGenerator createObjectNameGenerator() {
        return new ObjectNameGenerator(new StringService());
    }

    public static TestGeneratorService createTestGeneratorService() {
        return new TestGeneratorService(new StringService(), new ClassInfoService());
    }

    public static ObjectCreationAnalyzerService createObjectCreationAnalyzerService() {
        return new ObjectCreationAnalyzerService(
                new StringService(),
                new ClassInfoService());
    }

    public static ObjectCodeGeneratorFactoryManager createObjectCodeGeneratorFactoryManager() {
        return new ObjectCodeGeneratorFactoryManager(
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
                createObjectCodeGeneratorFactoryManager(),
                createTestGeneratorService());
    }
}
