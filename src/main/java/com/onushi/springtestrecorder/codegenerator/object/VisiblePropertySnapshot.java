package com.onushi.springtestrecorder.codegenerator.object;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class VisiblePropertySnapshot {
    // TODO IB !!!! check these everywhere and clarify
    protected List<String> requiredImports;
    protected List<String> requiredHelperObjects;

    protected PropertyValue value;
    // dependencies that not come from value, like the key of a map
    protected List<ObjectInfo> otherDependencies;

}
