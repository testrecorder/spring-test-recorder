package com.onushi.testrecording.analyzer.object;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.lang.reflect.Method;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class FieldReadInfo {
    private FieldReadType fieldReadType;
    private FieldValue fieldValue;
    private Method getter;
    // the result of the getter might be diff from the internal field value
    private Object value;
}
