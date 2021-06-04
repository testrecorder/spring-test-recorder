package com.onushi.testrecording.codegenerator.object;

import lombok.Builder;
import lombok.Getter;

import java.lang.reflect.AccessibleObject;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class VisibleProperty {
    protected AccessibleObject fieldOrMethod = null;
    // protected String initialValue = null;
    protected PropertyValue finalValue = null;
    // dependencies that not come from value, like the key of a map
    // protected List<ObjectInfo> initialDependencies = new ArrayList<>();
    protected List<ObjectInfo> finalDependencies = new ArrayList<>();
}
