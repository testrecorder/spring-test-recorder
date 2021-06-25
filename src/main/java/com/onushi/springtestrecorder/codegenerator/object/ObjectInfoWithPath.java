/*
 *
 * Copyright (c) 2021 spring-test-recorder contributors
 * This program is made available under the terms of the MIT License.
 *
 */

package com.onushi.springtestrecorder.codegenerator.object;


import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class ObjectInfoWithPath {
    private final ObjectInfo objectInfo;
    private final String path;

    public ObjectInfoWithPath(ObjectInfo objectInfo, String path) {
        this.objectInfo = objectInfo;
        this.path = path;
    }
}
