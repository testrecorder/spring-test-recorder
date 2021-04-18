package com.onushi.testapp;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class TestRunDto {
    private String packageName;
    private String className;
    private String methodName;
    private List<ObjectDto> arguments;
    private ObjectDto result;
}
