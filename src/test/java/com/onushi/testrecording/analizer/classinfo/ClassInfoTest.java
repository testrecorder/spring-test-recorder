package com.onushi.testrecording.analizer.classinfo;

import com.onushi.testrecording.analizer.classInfo.ClassInfo;
import lombok.Builder;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@Data
@Builder
class PersonWithBuilder {
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
}

@Data
class Person {
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
}


class ClassInfoTest {
    @Test
    void isSpringComponent() {
    }

    @Test
    void canBeCreatedWithNoArgsConstructor() {
    }

    @Test
    void canBeCreatedWithLombokBuilder() throws ClassNotFoundException {
        ClassInfo classInfo = new ClassInfo(Class.forName("com.onushi.testrecording.analizer.classinfo.PersonWithBuilder"));
        assertTrue(classInfo.canBeCreatedWithLombokBuilder());
        classInfo = new ClassInfo(Class.forName("com.onushi.testrecording.analizer.classinfo.Person"));
        assertFalse(classInfo.canBeCreatedWithLombokBuilder());
    }

    @Test
    void canBeCreatedWithSetters() {
    }
}
