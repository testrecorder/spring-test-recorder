package com.onushi.testapp;

import com.onushi.testrecording.RecordTestForThis;
import org.springframework.stereotype.Component;

@Component
public class Service2 {
    @RecordTestForThis
    public int add(int x, int y) throws InterruptedException {
        Thread.sleep(100);
        return x + y;
    }
}
