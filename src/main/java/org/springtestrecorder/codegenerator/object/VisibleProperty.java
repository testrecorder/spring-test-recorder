/*
 *
 * Copyright (c) 2021 spring-test-recorder contributors
 * This program is made available under the terms of the MIT License.
 *
 */

package org.springtestrecorder.codegenerator.object;

import org.springtestrecorder.codegenerator.test.TestRecordingPhase;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
public class VisibleProperty {
    // all VisibleProperty  have at least one snapshot since addVisiblePropertySnapshot is used
    protected String key;
    protected Map<TestRecordingPhase, VisiblePropertySnapshot> snapshots;

    public VisibleProperty(String key) {
        this.key = key;
        this.snapshots = new LinkedHashMap<>();
    }

    public VisiblePropertySnapshot getFirstSnapshot() {
        for (TestRecordingPhase testRecordingPhase : snapshots.keySet()) {
            return snapshots.get(testRecordingPhase);
        }
        return null;
    }

    public VisiblePropertySnapshot getLastSnapshot() {
        VisiblePropertySnapshot result = null;
        for (TestRecordingPhase testRecordingPhase : snapshots.keySet()) {
            result = snapshots.get(testRecordingPhase);
        }
        return result;
    }

    public boolean hasAfterMethodRunSnapshot() {
        return this.snapshots.get(TestRecordingPhase.AFTER_METHOD_RUN) != null;
    }

    @Override
    public String toString() {
        return "VisibleProperty{" +
                "key='" + key + '\'' +
                ", snapshots.size()=" + snapshots.size() +
                '}';
    }
}
