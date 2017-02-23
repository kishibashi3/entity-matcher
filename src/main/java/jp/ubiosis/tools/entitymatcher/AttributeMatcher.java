package jp.ubiosis.tools.entitymatcher;

import java.util.function.Function;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
class AttributeMatcher<M,A> extends BaseMatcher<M>{
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
}
