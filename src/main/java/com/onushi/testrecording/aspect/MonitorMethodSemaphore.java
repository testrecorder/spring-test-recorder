package com.onushi.testrecording.aspect;

import org.springframework.stereotype.Component;

@Component
public class MonitorMethodSemaphore {
    private transient boolean isMonitoring = false;

    public boolean isMonitoring() {
        return isMonitoring;
    }

    public void setMonitoring(boolean monitoring) {
        isMonitoring = monitoring;
    }
}
