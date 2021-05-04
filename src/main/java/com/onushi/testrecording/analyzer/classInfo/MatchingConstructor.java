package com.onushi.testrecording.analyzer.classInfo;

import com.onushi.testrecording.analyzer.object.FieldValue;
import lombok.Builder;
import lombok.Data;

import java.lang.reflect.Constructor;
import java.util.List;

@Data
@Builder
public class MatchingConstructor {
    private Constructor<?> constructor;
    private List<FieldValue> orderOfFields;
    private boolean fieldsCouldHaveDifferentOrder;
}
