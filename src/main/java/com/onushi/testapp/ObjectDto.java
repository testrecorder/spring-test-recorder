package com.onushi.testapp;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ObjectDto {
    private String className;
    private String value;
}
