/*
 *
 * Copyright (c) 2021 spring-test-recorder contributors
 * This program is made available under the terms of the MIT License.
 *
 */

package org.sample.model;

import java.util.Date;

public class CyclicChild {
    public CyclicParent parent;
    public Date date;
}
