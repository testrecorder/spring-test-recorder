package com.onushi.sampleapp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
public class StudentWithBuilder {
    private String firstName;
    private String lastName;
    private int age;
//    private boolean isOnline;
//    private boolean registered;
//    private boolean isab;

    private StudentWithBuilder() {
    }

//    public StudentWithBuilder setOnline(boolean online) {
//        isOnline = online;
//        return this;
//    }
//
//    public StudentWithBuilder setRegistered(boolean registered) {
//        this.registered = registered;
//        return this;
//    }
//
//    public StudentWithBuilder setIsab(boolean isab) {
//        this.isab = isab;
//        return this;
//    }
}
