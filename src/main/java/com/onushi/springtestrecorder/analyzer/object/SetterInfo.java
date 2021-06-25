/*
 *
 * Copyright (c) 2021 spring-test-recorder contributors
 * This program is made available under the terms of the MIT License.
 *
 */

package com.onushi.springtestrecorder.analyzer.object;

import lombok.Builder;
import lombok.Getter;

import java.lang.reflect.Method;

@Getter
@Builder
public class SetterInfo {
    private final String name;
    private final boolean isForBuilder;
    private final Method setter;
}
