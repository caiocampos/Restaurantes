package tst.campos.helper;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class SecurityHelper {

	/**
	 * Instância do codificador e decoficador
	 */
	private final BCryptPasswordEncoder cryptPasswordEncoder = new BCryptPasswordEncoder();

	public BCryptPasswordEncoder getCryptPasswordEncoder() {
		return cryptPasswordEncoder;
	}
}
