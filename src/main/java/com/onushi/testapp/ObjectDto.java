package com.onushi.testapp;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@Builder
public class ObjectDto {
    private String typeName;
    private String value;

    public static ObjectDto fromObject(Object object) {
        return ObjectDto.builder()
            .typeName(object.getClass().toString())
            .value(object.toString())
            .build();
    }
}
