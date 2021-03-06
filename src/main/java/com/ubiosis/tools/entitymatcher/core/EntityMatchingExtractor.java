package com.ubiosis.tools.entitymatcher.core;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.ubiosis.tools.entitymatcher.model.AssertField;
import com.ubiosis.tools.entitymatcher.model.AssertField.Rule;
import com.ubiosis.tools.entitymatcher.model.AssertModel;
import com.ubiosis.tools.entitymatcher.model.AssertModels;

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
public class EntityMatchingExtractor<M, V> {

    Function4<String, Rule, Object, Function<M, ?>, V> function;

    public EntityMatchingExtractor(Function4<String, Rule, Object, Function<M, ?>, V> function) {
        this.function = function;
    }

    public List<V> extract(AssertModel<M> expected) {
        List<V> matchers = new ArrayList<>();

        AssertModels models = expected.getClass().getAnnotation(AssertModels.class);
        boolean getter = models == null ? false : models.getter();

        try {
            for (Class<?> c = expected.getClass(); c != Object.class; c = c.getSuperclass()) {
                for (Field f : c.getDeclaredFields()) {
                    f.setAccessible(true);
                    Object field = f.get(expected);
                    String name = f.getName();

                    AssertField af = f.getAnnotation(AssertField.class);

                    Function<M, ?> actualGetter = getter
                            ? m -> Accessor.get(Accessor.getterName(name)).apply(m)
                            : m -> Accessor.field(name).apply(m);

                    if (af == null || !af.skipIfNull() || field != null) {
                        Rule rule = af == null ? Rule.IS : af.rule();
                        matchers.add(function.apply(name, rule, field, actualGetter));
                    }
                }
            }
        } catch (SecurityException | IllegalAccessException e) {
            throw new AssertionError("illegal assert model", e);
        }
        matchers.forEach(m -> log.info("matcher = {}", m));
        return matchers;
    }
}
