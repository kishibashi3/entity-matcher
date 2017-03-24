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

import static com.ubiosis.tools.entitymatcher.hamcrest.EntityMatcher.assertEntity;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Test;

import com.ubiosis.tools.entitymatcher.model.AssertField;
import com.ubiosis.tools.entitymatcher.model.AssertField.Rule;
import com.ubiosis.tools.entitymatcher.model.AssertModel;
import com.ubiosis.tools.entitymatcher.model.AssertModels;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * test for hamcrest EntityMatcher.
 * 
 * @author ishibashi.kazuhiro@u-biosis.com
 *
 */
public class EntityMatcherTest {

    @Test
    public void test() {
        Model m1 = new Model(1, "aaa");
        Model m2 = new Model(2, "bbb");
        List<Model> list = Arrays.asList(m1, m2);

        ModelAssertModel am = new ModelAssertModel(greaterThan(1L), "[b-z]{3}");

        assertThat(list, hasItem(assertEntity(am)));
    }

    @AllArgsConstructor
    @Data
    public static class Model {
        private long id;

        private String name;

    }

    @AllArgsConstructor
    @Data
    @AssertModels(getter = true)
    public static class ModelAssertModel implements AssertModel<Model> {
        @AssertField(rule = Rule.MATCHER)
        private Matcher<Long> id;

        @AssertField(skipIfNull = true, rule = Rule.REGEX)
        private String name;

    }
}
