package com.onushi.testapp;

import com.onushi.testrecording.RecordTestForThis;
import org.springframework.stereotype.Component;

@Component
public class Service2 {
    @RecordTestForThis
    public void serve() throws InterruptedException {
        System.out.println("serve start");
        Thread.sleep(2000);
        System.out.println("serve end");
    }
}
