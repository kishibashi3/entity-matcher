package com.ubiosis.tools.entitymatcher.core;

import java.util.function.Function;

import com.google.common.base.Preconditions;

@FunctionalInterface
public interface Function3<T, U, V, R> {

    R apply(T t, U u, V v);

    default <S> Function3<T, U, V, S> andThen(Function<? super R, ? extends S> after) {
        Preconditions.checkNotNull(after);
        return (T t, U u, V v) -> after.apply(apply(t, u, v));
    }
}
