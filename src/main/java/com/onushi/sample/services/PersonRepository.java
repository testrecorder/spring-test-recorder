package com.onushi.sample.services;

import com.onushi.sample.model.Person;

@SuppressWarnings("SameReturnValue")
public interface PersonRepository {
    Person getPersonFromDB(int id) throws Exception;

    int getPersonsCountFromDB(String firstParam, String secondParam);
}
