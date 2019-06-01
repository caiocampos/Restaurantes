package br.campos.restaurantes.util.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import br.campos.restaurantes.util.Searcher;

/**
 * Anotação que aponta qual é o buscador da Entidade e os critérios disponíveis
 *
 * @author Caio
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface SpecialSearch {

	@SuppressWarnings("rawtypes")
	Class<? extends Searcher> searcher();

	String[] queries();
}
