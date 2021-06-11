package com.onushi.testrecorder.analyzer.object;

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
