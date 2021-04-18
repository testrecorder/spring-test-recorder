package com.onushi.testapp;

import com.onushi.testrecording.dto.TestRunDto;

public interface TestGenerator {
    String getTestString(TestRunDto testRunDto);
}
