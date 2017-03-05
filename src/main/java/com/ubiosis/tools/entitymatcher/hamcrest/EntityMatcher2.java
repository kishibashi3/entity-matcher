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

import java.util.Map;
import java.util.function.Function;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

import com.google.common.collect.ImmutableMap;
import com.ubiosis.tools.entitymatcher.annotation.AssertField.Rule;
import com.ubiosis.tools.entitymatcher.annotation.AssertModel;
import com.ubiosis.tools.entitymatcher.core.Accessor;
import com.ubiosis.tools.entitymatcher.core.EntityMatchingExtractor;

import lombok.extern.log4j.Log4j2;

/**
 * JUnit Matcher of Every Entities.
 * 
 * @author ishibashi.kazuhiro@u-biosis.com
 * @param <M>
 *            asserting model class type.
 * @see AssertModel
 */
@Log4j2
public class EntityMatcher2<M> extends BaseMatcher<M> {

    @SuppressWarnings({ "unchecked", "rawtypes" })
    Map<Rule, Function<Object, Matcher<?>>> map = ImmutableMap.of(
            Rule.COMPARE, o -> Matchers.comparesEqualTo((Comparable) o),
            Rule.IS, o -> Matchers.is(o),
            Rule.REGEX, o -> new RegexMatcher(o.toString()),
            Rule.MATCHER, o -> (Matcher<Object>) o);

    EntityMatchingExtractor<M, Matcher<M>> extractor;

    private Matcher<M> matcher;

    public EntityMatcher2(AssertModel<M> expected) {
        extractor = new EntityMatchingExtractor<M, Matcher<M>>(
                (name, rule, field) -> expand(name, Accessor.field(name), map.get(rule).apply(field)));
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
     * assert factory method.
     * 
     * @param expected
     *            expected object.
     * @return matcher
     */
    @Factory
    public static <M> Matcher<M> assertEntity(AssertModel<M> expected) {
        return new EntityMatcher2<>(expected);
    }
}
