package br.campos.restaurantes.repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import br.campos.restaurantes.model.UserDocument;

/**
 * Repositório de Usuário
 *
 * @author Caio
 */
public interface UserDocumentRepository extends MongoRepository<UserDocument, String> {

	/**
	 * Busca Usuário pelo "username" ignorando se o texto é Caixa alta ou baixa
	 *
	 * @param username Nome de Usuário
	 * @return Usuário
	 */
	UserDocument findOneByUsernameIgnoreCase(String username);

	/**
	 * Busca Usuário pelo "username"
	 *
	 * @param username Nome de Usuário
	 * @return Usuário
	 */
	UserDocument findOneByUsername(String username);

	/**
	 * Busca lista de Usuários pelo nome
	 *
	 * @param name nome
	 * @return Lista de Usuários
	 */
	@Query("{ 'nome' : { $regex: '?0', $options: 'i' } }")
	List<UserDocument> findByNomeRegexIgnoreCase(String name);
}
