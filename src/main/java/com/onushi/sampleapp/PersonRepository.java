package com.onushi.sampleapp;

public interface PersonRepository {
    Person getPersonFromDB(int id) throws Exception;
}
