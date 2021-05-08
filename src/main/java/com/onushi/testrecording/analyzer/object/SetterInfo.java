package com.onushi.testrecording.analyzer.object;

import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Method;

@Getter
@Setter
public class SetterInfo {
    private String name;
    private boolean isForBuilder;
    private Method method;
}
