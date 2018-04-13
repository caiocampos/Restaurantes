package tst.campos.util;

import java.util.List;

/**
 * Definição de Interface para buscadores especiais
 *
 * @author Caio
 * @param <T>
 */
public interface Searcher<T extends MongoDocument> {

	/**
	 * Busca uma lista de Entidade pelo critério e parâmetros passados
	 *
	 * @param name Nome do critério
	 * @param param Parâmetros que devem ser usados
	 * @return Lista de Entidades
	 */
	List<T> search(String name, Object... param);

	/**
	 * Busca uma Entidade pelo critério e parâmetros passados
	 *
	 * @param name Nome do critério
	 * @param param Parâmetros que devem ser usados
	 * @return Entidade
	 */
	T searchOne(String name, Object... param);
}
