/*
 * Copyright 2015-2016 the original author or authors.
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
package com.ubiosis.tools.entitymatcher.hamcrest;

import static com.ubiosis.tools.entitymatcher.hamcrest.AttributeMatcher.expand;
import static org.hamcrest.CoreMatchers.allOf;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

import com.google.common.collect.ImmutableMap;
import com.ubiosis.tools.entitymatcher.core.BeanMatchingExtractor;
import com.ubiosis.tools.entitymatcher.model.Criteria.Rule;
import com.ubiosis.tools.entitymatcher.model.ExpectedCriteria;

/**
 * Hamcreset Matcher for Every Bean Classes.
 * 
 * @author ishibashi.kazuhiro@u-biosis.com
 * @param <M> asserting model class type.
 * @see ExpectedCriteria
 */
public class BeanMatcher<E extends ExpectedCriteria<M>, M> extends BaseMatcher<M> {

    private static final Map<Class<?>, BeanMatchingExtractor<?, ?>> extractors = new HashMap<>();

    @SuppressWarnings({ "unchecked", "rawtypes" })
    Map<Rule, Function<Object, Matcher<?>>> functions = ImmutableMap.of(
            Rule.COMPARE, o -> Matchers.comparesEqualTo((Comparable) o),
            Rule.IS, o -> Matchers.is(o),
            Rule.REGEX, o -> new RegexMatcher(o.toString()),
            Rule.MATCHER, o -> (Matcher<Object>) o);

    private final Matcher<M> matcher;

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public BeanMatcher(E expected) {
        Class<E> type = (Class<E>) expected.getClass();
        BeanMatchingExtractor<E, M> extractor = BeanMatchingExtractor.getExtractor(type);

        matcher = (Matcher<M>) allOf(extractor.extract(expected,
                (name, rule, field, actGetter) -> expand(name, (Function) actGetter, functions.get(rule).apply(field))));

    }

    @Override
    public boolean matches(Object actual) {
        return matcher.matches(actual);
    }

    @Override
    public void describeTo(Description desc) {
        desc.appendText("entity fields");
        matcher.describeTo(desc);
    }

    /**
     * matching factory method.
     * 
     * @param <M> model type.
     * @param expected expected object.
     * @return matcher
     */
    @Factory
    public static <E extends ExpectedCriteria<M>, M> Matcher<M> matches(E expected) {
        return new BeanMatcher<>(expected);
    }
}
