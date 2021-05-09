package com.onushi.testrecording.analyzer.methodrun;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.*;

// TODO IB !!!! add another class to store calls to services stored in target, send as params to target
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class MethodRunInfo {
    protected Object target;
    protected String methodName;
    protected List<Object> arguments;
    protected Class<?> fallBackResultType;
    protected Object result;
    protected Exception exception;
}
