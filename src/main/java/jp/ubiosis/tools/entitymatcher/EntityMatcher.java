package jp.ubiosis.tools.entitymatcher;

import java.lang.reflect.Field;
import java.util.List;
import java.util.function.Function;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

import com.google.common.collect.Lists;

import jp.ubiosis.tools.entitymatcher.annotation.AssertField;
import jp.ubiosis.tools.entitymatcher.annotation.AssertField.IfNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class EntityMatcher<M> extends BaseMatcher<M> {

	private Matcher<M> matcher;

	public EntityMatcher(AssertModel<M> expected) {
		List<Matcher<? super M>> matchers = Lists.newArrayList();
		try{
			for (Class<?> c = expected.getClass(); c != Object.class; c = c.getSuperclass()) {
				for (Field f : c.getDeclaredFields()) {
					f.setAccessible(true);
					Object field = f.get(expected);

					AssertField af = f.getAnnotation(AssertField.class);

					if(af!=null && af.ifNull()==IfNull.SKIP && field==null){
						continue;
					}else{
						Matcher<?> matcher = field instanceof Matcher ? (Matcher<?>) field : Matchers.is(f.get(expected));
						AttributeMatcher<M,?> unit = new AttributeMatcher<>( f.getName(), field(f.getName()), matcher );
						matchers.add(unit);
					}
				}
			}
		}catch(Exception e){
			throw new AssertionError(e);
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
			} catch (Exception e) {
				throw new AssertionError(e);
			}
		};
	}

	@Factory
	public static <M> Matcher<M> assertEntity(AssertModel<M> expected){
		return new EntityMatcher<>(expected);
	}
}
