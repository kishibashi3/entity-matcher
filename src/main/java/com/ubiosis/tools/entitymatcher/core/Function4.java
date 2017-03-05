package com.ubiosis.tools.entitymatcher.core;

import java.util.function.Function;

import org.assertj.core.util.Preconditions;

/**
 * funcion interface with 4 params.
 * 
 * @author ishibashi.kazuhiro@u-biosis.com
 *
 * @param <T> 1st param
 * @param <U> 2nd param
 * @param <V> 3rd param
 * @param <W> 4th param
 * @param <R> return
 */
@FunctionalInterface
public interface Function4<T, U, V, W, R> {

    /**
     * apply method.
     * 
     * @param t 1st param
     * @param u 2nd param
     * @param v 3rd param
     * @param w 4th param
     * @return return
     */
    R apply(T t, U u, V v, W w);

    /**
     * function connector.
     * 
     * @param <S> new return type.
     * @param after function and then
     * @return connected function
     */
    default <S> Function4<T, U, V, W, S> andThen(Function<? super R, ? extends S> after) {
        Preconditions.checkNotNull(after);
        return (T t, U u, V v, W w) -> after.apply(apply(t, u, v, w));
    }
}
