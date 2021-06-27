/*
 *
 * Copyright (c) 2021 spring-test-recorder contributors
 * This program is made available under the terms of the MIT License.
 *
 */

package org.springtestrecorder.sample.model;

import java.util.List;

public class CyclicParent {
    public int id;
    public List<CyclicChild> childList;
}
