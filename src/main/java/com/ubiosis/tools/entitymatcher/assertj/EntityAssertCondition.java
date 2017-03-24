/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ubiosis.tools.entitymatcher.assertj;

import static org.assertj.core.api.Assertions.allOf;

import java.util.Map;
import java.util.function.Function;

import org.assertj.core.api.Condition;

import com.google.common.collect.ImmutableMap;
import com.ubiosis.tools.entitymatcher.core.BeanMatchingExtractor;
import com.ubiosis.tools.entitymatcher.core.Function4;
import com.ubiosis.tools.entitymatcher.model.Criteria.Rule;
import com.ubiosis.tools.entitymatcher.model.ExpectedCriteria;

/**
 * EntityMatcher for AssertJ.
 * 
 * @author ishibashi.kazuhirio@u-biosis.com
 *
 */
public class EntityAssertCondition {

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static Map<Rule, Function4<String, Object, Function, String, Condition<?>>> map = ImmutableMap.of(
            Rule.IS, (name, field, actGetter, desc) -> new Condition<>(m -> actGetter.apply(m).equals(field), desc),

            Rule.COMPARE, (name, field, actGetter, desc) -> new Condition<>(m -> ((Comparable) actGetter.apply(m)).compareTo(field) == 0, desc),

            Rule.REGEX, (name, field, actGetter, desc) -> new Condition<>(m -> actGetter.apply(m).toString().matches(field.toString()), desc),

            Rule.CONDITION, (name, field, actGetter, desc) -> new Condition<>(m -> ((Condition) field).matches(actGetter.apply(m)), desc));

    /**
     * entity matching Condition.
     * 
     * @param <M> model type.
     * @param expected expected model.
     * @return Condition
     */
    @SuppressWarnings("unchecked")
    public static <E extends ExpectedCriteria<M>, M> Condition<M> matches(E expected) {
        Class<E> type = (Class<E>) expected.getClass();
        BeanMatchingExtractor<E, M> extractor = BeanMatchingExtractor.getExtractor(type);

        return allOf(extractor.extract(expected,
                (name, rule, field, actGetter) -> (Condition<M>) map.get(rule).apply(name, field, actGetter, String.format("%s = %s", name, field))));

    }
}
