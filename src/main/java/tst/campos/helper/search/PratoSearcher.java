package tst.campos.helper.search;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tst.campos.model.PratoDocument;
import tst.campos.repository.PratoDocumentRepository;
import tst.campos.util.Searcher;

/**
 * Buscador especial para Pratos
 *
 * @author Caio
 */
@Component
public class PratoSearcher implements Searcher<PratoDocument> {

	/**
	 * Repositório de Prato
	 */
	@Autowired
	private PratoDocumentRepository pratoDocumentRepo;

	/**
	 * Busca uma lista de Pratos pelo critério e parâmetros passados
	 */
	@Override
	public List<PratoDocument> search(String name, Object... param) {
		if (name == null) {
			return null;
		} else if (name.equals("nomeParcialSemCaixa") && param != null && param.length > 0) {
			return nomeParcialSemCaixa(String.valueOf(param[0]));
		}
		return null;
	}

	/**
	 * Busca um Prato pelo critério e parâmetros passados
	 */
	@Override
	public PratoDocument searchOne(String name, Object... param) {
		return null;
	}

	/**
	 * Busca uma lista de Pratos pelo nome
	 * @param param Nome
	 * @return Pratos encontrados
	 */
	public List<PratoDocument> nomeParcialSemCaixa(String param) {
		return pratoDocumentRepo.findByNomeRegexIgnoreCase(param);
	}
}
