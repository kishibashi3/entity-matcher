/*
 * Copyright 2016-2017 the original author or authors.
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

import static com.ubiosis.tools.entitymatcher.hamcrest.BeanMatcher.matches;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Test;

import com.ubiosis.tools.entitymatcher.model.Criteria;
import com.ubiosis.tools.entitymatcher.model.Criteria.Rule;
import com.ubiosis.tools.entitymatcher.model.ExpectedCriteria;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * test for hamcrest EntityMatcher.
 * 
 * @author ishibashi.kazuhiro@u-biosis.com
 *
 */
public class BeanMatcherTest {

    @Test
    public void test() {
        Model m1 = new Model(1, "aaa");
        Model m2 = new Model(2, "bbb");
        List<Model> list = Arrays.asList(m1, m2);

        ModelAssertModel am = new ModelAssertModel(greaterThan(1L), "[b-z]{3}");

        assertThat(list, hasItem(matches(am)));
    }

    @AllArgsConstructor
    @Data
    public static class Model {
        private long id;

        private String name;

    }

    @AllArgsConstructor
    @Data
    @Criteria(byField = false)
    public static class ModelAssertModel implements ExpectedCriteria<Model> {
        @Criteria(rule = Rule.MATCHER)
        private Matcher<Long> id;

        @Criteria(skipIfNull = true, rule = Rule.REGEX)
        private String name;

    }
}
