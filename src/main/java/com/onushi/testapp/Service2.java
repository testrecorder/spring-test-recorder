package com.onushi.testapp;

import com.onushi.testrecording.RecordTestForThis;
import org.springframework.stereotype.Component;

@Component
public class Service2 {
    public int add(int x, int y) throws InterruptedException {
        return x + y;
    }

    public int negate(int x) throws InterruptedException {
        return -x;
    }

    @RecordTestForThis
    public int returnZero() throws InterruptedException {
        return 0;
    }
}
