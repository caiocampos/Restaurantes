package tst.campos.service.rules;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tst.campos.model.PratoDocument;
import tst.campos.model.RestauranteDocument;
import tst.campos.repository.PratoDocumentRepository;
import tst.campos.repository.RestauranteDocumentRepository;
import tst.campos.util.BadRequestException;
import tst.campos.util.BusinessRuleAdapter;

/**
 * Classe que trata as regras para Restaurantes
 *
 * @author Caio
 */
@Component
public class RestauranteBusinessRule extends BusinessRuleAdapter<RestauranteDocument> {

	/**
	 * Repositório de Prato
	 */
	@Autowired
	private PratoDocumentRepository pratoDocumentRepo;

	/**
	 * Repositório de Restaurante
	 */
	@Autowired
	private RestauranteDocumentRepository restauranteDocumentRepo;

	/**
	 * Tratativa executada antes de salvar um Restaurante
	 *
	 * @param doc Restaurante a salvar
	 * @return resultado depois da tratativa
	 * @throws tst.campos.util.BadRequestException
	 */
	@Override
	public RestauranteDocument onSave(RestauranteDocument doc) throws BadRequestException {
		if (doc.getNome() == null || doc.getNome().isEmpty()) {
			throw new BadRequestException("O nome não pode estar vazio.");
		} else if (doc.getId() == null && restauranteDocumentRepo.findOneByNome(doc.getNome()) != null) {
			throw new BadRequestException("Restaurante já existe.");
		} else if (doc.getTelefone() != null) {
			doc.setTelefone(doc.getTelefone().replaceAll("[^\\d-]", ""));
		}
		return doc;
	}

	/**
	 * Tratativa executada antes de apagar um Restaurante
	 *
	 * @param doc Restaurante a ser apagado
	 * @throws tst.campos.util.BadRequestException
	 */
	@Override
	public void onDelete(RestauranteDocument doc) throws BadRequestException {
		List<PratoDocument> pratos = pratoDocumentRepo.findByRestaurante(doc);
		pratoDocumentRepo.deleteAll(pratos);
	}
}
