package com.onushi.testapp;

import com.onushi.testrecording.RecordTestForThis;
import org.springframework.stereotype.Component;

@Component
public class Service2 {
    @RecordTestForThis
    public int add(int x, int y) throws InterruptedException {
        System.out.println("serve start");
        Thread.sleep(500);
        System.out.println("serve end");
        return x + y;
    }
}
