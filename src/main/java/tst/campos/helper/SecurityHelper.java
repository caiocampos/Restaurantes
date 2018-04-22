package tst.campos.helper;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Helper de Segurança
 *
 * @author Caio
 */
@Component
public class SecurityHelper {

	/**
	 * Instância do codificador e decoficador
	 */
	private final BCryptPasswordEncoder cryptPasswordEncoder = new BCryptPasswordEncoder();

	/**
	 * recupera o codificador e decoficador
	 * @return Instância do codificador e decoficador
	 */
	public BCryptPasswordEncoder getCryptPasswordEncoder() {
		return cryptPasswordEncoder;
	}
}
