package br.campos.restaurantes.util;

/**
 * Classe que trata as regras para entidades
 *
 * @param <T> Documento do Mongo
 *
 * @author Caio
 */
public abstract class BusinessRuleAdapter<T extends MongoDocument> {

	/**
	 * Tratativa executada antes de salvar uma Entidade
	 *
	 * @param doc Entidade a salvar
	 * @return resultado depois da tratativa
	 * @throws br.campos.restaurantes.util.BadRequestException
	 */
	public T onSave(T doc) throws BadRequestException {
		return doc;
	}

	/**
	 * Tratativa executada antes de apagar uma Entidade
	 *
	 * @param doc Entidade a ser apagada
	 * @throws br.campos.restaurantes.util.BadRequestException
	 */
	public void onDelete(T doc) throws BadRequestException {
	}
}
