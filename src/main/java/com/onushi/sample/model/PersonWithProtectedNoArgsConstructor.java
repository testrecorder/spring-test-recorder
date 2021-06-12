package com.onushi.sample.model;

public class PersonWithProtectedNoArgsConstructor {
    protected PersonWithProtectedNoArgsConstructor() {
    }

    public static PersonWithProtectedNoArgsConstructor createService() {
        return new PersonWithProtectedNoArgsConstructor();
    }
}
