/*
 *
 * Copyright (c) 2021 spring-test-recorder contributors
 * This program is made available under the terms of the MIT License.
 *
 */

package org.springtestrecorder.sample.model;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class StudentWithSetters {
    private String firstName;
    private int age;
    private int isolation;
    private int isModule;
    private boolean isOnline;
    private Boolean isOnline1;
    private boolean registered;
    private boolean isometric;
    private boolean is;
    private boolean isa;
    private boolean isA;
    private String otherField;

    public StudentWithSetters setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public StudentWithSetters setAge(int age) {
        this.age = age;
        return this;
    }

    public StudentWithSetters setIsolation(int isolation) {
        this.isolation = isolation;
        return this;
    }

    public StudentWithSetters setIsModule(int isModule) {
        this.isModule = isModule;
        return this;
    }

    public StudentWithSetters setOnline(boolean online) {
        isOnline = online;
        return this;
    }

    public StudentWithSetters setOnline1(Boolean online1) {
        isOnline1 = online1;
        return this;
    }

    public StudentWithSetters setRegistered(boolean registered) {
        this.registered = registered;
        return this;
    }

    public StudentWithSetters setIsometric(boolean isometric) {
        this.isometric = isometric;
        return this;
    }

    public StudentWithSetters setIs(boolean is) {
        this.is = is;
        return this;
    }

    public StudentWithSetters setIsa(boolean isa) {
        this.isa = isa;
        return this;
    }

    public StudentWithSetters setA(boolean a) {
        isA = a;
        return this;
    }

    public void setOtherField(String otherField) {
        this.otherField = otherField;
    }
}
