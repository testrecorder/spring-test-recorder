package com.onushi.sample.services;

import lombok.Getter;

@Getter
public class ServiceWithState {
    private int sampleInt;

    public ServiceWithState(int sampleInt) {
        this.sampleInt = sampleInt;
    }

    public void changeService(ServiceWithState serviceWithState) {
        serviceWithState.sampleInt = 10;
    }
}
