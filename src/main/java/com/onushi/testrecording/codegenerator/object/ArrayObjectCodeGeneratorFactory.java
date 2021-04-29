package com.onushi.testrecording.codegenerator.object;

import com.onushi.testrecording.codegenerator.template.StringGenerator;
import com.onushi.testrecording.codegenerator.test.TestGenerator;

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

    public ObjectCodeGenerator createObjectCodeGenerator(TestGenerator testGenerator, Object object, String objectName) {
        ObjectCodeGenerator objectCodeGenerator = new ObjectCodeGenerator(object, objectName, objectName);

        ArrayAsList arrayAsList = getElementList(object);

        objectCodeGenerator.dependencies = arrayAsList.list
                .stream()
                .distinct()
                .map(fieldValue -> objectCodeGeneratorFactory.getCommonObjectCodeGenerator(testGenerator, fieldValue))
                .collect(Collectors.toList());

        objectCodeGenerator.declareClassName = "TODO IB !!!!";

        objectCodeGenerator.initCode = new StringGenerator()
                .setTemplate("{{elementClassShort}}[] {{objectName}} = {{{elementsInlineCode}}};\n")
                .addAttribute("elementClassShort", arrayAsList.elementClass.getSimpleName())
                .addAttribute("objectName", objectName)
                .addAttribute("elementsInlineCode", getElementsInlineCode(objectCodeGenerator.dependencies))
                .generate();

        return objectCodeGenerator;
    }

    private String getElementsInlineCode(List<ObjectCodeGenerator> dependencies) {
        return dependencies.stream()
                .map(ObjectCodeGenerator::getInlineCode).collect(Collectors.joining(", "));
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


