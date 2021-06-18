package com.onushi.springtestrecorder.codegenerator.object;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

// TODO IB !!!! 1 add Map<TestRecordingPhase, VisiblePropertyState> snapshot
@Getter
@Builder
public class VisibleProperty {
    protected PropertySource propertySource;
    protected List<String> requiredImports;
    protected List<String> requiredHelperObjects;

    protected PropertyValue finalValue;
    // dependencies that not come from value, like the key of a map
    protected List<ObjectInfo> finalDependencies;
}
