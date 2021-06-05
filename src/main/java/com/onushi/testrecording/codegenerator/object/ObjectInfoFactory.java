package com.onushi.testrecording.codegenerator.object;

import com.onushi.testrecording.analyzer.classInfo.ClassInfoService;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

public abstract class ObjectInfoFactory {
    abstract ObjectInfo createObjectInfo(ObjectInfoCreationContext context);

    protected String getElementsClassName(List<ObjectInfo> objectInfos) {
        List<String> distinct = objectInfos.stream()
                .filter(x -> !x.inlineCode.equals("null"))
                .map(ObjectInfo::getActualClassName)
                .distinct()
                .collect(Collectors.toList());
        if (distinct.size() == 1) {
            return distinct.get(0);
        } else {
            return "Object";
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
                objectInfo.addVisibleProperty(key, VisibleProperty.builder()
                        .propertySource(PropertySource.fromGetter(publicGetter))
                        .finalValue(PropertyValue.fromObjectInfo(valueObjectInfo))
                        .build());
            } catch (Exception ex) {
                // TODO IB !!!! add a comment
            }
        }

        List<Field> publicFields = classInfoService.getPublicFields(objectInfo.getObject().getClass());
        for (Field publicField : publicFields) {
            try {
                Object value = publicField.get(objectInfo.getObject());
                ObjectInfo valueObjectInfo =
                        objectInfoFactoryManager.getCommonObjectInfo(context.getTestGenerator(), value);
                String key = "." + publicField.getName();
                objectInfo.addVisibleProperty(key, VisibleProperty.builder()
                        .propertySource(PropertySource.fromField(publicField))
                        .finalValue(PropertyValue.fromObjectInfo(valueObjectInfo))
                        .build());
            } catch(Exception ex) {
                // TODO IB !!!! add a comment
            }
        }
    }
}
