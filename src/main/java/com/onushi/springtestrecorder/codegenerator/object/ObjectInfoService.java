package com.onushi.springtestrecorder.codegenerator.object;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// TODO IB !!!! continue here and recursively compare ObjectInfos
// TODO IB !!!! avoid cyclic traversal
// TODO IB !!!! avoid cyclic traversal when doing asserts too. but maybe it's solved from context already
// TODO IB !!!! test cyclic
@Service
public class ObjectInfoService {
    /* TODO IB !!!! 1 reactivate
        public boolean objectInfoChangedDeep(ObjectInfo objectInfo) {
            if (objectInfo == null || objectInfo.getVisibleProperties() == null) {
                return false;
            }
            for (Map.Entry<String, VisibleProperty> entry : objectInfo.getVisibleProperties().entrySet()) {
                VisibleProperty visibleProperty = entry.getValue();
                if (visiblePropertyChangedDeep(visibleProperty)) {
                    return true;
                }
            }
            return false;
        }

        // TODO IB !!!! how about the case when there was a property and it's no longer present?
        public boolean visiblePropertyChangedDeep(VisibleProperty visibleProperty) {
            VisiblePropertySnapshot firstSnapshot = visibleProperty.getFirstSnapshot();
            VisiblePropertySnapshot lastSnapshot = visibleProperty.getLastSnapshot();
            if (visibleProperty.getSnapshots().values().size() > 1) {
                return !isSameValueDeep(firstSnapshot.getValue(), lastSnapshot.getValue());
            } else if (visibleProperty.hasAfterMethodRunSnapshot()) {
                PropertyValue lastValue = lastSnapshot.getValue();
                if (lastValue.getObjectInfo() != null) {
                    return objectInfoChangedDeep(lastValue.getObjectInfo());
                } else {
                    return false;
                }
            }
            return false;
        }


        public boolean isSameValueDeep(PropertyValue otherPropertyValue1, PropertyValue otherPropertyValue2) {
            if (otherPropertyValue2 == null && otherPropertyValue1 == null) {
                return true;
            }
            if (otherPropertyValue2 == null || otherPropertyValue1 == null) {
                return false;
            }

            if (otherPropertyValue2.getString() != null) {
                return otherPropertyValue2.getString().equals(otherPropertyValue1.getString());
            } else if (otherPropertyValue2.getObjectInfo() != null) {
                // TODO IB !!!! test if an ObjectInfo has some changes
                // return otherPropertyValue2.getObjectInfo() == otherPropertyValue1.getObjectInfo();
                if (otherPropertyValue2.getObjectInfo() != otherPropertyValue1.getObjectInfo()) {
                    return false;
                } else {
                    return !objectInfoChangedDeep(otherPropertyValue2.getObjectInfo());
                }
            }
            return otherPropertyValue1.getString() == null && otherPropertyValue1.getObjectInfo() == null;
        }

        // TODO IB !!!! test this
        // TODO IB !!!! test all functions
        public boolean isSameObjectInfoConsideringAfterMethodRunSnapshotDeep(ObjectInfo objectInfo1, ObjectInfo objectInfo2) {
            if (objectInfo1 == null && objectInfo2 == null) {
                return true;
            }
            if (objectInfo1 == null || objectInfo2 == null) {
                return false;
            }
            List<VisibleProperty> visibleProperties1 = objectInfo1.getVisibleProperties().values().stream()
                    .filter(x -> x.getAfterMethodRunSnapshot() != null)
                    .collect(Collectors.toList());
            List<VisibleProperty> visibleProperties2 = objectInfo2.getVisibleProperties().values().stream()
                    .filter(x -> x.getAfterMethodRunSnapshot() != null)
                    .collect(Collectors.toList());
            if (visibleProperties1.size() != visibleProperties2.size()) {
                return false;
            }
            for (VisibleProperty visibleProperty1 : visibleProperties1) {
                PropertyValue propertyValue1 = visibleProperty1.getAfterMethodRunSnapshot().getValue();
                VisibleProperty visibleProperty2 = visibleProperties2.stream()
                        .filter(x -> x.getKey().equals(visibleProperty1.getKey()))
                        .findFirst().orElse(null);
                if (visibleProperty2 == null) {
                    return false;
                }
                PropertyValue propertyValue2 = visibleProperty2.getAfterMethodRunSnapshot().getValue();
                boolean isSameValue = isSameValueConsideringAfterMethodRunSnapshotDeep(propertyValue1, propertyValue2);
                if (!isSameValue) {
                    return false;
                }
            }
            return true;
        }

        // TODO IB !!!! do we need both? refactor
        public boolean isSameValueConsideringAfterMethodRunSnapshotDeep(PropertyValue otherPropertyValue1, PropertyValue otherPropertyValue2) {
            if (otherPropertyValue2 == null && otherPropertyValue1 == null) {
                return true;
            }
            if (otherPropertyValue1 == null || otherPropertyValue2 == null) {
                return false;
            }
            if (otherPropertyValue2.getString() != null) {
                return otherPropertyValue2.getString().equals(otherPropertyValue1.getString());
            } else if (otherPropertyValue2.getObjectInfo() != null) {
                return isSameObjectInfoConsideringAfterMethodRunSnapshotDeep(otherPropertyValue2.getObjectInfo(), otherPropertyValue1.getObjectInfo());
            } else {
                return otherPropertyValue1.getString() == null && otherPropertyValue1.getObjectInfo() == null;
            }
        }
     */
}
