package br.campos.restaurantes.service.search;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.campos.restaurantes.model.UserDocument;
import br.campos.restaurantes.repository.UserDocumentRepository;
import br.campos.restaurantes.util.Searcher;

/**
 * Buscador especial para Usuários
 *
 * @author Caio
 */
@Component
public class UserSearcher implements Searcher<UserDocument> {

	/**
	 * Repositório de Usuário
	 */
	@Autowired
	private UserDocumentRepository userDocumentRepo;

	/**
	 * Busca uma lista de Usuários pelo critério e parâmetros passados
	 */
	@Override
	public List<UserDocument> search(String name, Object... param) {
		if (name == null) {
			return null;
		} else if (name.equals("nomeParcialSemCaixa") && param != null && param.length > 0) {
			return nomeParcialSemCaixa(String.valueOf(param[0]));
		}
		return null;
	}

	/**
	 * Busca um Usuário pelo critério e parâmetros passados
	 */
	@Override
	public UserDocument searchOne(String name, Object... param) {
		return null;
	}

	/**
	 * Busca uma lista de usuários pelo nome
	 *
	 * @param param Nome
	 * @return Usuários encontrados
	 */
	public List<UserDocument> nomeParcialSemCaixa(String param) {
		return userDocumentRepo.findByNomeRegexIgnoreCase(param);
	}
}
