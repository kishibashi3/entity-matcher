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
package com.ubiosis.tools.entitymatcher.assertj;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.Condition;
import org.junit.Before;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import com.ubiosis.tools.entitymatcher.model.Criteria;
import com.ubiosis.tools.entitymatcher.model.Criteria.Rule;
import com.ubiosis.tools.entitymatcher.model.ExpectedCriteria;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * test for AssertJ EntityAssertCondition.
 * 
 * @author ishibashi.kazuhiro@u-biosis.com
 *
 */
@Log4j2
public class EntityAssertConditionTest {

    @Before
    public void setup() {

    }

    @Test
    public void test() {
        Model m1 = new Model(1, "aaa");

        ModelAssertModel am = new ModelAssertModel(new Condition<>(l -> l < 3, " less than 3"), "[a-z]{3}");

        assertThat(m1).is(EntityAssertCondition.matches(am));

    }

    @Test
    public void testYaml() {
        Yaml yaml = new Yaml();
        Config conf = yaml.loadAs(ClassLoader.getSystemResourceAsStream("test.yml"), Config.class);
        log.error(conf.toString());

    }

    @AllArgsConstructor
    @Data
    public static class Model {

        private long id;

        private String name;

    }

    @AllArgsConstructor
    @Data
    @Criteria(byField = true)
    public static class ModelAssertModel implements ExpectedCriteria<Model> {

        @Criteria(rule = Rule.CONDITION)
        private Condition<Long> id;

        @Criteria(skipIfNull = true, rule = Rule.REGEX)
        private String name;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Criteria(byField = true)
    public static class ModelAssertModel2 implements ExpectedCriteria<Model> {

        private Long id;

        @Criteria(skipIfNull = true, rule = Rule.REGEX)
        private String name;
    }

    @Data
    public static class Config {

        List<ModelAssertModel2> models = new ArrayList<>();
    }
}
