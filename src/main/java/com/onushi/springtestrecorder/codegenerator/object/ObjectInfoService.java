package com.onushi.springtestrecorder.codegenerator.object;

import org.springframework.stereotype.Service;

// TODO IB !!!! continue here and recursively compare ObjectInfos
// TODO IB !!!! avoid cyclic traversal
// TODO IB !!!! avoid cyclic traversal when doing asserts too. but maybe it's solved from context already
@Service
public class ObjectInfoService {
    public boolean isSameValue(PropertyValue otherPropertyValue1, PropertyValue otherPropertyValue2) {
        if (otherPropertyValue2 == null && otherPropertyValue1 == null) {
            return true;
        }
        if (otherPropertyValue1 == null || otherPropertyValue2 == null) {
            return false;
        }
        if (otherPropertyValue2.getString() != null) {
            return otherPropertyValue2.getString().equals(otherPropertyValue1.getString());
        } else if (otherPropertyValue2.getObjectInfo() != null) {
            // TODO IB !!!! test if an ObjectInfo has some changes
            return otherPropertyValue2.getObjectInfo() == otherPropertyValue1.getObjectInfo();
        }
        return otherPropertyValue1.getString() == null && otherPropertyValue1.getObjectInfo() == null;
    }
}
