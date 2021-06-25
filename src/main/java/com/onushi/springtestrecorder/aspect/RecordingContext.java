/*
 *
 * Copyright (c) 2021 spring-test-recorder contributors
 * This program is made available under the terms of the MIT License.
 *
 */

package com.onushi.springtestrecorder.aspect;

import com.onushi.springtestrecorder.codegenerator.test.TestGenerator;
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
