package com.onushi.testrecording.analizer.methodrun;

import lombok.Builder;
import lombok.Getter;

import java.util.*;


@Getter
@Builder
public class MethodRunInfo {
    protected Object target;
    protected String methodName;
    protected List<Object> arguments;
    // TODO IB !!!! rename to fallBackResultType
    protected Class<?> resultType;
    protected Object result;
    protected Exception exception;

    protected MethodRunInfo() {}

    public MethodRunInfo(Object target, String methodName, List<Object> arguments, Class<?> resultType, Object result, Exception exception) {
        this.target = target;
        this.methodName = methodName;
        this.arguments = arguments;
        this.resultType = resultType;
        this.result = result;
        this.exception = exception;
    }
}
