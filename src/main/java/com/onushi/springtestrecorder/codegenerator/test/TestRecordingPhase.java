package com.onushi.springtestrecorder.codegenerator.test;

public enum TestRecordingPhase {
    BEFORE_METHOD_RUN,
    // TODO IB !!!! the mock could return something, but then the function being tested could modify that object
    DURING_METHOD_RUN,
    AFTER_METHOD_RUN
}
