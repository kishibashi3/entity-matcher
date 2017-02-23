package jp.ubiosis.tools.entitymatcher;

import static jp.ubiosis.tools.entitymatcher.EntityMatcher.assertEntity;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Test;

import jp.ubiosis.tools.entitymatcher.annotation.AssertField;
import jp.ubiosis.tools.entitymatcher.annotation.AssertField.IfNull;
import lombok.AllArgsConstructor;
import lombok.Data;

public class EntityMatcherTest {


    @Test
    public void test() {
        Model m1 = new Model(1, "aaa");
        Model m2 = new Model(2, "bbb");
        List<Model> list = Arrays.asList(m1, m2);

        ModelAssertModel am = new ModelAssertModel(greaterThan(1L), "aaa");
        am.setId(greaterThan(1L));

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
    public static class ModelAssertModel implements AssertModel<Model> {
        private Matcher<Long> id;
        @AssertField(ifNull=IfNull.SKIP)
        private String name;
    }
}
