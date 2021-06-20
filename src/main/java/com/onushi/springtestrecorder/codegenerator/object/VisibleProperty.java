package com.onushi.springtestrecorder.codegenerator.object;

import com.onushi.springtestrecorder.codegenerator.test.TestRecordingPhase;
import lombok.Getter;
import java.util.LinkedHashMap;
import java.util.Map;

@Getter
public class VisibleProperty {
    // all VisibleProperty  have at least one snapshot since addVisiblePropertySnapshot is used
    protected Map<TestRecordingPhase, VisiblePropertySnapshot> snapshots;

    public VisibleProperty() {
        this.snapshots = new LinkedHashMap<>();
    }

    // TODO IB !!!! improve
    // TODO IB !!!! test that this is in order
    // TODO IB !!!! exception if there is no snapshot
    public VisiblePropertySnapshot getFirstSnapshot() {
        if (snapshots != null) {
            for (TestRecordingPhase testRecordingPhase : snapshots.keySet()) {
                return snapshots.get(testRecordingPhase);
            }
        }
        return null;
    }

    // TODO IB !!!! improve
    public VisiblePropertySnapshot getLastSnapshot() {
        if (snapshots != null) {
            VisiblePropertySnapshot result = null;
            for (TestRecordingPhase testRecordingPhase : snapshots.keySet()) {
                result = snapshots.get(testRecordingPhase);
            }
            return result;
        }
        return null;
    }

    public boolean hasAfterMethodRunSnapshot() {
        return this.snapshots.get(TestRecordingPhase.AFTER_METHOD_RUN) != null;
    }
}
