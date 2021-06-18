package com.onushi.springtestrecorder.codegenerator.object;

import com.onushi.springtestrecorder.codegenerator.test.TestRecordingPhase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

// TODO IB !!!! clean
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VisibleProperty {
    protected Map<TestRecordingPhase, VisiblePropertySnapshot> snapshots;

    // TODO IB !!!! 4 remove all these
    // TODO IB !!!! 4 remove builder
    protected PropertySource propertySource;
    protected List<String> requiredImports;
    protected List<String> requiredHelperObjects;

    protected PropertyValue finalValue;
    // dependencies that not come from value, like the key of a map
    protected List<ObjectInfo> finalDependencies;
}
