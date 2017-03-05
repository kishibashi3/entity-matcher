package com.ubiosis.tools.entitymatcher.core;

import java.util.function.Function;

import org.assertj.core.util.Preconditions;

@FunctionalInterface
public interface Function4<T, U, V, W, R> {

    R apply(T t, U u, V v, W w);

    default <S> Function4<T, U, V, W, S> andThen(Function<? super R, ? extends S> after) {
        Preconditions.checkNotNull(after);
        return (T t, U u, V v, W w) -> after.apply(apply(t, u, v, w));
    }
}
