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
package com.ubiosis.tools.hamcrest.entitymatcher;

import java.util.function.Function;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;

import lombok.AllArgsConstructor;
import lombok.ToString;

/**
 * Assert entity's field.
 * 
 * @author ishibashi.kazuhiro@u-biosis.com
 *
 * @param <M> entity type.
 * @param <A> field type.
 */
@AllArgsConstructor
@ToString
public class AttributeMatcher<M,A> extends BaseMatcher<M>{
    
    private String title;
    private Function<M,A> getter;
    private Matcher<A> matcher;
    
    @Override
    public void describeTo(Description desc) {
        desc.appendText(title + " ");
        matcher.describeTo(desc);
    }
    @Override
    @SuppressWarnings("unchecked")
    public boolean matches(Object actual) {
        return matcher.matches( getter.apply((M)actual) );
    }
    /**
     * assert method factory.
     * 
     * @param title assert description about attribute to assert.
     * @param getter converter of model to attribute.
     * @param matcher attribute matcher.
     * @return
     */
    @Factory
    public static <M,A> Matcher<M> expand(String title, Function<M,A> getter, Matcher<A> matcher){
        return new AttributeMatcher<>(title, getter, matcher);
    }
}
