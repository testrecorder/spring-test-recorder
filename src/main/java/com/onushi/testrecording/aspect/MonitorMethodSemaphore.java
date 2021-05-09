package com.onushi.testrecording.aspect;

import org.springframework.stereotype.Component;

// TODO IB !!!! this should contain a List<MethodRunInfo> that are currently active
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
