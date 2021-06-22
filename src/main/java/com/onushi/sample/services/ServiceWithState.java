package com.onushi.sample.services;

public class ServiceWithState {
    public int sampleInt  = 5;


    public void changeService(ServiceWithState serviceWithState) {
        serviceWithState.sampleInt = 10;
    }
}
