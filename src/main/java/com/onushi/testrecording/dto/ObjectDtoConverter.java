package com.onushi.testrecording.dto;

import com.onushi.testrecording.dto.ObjectDto;
import org.springframework.stereotype.Component;

@Component
public class ObjectDtoConverter {
    // TODO IB !!!! make it work in all the simple cases
    public static ObjectDto createObjectDto(Object object) {
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
                // TODO IB something special for char
                default:
                    value = object.toString();
                    break;
            }
        }

        return new ObjectDto(className, value);
    }
}
