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

import static com.ubiosis.tools.entitymatcher.hamcrest.AttributeMatcher.expand;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

import com.ubiosis.tools.entitymatcher.annotation.AssertField;
import com.ubiosis.tools.entitymatcher.annotation.AssertModel;
import com.ubiosis.tools.entitymatcher.annotation.AssertModels;
import com.ubiosis.tools.entitymatcher.annotation.AssertField.Rule;

import lombok.extern.log4j.Log4j2;

/**
 * JUnit Matcher of Every Entities.
 * 
 * @author ishibashi.kazuhiro@u-biosis.com
 * @param <M> asserting model class type.
 * @see AssertModel
 */
@Log4j2
public class EntityMatcher<M> extends BaseMatcher<M> {

    private Matcher<M> matcher;

    public EntityMatcher(AssertModel<M> expected) {
        List<Matcher<? super M>> matchers = new ArrayList<>();
        
        AssertModels models = expected.getClass().getAnnotation(AssertModels.class);
        boolean getter = models==null? false : models.getter();
        
        try {
            for (Class<?> c = expected.getClass(); c != Object.class; c = c.getSuperclass()) {
                for (Field f : c.getDeclaredFields()) {
                    f.setAccessible(true);
                    Object field = f.get(expected);
                    String name = f.getName();

                    AssertField af = f.getAnnotation(AssertField.class);

                    if(af!=null && af.skipIfNull() && field==null) continue;
                    Rule rule = field instanceof Matcher ? Rule.MATCHER : af==null ? Rule.IS :af.rule();
                    
                    matchers.add( expand( name, getter ? get(name) : field(name), rule.matcher(field) ));
                }
            }
        }catch(SecurityException | IllegalAccessException e){
            throw new AssertionError("illegal assert model" , e);
        }
        matchers.forEach(m -> log.info("matcher = {}", m));
        matcher = Matchers.allOf(matchers);
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

    @SuppressWarnings("unchecked")
    private static <M,A> Function<M,A> field(String fieldName){

        return m -> {
            try {
                Field f = m.getClass().getDeclaredField(fieldName);
                f.setAccessible(true);
                return (A)f.get(m);
            } catch (NoSuchFieldException | IllegalAccessException | SecurityException e) {
                throw new AssertionError("illegal assert model.", e);
            }
        };
    }
    @SuppressWarnings("unchecked")
    private static <M,A> Function<M,A> get(String fieldName){

        return m -> {
            try {
                Method mm = m.getClass().getDeclaredMethod("get" + fieldName.substring(0,1).toUpperCase() + fieldName.substring(1));
                mm.setAccessible(true);
                return (A)mm.invoke(m);
            } catch (NoSuchMethodException | IllegalAccessException | SecurityException e) {
                throw new AssertionError("illegal assert model.", e);
            } catch (IllegalArgumentException | InvocationTargetException e) {
                throw new AssertionError("illegal assert model.", e);
            }
        };
    }
    /**
     * assert factory method.
     * 
     * @param expected expected object.
     * @return matcher
     */
    @Factory
    public static <M> Matcher<M> assertEntity(AssertModel<M> expected){
        return new EntityMatcher<>(expected);
    }
}
