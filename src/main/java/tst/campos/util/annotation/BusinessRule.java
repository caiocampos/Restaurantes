package tst.campos.util.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import tst.campos.util.BusinessRuleAdapter;

/**
 * Anotação que aponta qual é a Classe que controla as regras da entidade
 *
 * @author Caio
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface BusinessRule {

	@SuppressWarnings("rawtypes")
	Class<? extends BusinessRuleAdapter> value();
}
