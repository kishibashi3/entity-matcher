package com.ubiosis.tools.entitymatcher.core;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Function;

public class Accessor {

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

}
