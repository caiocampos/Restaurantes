package br.campos.restaurantes.repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import br.campos.restaurantes.model.PratoDocument;
import br.campos.restaurantes.model.RestauranteDocument;

/**
 * Repositório de Prato
 *
 * @author Caio
 */
public interface PratoDocumentRepository extends MongoRepository<PratoDocument, String> {

	/**
	 * Busca lista de Pratos pelo "restaurante"
	 *
	 * @param restaurante Restaurante
	 * @return Lista de Pratos do Restaurante
	 */
	List<PratoDocument> findByRestaurante(RestauranteDocument restaurante);

	/**
	 * Busca lista de Pratos pelo nome
	 *
	 * @param name nome
	 * @return Lista de Pratos
	 */
	@Query("{ 'nome' : { $regex: '?0', $options: 'i' } }")
	List<PratoDocument> findByNomeRegexIgnoreCase(String name);
}
