/*
 *
 * Copyright (c) 2021 spring-test-recorder contributors
 * This program is made available under the terms of the MIT License.
 *
 */

package org.springtestrecorder.sample.services;

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

    public ServiceWithState returnService() {
        this.sampleInt++;
        return this;
    }

    public ServiceWithState returnService2(ServiceWithState serviceWithState1, ServiceWithState serviceWithState2) {
        serviceWithState1.sampleInt = 11;
        return serviceWithState1;
    }
}
