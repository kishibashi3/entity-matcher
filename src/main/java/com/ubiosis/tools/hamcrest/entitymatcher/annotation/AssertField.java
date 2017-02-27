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
package com.ubiosis.tools.hamcrest.entitymatcher.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

import com.ubiosis.tools.hamcrest.entitymatcher.RegexMatcher;

/**
 * AssertModel's field setting.
 * 
 * @author ishibashi.kazuhiro@u-biosis.com
 * @see ../AssertModel
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AssertField {

    /**
     * assertion about null value.
     * 
     * @return {@code true}: skip assertion. {@code false}: assert null as a value.
     */
    boolean skipIfNull() default true;

    /**
     * asserting rule.
     * 
     * @return rule.
     */
    Rule rule() default Rule.IS;


    /**
     * Asserting rules.
     */
    public enum Rule{
        /**
         * assert by Asserts.is
         */
        IS {
            @Override
            public Matcher<?> matcher(Object o){
                return Matchers.is(o);
            }
        } , 
        /**
         * assert by Asserts.compare
         */
        COMPARE{
            @SuppressWarnings({ "unchecked", "rawtypes" })
            @Override
            public Matcher<?> matcher(Object o){
                return Matchers.comparesEqualTo((Comparable)o);
            }
        }, 
        /**
         * assert by RegexMatcher.
         */
        REGEX {
            @Override
            public Matcher<?> matcher(Object o){
                return new RegexMatcher<>(o.toString());
            }
        },
        /**
         * assert by value itself.
         */
        MATCHER {
            @Override
            public Matcher<?> matcher(Object o){
                return (Matcher<?>)o;
            }
        }; 

        /**
         * asserting 
         * 
         * @param o
         * @return
         */
        public abstract Matcher<?> matcher(Object o);
    }
}
