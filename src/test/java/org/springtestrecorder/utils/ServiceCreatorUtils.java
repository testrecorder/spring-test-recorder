/*
 *
 * Copyright (c) 2021 spring-test-recorder contributors
 * This program is made available under the terms of the MIT License.
 *
 */

package org.springtestrecorder.utils;

import org.springtestrecorder.analyzer.classInfo.ClassInfoService;
import org.springtestrecorder.analyzer.object.ObjectCreationAnalyzerService;
import org.springtestrecorder.analyzer.object.ObjectStateReaderService;
import org.springtestrecorder.codegenerator.object.CglibService;
import org.springtestrecorder.codegenerator.object.ObjectInfoFactoryManager;
import org.springtestrecorder.codegenerator.object.ObjectInfoService;
import org.springtestrecorder.codegenerator.template.StringService;
import org.springtestrecorder.codegenerator.test.*;


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
                        new TestObjectsInitGeneratorService(new StringService()),
                        new StringService(),
                        new ObjectInfoService()
                )
        );
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
