package com.onushi.testapp;

import com.onushi.testrecording.RecordTestForThis;
import org.springframework.stereotype.Component;

@Component
public class MathService {
    public int add(int x, int y) {
        return x + y;
    }

    public int negate(int x) {
        return -x;
    }

    public int returnZero() {
        return 0;
    }

    @RecordTestForThis
    public float addFloats(float x, float y) {
        return x + y;
    }

}
