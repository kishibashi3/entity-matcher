package com.ubiosis.tools.entitymatcher.assertj;

import static org.assertj.core.api.Assertions.allOf;

import java.util.Map;
import java.util.function.Function;

import org.assertj.core.api.Condition;

import com.google.common.collect.ImmutableMap;
import com.ubiosis.tools.entitymatcher.core.EntityMatchingExtractor;
import com.ubiosis.tools.entitymatcher.core.Function4;
import com.ubiosis.tools.entitymatcher.model.AssertField.Rule;
import com.ubiosis.tools.entitymatcher.model.AssertModel;

public class EntityAssertCondition {

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static Map<Rule, Function4<String, Object, Function, String, Condition<?>>> map = ImmutableMap.of(
            Rule.IS, (name, field, actGetter, desc) -> new Condition<>(m -> actGetter.apply(m).equals(field), desc),

            Rule.COMPARE, (name, field, actGetter, desc) -> new Condition<>(m -> ((Comparable) actGetter.apply(m)).compareTo(field) == 0, desc),

            Rule.REGEX, (name, field, actGetter, desc) -> new Condition<>(m -> actGetter.apply(m).toString().matches(field.toString()), desc),

            Rule.CONDITION, (name, field, actGetter, desc) -> new Condition<>(m -> ((Condition) field).matches(actGetter.apply(m)), desc));

    @SuppressWarnings({ "unchecked" })
    public static <M> Condition<M> assertEntity(AssertModel<M> expected) {
        EntityMatchingExtractor<M, Condition<M>> extractor = new EntityMatchingExtractor<M, Condition<M>>(
                (name, rule, field, actGetter) -> (Condition<M>) map.get(rule).apply(name, field, actGetter, String.format("%s = %s", name, field)));

        return allOf(extractor.extract(expected));

    }
}
