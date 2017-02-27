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
/**
 * Mark interface to indicate the class is an AssertModel of {@code <M>}.
 * 
 * <h3>usage</h3>
 * <p>
 * <ol>
 * <li> create {@code <AssertModel>} class to assert {@code <Model>} class.
 * <li> implements {@code AsertModel<Model>}
 * <li> add all fields of model class to assert to.
 * <li> assertModel's field type is same as model's field type or Matcher of model's field type.
 * <li> if field type is primitive, assertModel's field can determine as the boxing type.
 * <li> each field can assert by equals, compare, regex, or any matcher. set @AssertFields(rule=Rule.?).
 * <li> if field is null, assert null as a value. if skip assertion, set @AssertField(skipIfNull=true).
 * </ol>
 * 
 * @author ishibashi.kazuhirio@u-biosis.com
 * @param <M> Model type to assert.
 * @see EntityMatcher
 * @see AssertField
 */
public interface AssertModel<M> {

}
