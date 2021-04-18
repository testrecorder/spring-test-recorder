package com.onushi.testrecording.generator;

import com.onushi.testrecording.dto.TestRunDto;

public interface TestGenerator {
    String getTestString(TestRunDto testRunDto);
}
