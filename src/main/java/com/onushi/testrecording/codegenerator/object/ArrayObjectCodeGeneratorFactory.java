package com.onushi.testrecording.codegenerator.object;

import com.onushi.testrecording.codegenerator.template.StringGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ArrayObjectCodeGeneratorFactory {
    private final ObjectCodeGeneratorFactory objectCodeGeneratorFactory;

    public ArrayObjectCodeGeneratorFactory(ObjectCodeGeneratorFactory objectCodeGeneratorFactory) {
        this.objectCodeGeneratorFactory = objectCodeGeneratorFactory;
    }

    private class ArrayAsList {
        public Class<?> elementClass;
        public List<?> list;

        public ArrayAsList(Class<?> elementClass, List<?> list) {
            this.elementClass = elementClass;
            this.list = list;
        }
    }

    public ObjectCodeGenerator createObjectCodeGenerator(Object object, String objectName) {
        ObjectCodeGenerator objectCodeGenerator = new ObjectCodeGenerator(object, objectName, false, objectName);

        ArrayAsList arrayAsList = getElementList(object);

        StringGenerator stringGenerator = new StringGenerator();
        stringGenerator.setTemplate("{{elementClassShort}}[] {{objectName}} = {{{elementsInlineCode}}};\n");

        stringGenerator.addAttribute("elementClassShort", arrayAsList.elementClass.getSimpleName());
        stringGenerator.addAttribute("objectName", objectName);
        stringGenerator.addAttribute("elementsInlineCode", getElementsInlineCode(arrayAsList.list));

        objectCodeGenerator.initCode = stringGenerator.generate();
        return objectCodeGenerator;
    }

    // TODO IB this part seems to be repeated in multiple factories
    private String getElementsInlineCode(List<?> list) {
        List<ObjectCodeGenerator> elementObjectCodeGenerators = new ArrayList<>();
        for(Object element: list) {
            ObjectCodeGenerator elementObjectCodeGenerator = objectCodeGeneratorFactory.createObjectCodeGenerator(element, "ignored");
            elementObjectCodeGenerators.add(elementObjectCodeGenerator);
        }
        String elementsInlineCode = elementObjectCodeGenerators.stream()
                .map(ObjectCodeGenerator::getInlineCode).collect(Collectors.joining(", "));
        return elementsInlineCode;
    }

    private ArrayAsList getElementList(Object arrayObject) {
        String arrayClassName = arrayObject.getClass().getName();
        switch(arrayClassName) {
            case "[Z":
                boolean[] booleanArray = (boolean[]) arrayObject;
                List<Boolean> booleanList = new ArrayList<>();
                for (boolean element : booleanArray) {
                    booleanList.add(element);
                }
                return new ArrayAsList(boolean.class, booleanList);
            case "[B":
                byte[] byteArray = (byte[]) arrayObject;
                List<Byte> byteList = new ArrayList<>();
                for (byte element : byteArray) {
                    byteList.add(element);
                }
                return new ArrayAsList(byte.class, byteList);
            case "[C":
                char[] charArray = (char[]) arrayObject;
                List<Character> charList = new ArrayList<>();
                for (char element : charArray) {
                    charList.add(element);
                }
                return new ArrayAsList(char.class, charList);
            case "[D":
                double[] doubleArray = (double[]) arrayObject;
                return new ArrayAsList(double.class, Arrays.stream(doubleArray).boxed().collect(Collectors.toList()));
            case "[F":
                float[] floatArray = (float[]) arrayObject;
                List<Float> floatList = new ArrayList<>();
                for (float element : floatArray) {
                    floatList.add(element);
                }
                return new ArrayAsList(float.class, floatList);
            case "[I":
                int[] intArray = (int[]) arrayObject;
                return new ArrayAsList(int.class, Arrays.stream(intArray).boxed().collect(Collectors.toList()));
            case "[J":
                long[] longArray = (long[]) arrayObject;
                return new ArrayAsList(long.class, Arrays.stream(longArray).boxed().collect(Collectors.toList()));
            case "[S":
                short[] shortArray = (short[]) arrayObject;
                List<Short> shortList = new ArrayList<>();
                for (short element : shortArray) {
                    shortList.add(element);
                }
                return new ArrayAsList(short.class, shortList);
            default:
                try {
                    // arrays of arrays have double L here "[LL..."
                    String elementClassName = arrayClassName.substring(2, arrayClassName.length() - 1);
                    Class<?> clazz = Class.forName(elementClassName);
                    return new ArrayAsList(clazz, Arrays.stream((Object[]) arrayObject).map(clazz::cast).collect(Collectors.toList()));
                } catch (ClassNotFoundException e) {
                    return new ArrayAsList(Object.class, Arrays.stream((Object[]) arrayObject).collect(Collectors.toList()));
                }
        }
    }
}


