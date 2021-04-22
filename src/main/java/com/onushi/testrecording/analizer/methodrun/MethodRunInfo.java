package com.onushi.testrecording.analizer.methodrun;

import lombok.Getter;

import java.util.*;

// TODO IB Do I need to differentiate objects tested / sent as params / being the result?
// TODO IB handle exceptions being thrown

@Getter
public class MethodRunInfo {
    protected Object target;
    protected String methodName;
    protected List<Object> arguments;
    protected Object result;

    protected MethodRunInfo() {}
}
