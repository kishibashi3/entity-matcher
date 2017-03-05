package com.ubiosis.tools.entitymatcher.assertj;

import static org.assertj.core.api.Assertions.allOf;

import org.assertj.core.api.Condition;

import com.ubiosis.tools.entitymatcher.annotation.AssertModel;
import com.ubiosis.tools.entitymatcher.core.Accessor;
import com.ubiosis.tools.entitymatcher.core.EntityMatchingExtractor;

public class EntityAssertCondition {

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <M> Condition<M> assertEntity(AssertModel<M> expected) {
        EntityMatchingExtractor<M, Condition<M>> extractor = new EntityMatchingExtractor<M, Condition<M>>(
                (name, rule, field) -> {
                    String desc = String.format("%s = %s", name, field);
                    switch (rule) {
                        case IS:
                            return new Condition<>(m -> Accessor.field(name).apply(m).equals(field), desc);
                        case COMPARE:
                            return new Condition<>(m -> ((Comparable) Accessor.field(name).apply(m)).compareTo(field) == 0, desc);
                        case REGEX:
                            return new Condition<>(m -> Accessor.field(name).apply(m).toString().matches(field.toString()), desc);
                        case CONDITION:
                            return new Condition<>(m -> {
                                Condition c = (Condition) field;
                                return c.matches(Accessor.field(name).apply(m));
                            }, desc);
                        default:
                            return null;
                    }
                });
        return allOf(extractor.extract(expected));

    }
}
