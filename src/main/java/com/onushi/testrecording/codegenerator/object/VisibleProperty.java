package com.onushi.testrecording.codegenerator.object;

import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class VisibleProperty {
    protected PropertySource propertySource;
    // protected String initialValue = null;
    protected PropertyValue finalValue;
    // dependencies that not come from value, like the key of a map
    // protected List<ObjectInfo> initialDependencies = new ArrayList<>();
    // protected List<ObjectInfo> finalDependencies;
    protected List<String> requiredImports;
    protected List<String> requiredHelperObjects;
}
