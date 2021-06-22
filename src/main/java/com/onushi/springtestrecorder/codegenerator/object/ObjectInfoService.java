package com.onushi.springtestrecorder.codegenerator.object;

import com.onushi.springtestrecorder.codegenerator.test.TestRecordingMoment;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ObjectInfoService {
    public boolean objectInfoEquivalent(ObjectInfo objectInfo1, @NonNull TestRecordingMoment testRecordingMoment1,
                                        ObjectInfo objectInfo2, @NonNull TestRecordingMoment testRecordingMoment2) {
        if (objectInfo1 == null && objectInfo2 == null) {
            return true;
        }
        if (objectInfo1 == null || objectInfo2 == null) {
            return false;
        }
        if (objectInfo1.getObjectName().equals(ObjectInfo.CYCLIC_OBJECT_REPLACEMENT) ||
                objectInfo2.getObjectName().equals(ObjectInfo.CYCLIC_OBJECT_REPLACEMENT)) {
            return true;
        }
        if (objectInfo1 == objectInfo2 && testRecordingMoment1 == testRecordingMoment2) {
            return true;
        }
        List<VisibleProperty> visibleProperties1 = objectInfo1.getVisibleProperties().values().stream()
                .filter(x -> (testRecordingMoment1 == TestRecordingMoment.FIRST_SNAPSHOT ? x.getFirstSnapshot() : x.getLastSnapshot()) != null)
                .collect(Collectors.toList());
        List<VisibleProperty> visibleProperties2 = objectInfo2.getVisibleProperties().values().stream()
                .filter(x -> (testRecordingMoment2 == TestRecordingMoment.FIRST_SNAPSHOT ? x.getFirstSnapshot() : x.getLastSnapshot()) != null)
                .collect(Collectors.toList());
        if (visibleProperties1.size() != visibleProperties2.size()) {
            return false;
        }
        for (VisibleProperty visibleProperty1 : visibleProperties1) {
            PropertyValue propertyValue1 = (testRecordingMoment1 == TestRecordingMoment.FIRST_SNAPSHOT ?
                    visibleProperty1.getFirstSnapshot().getValue() : visibleProperty1.getLastSnapshot().getValue());
            VisibleProperty visibleProperty2 = visibleProperties2.stream()
                    .filter(x -> x.getKey().equals(visibleProperty1.getKey()))
                    .findFirst().orElse(null);
            if (visibleProperty2 == null) {
                return false;
            }
            PropertyValue propertyValue2 = (testRecordingMoment2 == TestRecordingMoment.FIRST_SNAPSHOT ?
                    visibleProperty2.getFirstSnapshot().getValue() : visibleProperty2.getLastSnapshot().getValue());
            if (!propertyValuesEquivalent(propertyValue1, testRecordingMoment1, propertyValue2, testRecordingMoment2)) {
                return false;
            }
        }
        return true;
    }

    public boolean propertyValuesEquivalent(PropertyValue propertyValue1, TestRecordingMoment testRecordingMoment1,
                                             PropertyValue propertyValue2, TestRecordingMoment testRecordingMoment2) {
        if (propertyValue1.getString() != null) {
            if (propertyValue2.getString() == null) {
                return false;
            } else {
                return propertyValue1.getString().equals(propertyValue2.getString());
            }
        }

        if (propertyValue1.getObjectInfo() != null) {
            if (propertyValue2.getObjectInfo() == null) {
                return false;
            } else {
                return objectInfoEquivalent(propertyValue1.getObjectInfo(), testRecordingMoment1, propertyValue2.getObjectInfo(), testRecordingMoment2);
            }
        }

        return propertyValue2.getString() == null && propertyValue2.getObjectInfo() == null;
    }
}
