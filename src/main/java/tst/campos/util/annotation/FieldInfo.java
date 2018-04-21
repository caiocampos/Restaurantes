package tst.campos.util.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Anotação que engloba todas as informações do campo
 *
 * @author Caio
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldInfo {

	String name();

	String label();

	enum FieldType {
		TEXT, TEXTAREA, NUMBER, VALUE, PHONE, TOGGLE, LIST, PASS, FOREIGN
	};

	FieldType type() default FieldType.TEXT;

	String[] options() default {};

	boolean required() default false;

	int size() default 3;
}
