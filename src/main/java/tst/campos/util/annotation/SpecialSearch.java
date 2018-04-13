package tst.campos.util.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import tst.campos.util.Searcher;

/**
 * Anotação que aponta qual é o buscador da Entidade e os critérios disponíveis
 *
 * @author Caio
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SpecialSearch {

	Class<? extends Searcher> searcher();

	String[] queries();
}
