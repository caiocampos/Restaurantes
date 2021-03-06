package br.campos.restaurantes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.campos.restaurantes.model.UserDocument;
import br.campos.restaurantes.repository.UserDocumentRepository;

/**
 * Serviço de Login
 *
 * @author Caio
 */
@Service
public class LoginService implements UserDetailsService {

	/**
	 * Repositório de Usuário
	 */
	@Autowired
	private UserDocumentRepository userDocumentRepo;

	/**
	 * Busca o usuário pelo "username"
	 *
	 * @param login
	 */
	@Override
	public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
		UserDocument user = userDocumentRepo.findOneByUsernameIgnoreCase(login);
		return user;
	}
}
