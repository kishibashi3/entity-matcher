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

import java.util.regex.Pattern;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

/**
 * assert by Regular Expression.
 * 
 * @author ishibashi.kazuhiro@u-biosis.com
 *
 * @param <T>
 *            asserting object type.
 */
public class RegexMatcher<T> extends BaseMatcher<T> {

    private final String regexp;

    public RegexMatcher(String regexp) {
        this.regexp = regexp;
    }

    @Override
    public boolean matches(Object actual) {
        return Pattern.matches(regexp, actual.toString());
    }

    @Override
    public void describeTo(Description desc) {
        desc.appendText("matches " + regexp + " ");
    }

}
