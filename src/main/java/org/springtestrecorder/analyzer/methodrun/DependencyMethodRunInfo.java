/*
 *
 * Copyright (c) 2021 spring-test-recorder contributors
 * This program is made available under the terms of the MIT License.
 *
 */

package org.springtestrecorder.analyzer.methodrun;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class DependencyMethodRunInfo {
    protected Object target;
    protected String methodName;
    protected List<Object> arguments;
    protected Class<?> fallBackResultType;
    protected Object result;
    protected Exception exception;
    protected long threadId;
}
