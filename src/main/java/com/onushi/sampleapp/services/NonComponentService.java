package com.onushi.sampleapp.services;

import com.onushi.testrecording.aspect.RecordMockForTest;

@RecordMockForTest
public class NonComponentService {
    public int add(int x, int y) {
        return x + y;
    }
}
