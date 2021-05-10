package com.onushi.sampleapp.services;

import com.onushi.sampleapp.model.Person;

public interface PersonRepository {
    Person getPersonFromDB(int id) throws Exception;

    int getPersonsCountFromDB()  throws Exception;
}
