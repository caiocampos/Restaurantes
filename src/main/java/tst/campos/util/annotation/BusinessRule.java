package tst.campos.util.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import tst.campos.util.BusinessRuleAdapter;

/**
 * Anotação que aponta qual é a Classe que controla as regras da entidade
 *
 * @author Caio
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface BusinessRule {
	Class<? extends BusinessRuleAdapter> value();
}
