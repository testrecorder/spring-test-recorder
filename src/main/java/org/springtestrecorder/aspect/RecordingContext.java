/*
 *
 * Copyright (c) 2021 spring-test-recorder contributors
 * This program is made available under the terms of the MIT License.
 *
 */

package org.springtestrecorder.aspect;

import org.springtestrecorder.codegenerator.test.TestGenerator;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Component
public class RecordingContext {
    private final Set<TestGenerator> testGeneratorSet = Collections.synchronizedSet(new HashSet<>());

    public Set<TestGenerator> getTestGeneratorSet() {
        return testGeneratorSet;
    }
}
