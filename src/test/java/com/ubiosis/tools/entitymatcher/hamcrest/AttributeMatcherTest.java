/*
 * Copyright 2015-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ubiosis.tools.entitymatcher.hamcrest;

import static com.ubiosis.tools.entitymatcher.hamcrest.AttributeMatcher.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.ubiosis.tools.entitymatcher.hamcrest.EntityMatcherTest.Model;

public class AttributeMatcherTest {

    @Test
    public void test(){
        Model model = new Model(1,"aaa");
        assertThat(model, expand("model.id", Model::getId, is(1L)));
        
    }
}
