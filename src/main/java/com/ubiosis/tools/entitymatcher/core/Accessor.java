package com.ubiosis.tools.entitymatcher.core;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Function;

/**
 * private field and getter's accessor.
 * 
 * @author ishibashi.kazuhiro@u-biosis.com
 *
 */
public class Accessor {

    /**
     * getters accessor.
     * 
     * @param <M> model type
     * @param <T> attribute type
     * @param name method name.
     * @return accessor function.
     */
    @SuppressWarnings("unchecked")
    public static <M, T> Function<M, T> get(String name) {

        return m -> {
            for (Class<?> c = m.getClass(); c != Object.class; c = c.getSuperclass()) {
                try {
                    Method method = c.getDeclaredMethod(name);
                    method.setAccessible(true);
                    return (T) method.invoke(m);

                } catch (NoSuchMethodException e) {
                    // do nothing.
                } catch (SecurityException | IllegalAccessException | InvocationTargetException e) {
                    throw new AssertionError(e);
                }
            }
            throw new NoSuchMethodError(name);
        };

    }

    /**
     * getter accessor.
     * 
     * @param <M> model type.
     * @param <T> attribute type.
     * @param type model type.
     * @param name method name.
     * @return accessor function.
     */
    @SuppressWarnings("unchecked")
    public static <M, T> Function<M, T> get(Class<M> type, String name) {

        for (Class<?> c = type; c != Object.class; c = c.getSuperclass()) {
            try {
                Method method = c.getDeclaredMethod(name);
                method.setAccessible(true);
                return m -> {
                    try {
                        return (T) method.invoke(m);
                    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                        throw new AssertionError(e);
                    }
                };
            } catch (NoSuchMethodException e) {
                // do nothing.
            } catch (SecurityException e) {
                throw new AssertionError(e);
            }
        }
        throw new NoSuchMethodError(name);
    }

    /**
     * field accessor.
     * 
     * @param <M> model type.
     * @param <T> attribute type.
     * @param name field name.
     * @return accessor function.
     */
    @SuppressWarnings("unchecked")
    public static <M, T> Function<M, T> field(String name) {

        return m -> {
            for (Class<?> c = m.getClass(); c != Object.class; c = c.getSuperclass()) {
                try {
                    Field field = c.getDeclaredField(name);
                    field.setAccessible(true);
                    return (T) field.get(m);

                } catch (NoSuchFieldException e) {
                    // do nothing.
                } catch (SecurityException | IllegalAccessException e) {
                    throw new AssertionError(e);
                }
            }
            throw new NoSuchMethodError(name);
        };

    }

    /**
     * field accecssor.
     * 
     * @param <M> model type.
     * @param <T> attribute type.
     * @param type model type.
     * @param name field name.
     * @return accessor function
     */
    @SuppressWarnings("unchecked")
    public static <M, T> Function<M, T> field(Class<M> type, String name) {

        for (Class<?> c = type; c != Object.class; c = c.getSuperclass()) {
            try {
                Field field = c.getDeclaredField(name);
                field.setAccessible(true);
                return m -> {
                    try {
                        return (T) field.get(m);
                    } catch (IllegalAccessException | IllegalArgumentException e) {
                        throw new AssertionError(e);
                    }
                };
            } catch (NoSuchFieldException e) {
                // do nothing.
            } catch (SecurityException e) {
                throw new AssertionError(e);
            }
        }
        throw new NoSuchFieldError(name);
    }

    /**
     * field name to getter name.
     * 
     * @param fieldName field name
     * @return getter name
     */
    public static String getterName(String fieldName) {
        return "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }
}
