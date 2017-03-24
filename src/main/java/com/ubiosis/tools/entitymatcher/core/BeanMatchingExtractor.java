package com.ubiosis.tools.entitymatcher.core;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.ubiosis.tools.entitymatcher.model.Criteria;
import com.ubiosis.tools.entitymatcher.model.Criteria.Rule;
import com.ubiosis.tools.entitymatcher.model.ExpectedCriteria;

import lombok.extern.log4j.Log4j2;

/**
 * Core entity-matcher logic.
 * 
 * @author ishibashi.kazuhiro@u-biosis.com
 *
 * @param <M> model type.
 * @param <V> matcher type.
 */
@Log4j2
public class BeanMatchingExtractor<E extends ExpectedCriteria<M>, M> {

    private static final Map<Class<?>, BeanMatchingExtractor<?, ?>> EXTRACTORS = new HashMap<>();

    @SuppressWarnings("unchecked")
    public static <E extends ExpectedCriteria<M>, M> BeanMatchingExtractor<E, M> getExtractor(Class<E> type) {
        return (BeanMatchingExtractor<E, M>) EXTRACTORS.computeIfAbsent(
                type, k -> new BeanMatchingExtractor<>(type));

    }

    private final List<BiFunction<E, Function4<String, Rule, Object, Function<M, ?>, ?>, ?>> matchers;

    public BeanMatchingExtractor(Class<E> type) {

        final Criteria _criteria = type.getAnnotation(Criteria.class);
        final boolean _byfield = _criteria == null ? Criteria.DEFAULT_BY_FIELD : _criteria.byField();
        final boolean _skip = _criteria == null ? Criteria.DEFAULT_SKIP_IF_NULL : _criteria.skipIfNull();
        final Rule _rule = _criteria == null ? Criteria.DEFAULT_RULE : _criteria.rule();

        matchers = new ArrayList<>();

        try {
            for (Class<?> c = type; c != Object.class; c = c.getSuperclass()) {
                for (Field f : c.getDeclaredFields()) {
                    if (Modifier.isStatic(f.getModifiers())) continue;
                    f.setAccessible(true);

                    Function<E, ?> expected = o -> {
                        try {
                            return f.get(o);
                        } catch (IllegalAccessException e) {
                            return new Error("fail to get expected attribute.", e);
                        }
                    };
                    String name = f.getName();

                    Criteria criteria = f.getAnnotation(Criteria.class);
                    boolean byfield = criteria == null ? _byfield : criteria.byField();
                    boolean skip = criteria == null ? _skip : criteria.skipIfNull();
                    Rule rule = criteria == null ? _rule : criteria.rule();

                    Function<M, ?> actual = byfield
                            ? m -> Accessor.field(name).apply(m)
                            : m -> Accessor.get(name).apply(m);

                    matchers.add((exp, function) -> {
                        Object expField = expected.apply(exp);
                        return skip && expField == null
                                ? null : function.apply(name, rule, expField, actual);
                    });
                }
            }
        } catch (SecurityException e) {
            throw new AssertionError("illegal assert model", e);
        }
        matchers.forEach(m -> log.info("matcher = {}", m));
    }

    @SuppressWarnings("unchecked")
    public <V> List<V> extract(E expected, Function4<String, Rule, Object, Function<M, ?>, V> function) {
        return matchers.stream().map(f -> (V) f.apply(expected, function))
                .filter(l -> l != null)
                .collect(Collectors.toList());
    }
}
