package com.ubiosis.tools.entitymatcher.core;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.ubiosis.tools.entitymatcher.annotation.AssertField;
import com.ubiosis.tools.entitymatcher.annotation.AssertField.Rule;
import com.ubiosis.tools.entitymatcher.annotation.AssertModel;
import com.ubiosis.tools.entitymatcher.annotation.AssertModels;
import com.ubiosis.tools.entitymatcher.annotation.TriFunction;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class EntityMatchingExtractor<M, V> {

    TriFunction<String, Rule, Object, V> function;

    public EntityMatchingExtractor(TriFunction<String, Rule, Object, V> function) {
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

                    if (af != null && af.skipIfNull() && field == null)
                        continue;
                    Rule rule = af == null ? Rule.IS : af.rule();
                    matchers.add(function.apply(name, rule, field));
                }
            }
        } catch (SecurityException | IllegalAccessException e) {
            throw new AssertionError("illegal assert model", e);
        }
        matchers.forEach(m -> log.info("matcher = {}", m));
        return matchers;
    }
}
