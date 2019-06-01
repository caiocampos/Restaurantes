package br.campos.restaurantes.service.rules;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.campos.restaurantes.helper.SecurityHelper;
import br.campos.restaurantes.model.UserDocument;
import br.campos.restaurantes.repository.UserDocumentRepository;
import br.campos.restaurantes.util.BadRequestException;
import br.campos.restaurantes.util.BusinessRuleAdapter;

/**
 * Classe que trata as regras para Usuários
 *
 * @author Caio
 */
@Component
public class UserBusinessRule extends BusinessRuleAdapter<UserDocument> {

	/**
	 * Repositório de Usuário
	 */
	@Autowired
	private UserDocumentRepository userDocumentRepo;

	@Autowired
	private SecurityHelper securityHelper;

	/**
	 * Tratativa executada antes de salvar um Usuário
	 *
	 * @param doc Usuário a salvar
	 * @return resultado depois da tratativa
	 * @throws br.campos.restaurantes.util.BadRequestException
	 */
	@Override
	public UserDocument onSave(UserDocument doc) throws BadRequestException {
		if (doc.getUsername() == null || doc.getUsername().length() < 4) {
			throw new BadRequestException("Nome de usuário inválido.");
		} else if (doc.getPassword() == null || doc.getPassword().length() < 4) {
			throw new BadRequestException("Senha inválida.");
		}
		if (doc.getId() == null) {
			UserDocument findLogin = userDocumentRepo.findOneByUsernameIgnoreCase(doc.getUsername());
			if (findLogin != null) {
				throw new BadRequestException("Usuário já existe.");
			}
			doc.setPassword(securityHelper.getCryptPasswordEncoder().encode(doc.getPassword()));
		} else {
			try {
				String pass = userDocumentRepo.findById(doc.getId()).get().getPassword();
				if (!doc.getPassword().equals(pass)) {
					doc.setPassword(securityHelper.getCryptPasswordEncoder().encode(pass));
				}
			} catch (Exception ex) {
				throw new BadRequestException("Não foi possível validar a Senha.");
			}
		}
		return doc;
	}
}
