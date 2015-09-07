package com.github.bingoohuang.utils.lang;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class Typee {

    public static Class<?> getActualTypeArgument(Class<?> clz, int order) {
        Type genericType = clz.getGenericInterfaces()[order];
        if (!ParameterizedType.class.isAssignableFrom(genericType.getClass())) return Void.class;

        ParameterizedType parameterizedType = (ParameterizedType) genericType;
        try {
            return (Class<?>) parameterizedType.getActualTypeArguments()[0];
        } catch (ClassCastException ex) {
            return Void.class;
        }
    }
}
