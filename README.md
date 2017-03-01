[![CircleCI](https://circleci.com/gh/kishibashi3/entity-matcher/tree/master.svg?style=shield)](https://circleci.com/gh/kishibashi3/entity-matcher/tree/master)

entity matcher
---

EntityMatcher is a hamcrest custom matcher to test any model classes.


`AssertModel<Model>` is mark interface to indicate the class is an AssertModel of `Model`.
 
## usage


0. create AssertModel class to assert {@code <Model>} class.
0. implements `AsertModel<Model>`
0. add all fields of model class to assert to.
0. assertModel's field type is same as model's field type or Matcher of model's field type.
0. if field type is primitive, assertModel's field can determine as the boxing type.
0. each field can assert by equals, compare, regex, or any matcher. set @AssertFields(rule=Rule.?).
0. if field is null, assert null as a value. if skip assertion, set @AssertField(skipIfNull=true).


## dependency




## usage


