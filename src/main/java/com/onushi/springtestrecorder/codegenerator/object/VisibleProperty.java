package com.onushi.springtestrecorder.codegenerator.object;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

// TODO IB !!!! for all objects set initialValue, finalValue, initialDependencies and finalDependencies
@Getter
@Builder
public class VisibleProperty {
    protected PropertySource propertySource;
    protected PropertyValue initialValue;
    protected PropertyValue finalValue;
    // dependencies that not come from value, like the key of a map
    // protected List<ObjectInfo> initialDependencies = new ArrayList<>();
    protected List<ObjectInfo> initialDependencies;
    protected List<ObjectInfo> finalDependencies;
    protected List<String> requiredImports;
    protected List<String> requiredHelperObjects;
}
