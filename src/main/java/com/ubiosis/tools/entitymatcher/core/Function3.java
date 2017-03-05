package com.ubiosis.tools.entitymatcher.core;

import java.util.function.Function;

import com.google.common.base.Preconditions;

/**
 * funcion interface with 3 params.
 * 
 * @author ishibashi.kazuhiro@u-biosis.com
 *
 * @param <T> 1st param type
 * @param <U> 2nd param type
 * @param <V> 3rd param type
 * @param <R> return type
 */
@FunctionalInterface
public interface Function3<T, U, V, R> {

    /**
     * apply method.
     * 
     * @param t 1st param
     * @param u 2nd param
     * @param v 3rd param
     * @return return
     */
    R apply(T t, U u, V v);

    /**
     * function connector.
     * 
     * @param <S> new return type.
     * @param after function and then
     * @return connected function
     */
    default <S> Function3<T, U, V, S> andThen(Function<? super R, ? extends S> after) {
        Preconditions.checkNotNull(after);
        return (T t, U u, V v) -> after.apply(apply(t, u, v));
    }
}
