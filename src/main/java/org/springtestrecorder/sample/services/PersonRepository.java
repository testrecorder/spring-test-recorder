/*
 *
 * Copyright (c) 2021 spring-test-recorder contributors
 * This program is made available under the terms of the MIT License.
 *
 */

package org.springtestrecorder.sample.services;

import org.springtestrecorder.sample.model.Person;

@SuppressWarnings("SameReturnValue")
public interface PersonRepository {
    Person getPersonFromDB(int id) throws Exception;

    int getPersonsCountFromDB(String firstParam, String secondParam);
}
