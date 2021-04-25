package com.onushi.testrecording.analizer.methodrun;

import lombok.Builder;
import lombok.Getter;

import java.util.*;

// TODO IB Do I need to differentiate objects tested / sent as params / being the result?
// TODO IB handle exceptions being thrown

@Getter
@Builder
public class MethodRunInfo {
    protected Object target;
    protected String methodName;
    protected List<Object> arguments;
    protected Class<?> resultType;
    protected Object result;

    protected MethodRunInfo() {}

    public MethodRunInfo(Object target, String methodName, List<Object> arguments, Class<?> resultType, Object result) {
        this.target = target;
        this.methodName = methodName;
        this.arguments = arguments;
        this.resultType = resultType;
        this.result = result;
    }
}
