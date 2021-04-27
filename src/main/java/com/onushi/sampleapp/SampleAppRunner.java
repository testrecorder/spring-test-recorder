package com.onushi.sampleapp;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class SampleAppRunner implements CommandLineRunner {
    private final SampleService sampleService;
    private final PersonService personService;

    public SampleAppRunner(SampleService sampleService, PersonService personService) {
        this.sampleService = sampleService;
        this.personService = personService;
    }

    @Override
    public void run(String... args) throws Exception {
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        Date d1 = simpleDateFormat.parse("2021-01-01");
//        Date d2 = simpleDateFormat.parse("2021-02-02");
//        Date min = sampleService.minDate(d1, d2);
//
//        Date d3 = simpleDateFormat.parse("2021-01-02");
//        Date d4 = simpleDateFormat.parse("2021-02-03");
//        Date min1 = sampleService.minDate(d3, d4);
//
//        sampleService.addInternal(5, 6);
          sampleService.add(5, 6);
//        sampleService.addFloats(2f, 3f);
//        sampleService.logicalAnd(true, true);
//        sampleService.toYYYY_MM_DD_T_HH_MM_SS_Z(new Date(), new Date());
//        sampleService.testTypes((short)6, (byte)4, 5, true, 'c', 1.5);

        // personService.loadPerson(1);

        // sampleService.testException(5);

//        Person person = Person.builder()
//                .isActor(true)
//                .build();
    }
}
