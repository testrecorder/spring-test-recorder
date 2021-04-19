package com.onushi.testrecording.analizer;

import com.onushi.testrecording.dto.ObjectDto;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class ObjectAnalyzerImpl implements ObjectAnalyzer {
    // TODO IB !!!! make it work in all the simple cases
    // TODO IB !!!! create a class analyzer to check if the constructors of the object being tested has dependencies

    // TODO IB something special for char -> (char)c
    // TODO IB handle objects sent as params
    // TODO IB handle objects received as result
    // TODO IB handle exceptions being thrown
    // TODO IB maybe I can use serialization to transform to Json and back
    @Override
    public ObjectDto analyzeObject(Object object) {
        String className;
        String value;
        if (object == null) {
            className = "null";
            value = "null";
        } else {
            className = object.getClass().getName();
            // TODO !!!! some of this must actually be moved to Generate
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
                case "java.util.Date":
                    Date date = (Date)object;
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                    String dateStr = simpleDateFormat.format(date);
                    value = String.format("new SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss.SSS\").parse(\"%s\")", dateStr);
                    break;
                default:
                    value = object.toString();
                    break;
            }
        }

        return ObjectDto.builder()
                .className(className)
                .value(value)
                .build();
    }
}
