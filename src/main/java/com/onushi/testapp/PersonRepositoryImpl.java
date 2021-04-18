package com.onushi.testapp;

import org.springframework.stereotype.Component;
import java.text.SimpleDateFormat;

@Component
public class PersonRepositoryImpl implements PersonRepository {
    @Override
    public Person getPerson(int id) throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if (id == 1) {
            return Person.builder()
                    .firstName("Chuck")
                    .lastName("Norris")
                    .dateOfBirth(simpleDateFormat.parse("1940-03-10"))
                    .isActor(true)
                    .build();

        } else if (id == 2) {
            return Person.builder()
                    .firstName("Bruce")
                    .lastName("Lee")
                    .dateOfBirth(simpleDateFormat.parse("1940-11-27"))
                    .isActor(true)
                    .build();
        } else {
            return null;
        }
    }
}
