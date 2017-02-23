package jp.ubiosis.tools.entitymatcher.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AssertField {
	
	IfNull ifNull() default IfNull.EVALUATE;

	
	public enum IfNull{ SKIP, EVALUATE }
}
