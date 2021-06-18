package com.onushi.springtestrecorder.codegenerator.object;

import com.onushi.springtestrecorder.analyzer.classInfo.ClassInfoService;
import com.onushi.springtestrecorder.codegenerator.test.TestRecordingPhase;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class ObjectInfoFactory {
    abstract ObjectInfo createObjectInfo(ObjectInfoCreationContext context);

    protected String getElementsComposedClassNameForDeclare(List<ObjectInfo> elements) {
        List<String> distinct = elements.stream()
                .filter(x -> !x.inlineCode.equals("null"))
                .map(ObjectInfo::getComposedClassNameForDeclare)
                .distinct()
                .collect(Collectors.toList());
        if (distinct.size() == 1) {
            return distinct.get(0);
        } else {
            return "Object";
        }
    }

    protected List<String> getElementsDeclareRequiredImports(List<ObjectInfo> elements) {
        List<ObjectInfo> distinct = elements.stream()
                .filter(x -> !x.inlineCode.equals("null"))
                .distinct()
                .collect(Collectors.toList());
        if (distinct.size() == 0) {
            return new ArrayList<>();
        } else {
            List<String> result = distinct.get(0).getDeclareRequiredImports();
            for (int i = 1; i < distinct.size(); i++) {
                List<String> toIntersect = distinct.get(i).getDeclareRequiredImports();
                result = result.stream()
                        .filter(toIntersect::contains)
                        .collect(Collectors.toList());
            }
            return result;
        }
    }


    protected void setVisiblePropertiesForUnknown(ObjectInfo objectInfo,
                ObjectInfoCreationContext context, ObjectInfoFactoryManager objectInfoFactoryManager,
                ClassInfoService classInfoService) {
        List<Method> publicGetters = classInfoService.getPublicGetters(objectInfo.getObject().getClass());
        for (Method publicGetter : publicGetters) {
            try {
                Object value = publicGetter.invoke(objectInfo.getObject());
                ObjectInfo valueObjectInfo =
                        objectInfoFactoryManager.getCommonObjectInfo(context.getTestGenerator(), value);
                String key = "." + publicGetter.getName() + "()";
                addVisiblePropertySnapshot(objectInfo, key, context.getTestGenerator().getCurrentTestRecordingPhase(),
                        VisiblePropertySnapshot.builder()
                                .value(PropertyValue.fromObjectInfo(valueObjectInfo))
                                .build());
            } catch (Exception ex) {
                // TODO IB add a comment
            }
        }

        List<Field> publicFields = classInfoService.getPublicFields(objectInfo.getObject().getClass());
        for (Field publicField : publicFields) {
            try {
                Object value = publicField.get(objectInfo.getObject());
                ObjectInfo valueObjectInfo =
                        objectInfoFactoryManager.getCommonObjectInfo(context.getTestGenerator(), value);
                String key = "." + publicField.getName();
                addVisiblePropertySnapshot(objectInfo, key, context.getTestGenerator().getCurrentTestRecordingPhase(),
                        VisiblePropertySnapshot.builder()
                                .value(PropertyValue.fromObjectInfo(valueObjectInfo))
                                .build());

            } catch(Exception ex) {
                // TODO IB add a comment
            }
        }
    }

    protected void addVisiblePropertySnapshot(ObjectInfo objectInfo, String key,
                  TestRecordingPhase testRecordingPhase, VisiblePropertySnapshot visiblePropertySnapshot) {
        VisibleProperty visibleProperty = objectInfo.visibleProperties.get(key);
        if (visibleProperty == null) {
            visibleProperty = new VisibleProperty();
            objectInfo.visibleProperties.put(key, visibleProperty);
        }
        visibleProperty.snapshots.put(testRecordingPhase, visiblePropertySnapshot);
    }
}
