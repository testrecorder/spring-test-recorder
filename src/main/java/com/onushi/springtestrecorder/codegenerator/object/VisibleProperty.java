package com.onushi.springtestrecorder.codegenerator.object;

import com.onushi.springtestrecorder.codegenerator.test.TestRecordingPhase;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@NoArgsConstructor
public class VisibleProperty {
    // all VisibleProperty should have at least one snapshot
    protected Map<TestRecordingPhase, VisiblePropertySnapshot> snapshots;

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
}
