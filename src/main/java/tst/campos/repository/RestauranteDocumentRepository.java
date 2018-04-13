package tst.campos.repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import tst.campos.model.RestauranteDocument;

/**
 * Repositório de Restaurante
 *
 * @author Caio
 */
public interface RestauranteDocumentRepository extends MongoRepository<RestauranteDocument, String> {

	/**
	 * Busca Restaurante pelo nome
	 *
	 * @param name nome
	 * @return Restaurante
	 */
	RestauranteDocument findOneByNome(String name);

	/**
	 * Busca lista de Restaurantes pelo nome
	 *
	 * @param name nome
	 * @return Lista de Restaurantes
	 */
	@Query("{ 'nome' : { $regex: '?0', $options: 'i' } }")
	List<RestauranteDocument> findByNomeRegexIgnoreCase(String name);
}
