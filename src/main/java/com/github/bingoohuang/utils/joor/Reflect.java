package com.github.bingoohuang.utils.joor;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.LinkedHashMap;
import java.util.Map;

public class Reflect {

    public static Reflect on(String name) throws ReflectException {
        return on(forName(name));
    }

    public static Reflect on(Class<?> clazz) {
        return new Reflect(clazz);
    }

    public static Reflect on(Object object) {
        return new Reflect(object);
    }

    private final Object object;

    private final boolean isClass;

    private Reflect(Class<?> type) {
        this.object = type;
        this.isClass = true;
    }

    private Reflect(Object object) {
        this.object = object;
        this.isClass = false;
    }

    public <T> T get() {
        return (T) object;
    }

    public Reflect set(String name, Object value) throws ReflectException {
        try {

            // Try setting a public field
            Field field = type().getField(name);
            field.set(object, unwrap(value));
            return this;
        }
        catch (Exception e1) {
            boolean accessible = true;
            Field field = null;

            // Try again, setting a non-public field
            try {
                field = type().getDeclaredField(name);
                accessible = field.isAccessible();
                if (!accessible) field.setAccessible(true);

                field.set(object, unwrap(value));
                return this;
            }
            catch (Exception e2) {
                throw new ReflectException(e2);
            }
            finally {
                if (field != null && !accessible) {
                    field.setAccessible(false);
                }
            }
        }
    }
    
    public Reflect set(Field field, Object value) throws ReflectException {
        try {
            field.set(object, unwrap(value));
            return this;
        }
        catch (Exception e1) {
            boolean accessible = true;

            // Try again, setting a non-public field
            try {
                accessible = field.isAccessible();
                if (!accessible) field.setAccessible(true);

                field.set(object, unwrap(value));
                return this;
            }
            catch (Exception e2) {
                throw new ReflectException(e2);
            }
            finally {
                if (field != null && !accessible) {
                    field.setAccessible(false);
                }
            }
        }
    }

    public <T> T get(String name) throws ReflectException {
        return field(name).<T> get();
    }

    public <T> T get(Field field) throws ReflectException {
        boolean accessible = true;
        try {
            accessible = field.isAccessible();
            if (!accessible) field.setAccessible(true);
            return on(field.get(object)).<T> get();
        }
        catch (Exception e) {
            throw new ReflectException(e);
        }
        finally {
            if (!accessible) field.setAccessible(false);
        }
    }

    public Reflect field(String name) throws ReflectException {
        try {

            // Try getting a public field
            Field field = type().getField(name);
            return on(field.get(object));
        }
        catch (Exception e1) {
            Field field = null;
            boolean accessible = true;

            // Try again, getting a non-public field
            try {
                field = type().getDeclaredField(name);
                accessible = field.isAccessible();

                if (!accessible) field.setAccessible(true);

                return on(field.get(object));
            }
            catch (Exception e2) {
                throw new ReflectException(e2);
            }
            finally {
                if (field != null && !accessible) {
                    field.setAccessible(false);
                }
            }
        }
    }

    public Map<String, Reflect> fields() {
        Map<String, Reflect> result = new LinkedHashMap<String, Reflect>();

        for (Field field : type().getFields()) {
            if (!isClass ^ Modifier.isStatic(field.getModifiers())) {
                String name = field.getName();
                result.put(name, field(name));
            }
        }

        return result;
    }


    public Reflect call(String name) throws ReflectException {
        return call(name, new Object[0]);
    }

    public Reflect call(String name, Object... args) throws ReflectException {
        Class<?>[] types = types(args);

        // Try invoking the "canonical" method, i.e. the one with exact
        // matching argument types
        try {
            Method method = type().getMethod(name, types);
            return on(method, object, args);
        }

        // If there is no exact match, try to find one that has a "similar"
        // signature if primitive argument types are converted to their wrappers
        catch (NoSuchMethodException e) {
            for (Method method : type().getMethods()) {
                if (method.getName().equals(name) && match(method.getParameterTypes(), types)) { return on(method,
                        object, args); }
            }

            throw new ReflectException(e);
        }
    }

    public Reflect create() throws ReflectException {
        return create(new Object[0]);
    }

    public Reflect create(Object... args) throws ReflectException {
        Class<?>[] types = types(args);

        // Try invoking the "canonical" constructor, i.e. the one with exact
        // matching argument types
        try {
            Constructor<?> constructor = type().getConstructor(types);
            return on(constructor, args);
        }

        // If there is no exact match, try to find one that has a "similar"
        // signature if primitive argument types are converted to their wrappers
        catch (NoSuchMethodException e) {
            for (Constructor<?> constructor : type().getConstructors()) {
                if (match(constructor.getParameterTypes(), types)) { return on(constructor, args); }
            }

            throw new ReflectException(e);
        }
    }

    public <P> P as(Class<P> proxyType) {
        final InvocationHandler handler = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                return on(object).call(method.getName(), args).get();
            }
        };

        return (P) Proxy.newProxyInstance(proxyType.getClassLoader(), new Class[] { proxyType }, handler);
    }

    private boolean match(Class<?>[] declaredTypes, Class<?>[] actualTypes) {
        if (declaredTypes.length == actualTypes.length) {
            for (int i = 0; i < actualTypes.length; i++) {
                if (!wrapper(declaredTypes[i]).isAssignableFrom(wrapper(actualTypes[i]))) { return false; }
            }

            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return object.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Reflect) { return object.equals(((Reflect) obj).get()); }

        return false;
    }

    @Override
    public String toString() {
        return object.toString();
    }

    private static Reflect on(Constructor<?> constructor, Object... args) throws ReflectException {
        try {
            return on(constructor.newInstance(args));
        }
        catch (Exception e) {
            throw new ReflectException(e);
        }
    }

    private static Reflect on(Method method, Object object, Object... args) throws ReflectException {
        boolean accessible = method.isAccessible();

        try {
            if (!accessible) method.setAccessible(true);

            if (method.getReturnType() == void.class) {
                method.invoke(object, args);
                return on(object);
            }
            else {
                return on(method.invoke(object, args));
            }
        }
        catch (Exception e) {
            throw new ReflectException(e);
        }
        finally {
            if (!accessible) {
                method.setAccessible(false);
            }
        }
    }

    private static Object unwrap(Object object) {
        if (object instanceof Reflect) { return ((Reflect) object).get(); }

        return object;
    }

    private static Class<?>[] types(Object... values) {
        if (values == null) { return new Class[0]; }

        Class<?>[] result = new Class[values.length];

        for (int i = 0; i < values.length; i++) {
            result[i] = values[i].getClass();
        }

        return result;
    }

    private static Class<?> forName(String name) throws ReflectException {
        try {
            return Class.forName(name);
        }
        catch (Exception e) {
            throw new ReflectException(e);
        }
    }

    public Class<?> type() {
        if (isClass) {
            return (Class<?>) object;
        }
        else {
            return object.getClass();
        }
    }

    private static Class<?> wrapper(Class<?> type) {
        if (boolean.class == type) {
            return Boolean.class;
        }
        else if (int.class == type) {
            return Integer.class;
        }
        else if (long.class == type) {
            return Long.class;
        }
        else if (short.class == type) {
            return Short.class;
        }
        else if (byte.class == type) {
            return Byte.class;
        }
        else if (double.class == type) {
            return Double.class;
        }
        else if (float.class == type) {
            return Float.class;
        }
        else if (char.class == type) {
            return Character.class;
        }
        else {
            return type;
        }
    }

}
