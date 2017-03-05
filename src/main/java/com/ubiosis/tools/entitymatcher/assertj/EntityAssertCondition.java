package com.ubiosis.tools.entitymatcher.assertj;

import static org.assertj.core.api.Assertions.allOf;

import java.util.Map;

import org.assertj.core.api.Condition;

import com.google.common.collect.ImmutableMap;
import com.ubiosis.tools.entitymatcher.core.Accessor;
import com.ubiosis.tools.entitymatcher.core.EntityMatchingExtractor;
import com.ubiosis.tools.entitymatcher.core.TriFunction;
import com.ubiosis.tools.entitymatcher.model.AssertField.Rule;
import com.ubiosis.tools.entitymatcher.model.AssertModel;

public class EntityAssertCondition {

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static Map<Rule, TriFunction<String, Object, String, Condition<?>>> map = ImmutableMap.of(
            Rule.IS, (name, field, desc) -> new Condition<>(m -> Accessor.field(name).apply(m).equals(field), desc),

            Rule.COMPARE, (name, field, desc) -> new Condition<>(m -> ((Comparable) Accessor.field(name).apply(m)).compareTo(field) == 0, desc),

            Rule.REGEX, (name, field, desc) -> new Condition<>(m -> Accessor.field(name).apply(m).toString().matches(field.toString()), desc),

            Rule.CONDITION, (name, field, desc) -> new Condition<>(m -> ((Condition) field).matches(Accessor.field(name).apply(m)), desc));

    @SuppressWarnings({ "unchecked" })
    public static <M> Condition<M> assertEntity(AssertModel<M> expected) {
        EntityMatchingExtractor<M, Condition<M>> extractor = new EntityMatchingExtractor<M, Condition<M>>(
                (name, rule, field) -> {
                    return (Condition<M>) map.get(rule).apply(name, field, String.format("%s = %s", name, field));
                });

        return allOf(extractor.extract(expected));

    }
}
