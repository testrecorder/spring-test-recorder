package com.onushi.sampleapp.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Department {
    private int id;
    private String name;
}
