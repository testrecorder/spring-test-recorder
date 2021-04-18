package com.onushi.testrecording;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// TODO IB Can I mock hidden dependencies? If yes, I could send a list of classes for which to intercept methods

// Unit tests will be generated for this method from runtime parameters and result
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RecordTestForThis {
}
