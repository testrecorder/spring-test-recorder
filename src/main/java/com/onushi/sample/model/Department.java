package com.onushi.sample.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class Department {
    private int id;
    private String name;
}
