package tst.campos.util;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * Define as autoridades básicas usadas pelo sistema
 *
 * @author Caio
 */
public abstract class Authority {

	public static final GrantedAuthority ADMIN = new SimpleGrantedAuthority("admin");
	public static final GrantedAuthority USER = new SimpleGrantedAuthority("user");
}
