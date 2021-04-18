package com.onushi.testrecording.dto;

import org.springframework.stereotype.Component;

@Component
public class ObjectDtoConverter {
    // TODO IB !!!! make it work in all the simple cases
    // TODO IB something special for char -> (char)c
    // TODO handle objects sent as params
    // TODO handle objects received as result
    // TODO handle exceptions being thrown
    public ObjectDto createObjectDto(Object object) {
        String className;
        String value;
        if (object == null) {
            className = "null";
            value = "null";
        } else {
            className = object.getClass().getName();
            switch (className) {
                case "java.lang.Float":
                    value = object + "f";
                    break;
                case "java.lang.Long":
                    value = object + "L";
                    break;
                case "java.lang.byte":
                    value = "(byte)" + object;
                    break;
                case "java.lang.short":
                    value = "(short)" + object;
                    break;
                case "java.lang.String":
                    value = "\"" + object + "\"";
                    break;
                default:
                    value = object.toString();
                    break;
            }
        }

        return new ObjectDto(className, value);
    }
}
