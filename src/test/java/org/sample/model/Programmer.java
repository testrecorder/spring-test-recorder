/*
 *
 * Copyright (c) 2021 spring-test-recorder contributors
 * This program is made available under the terms of the MIT License.
 *
 */

package org.sample.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

// For testing asserts on result fields
@Builder
@Setter
public class Programmer {
    public int id;
    public String firstName;
    public String lastName;
    public Department department;
    @Getter private double salary;
    @Getter private boolean isOnline;
}
