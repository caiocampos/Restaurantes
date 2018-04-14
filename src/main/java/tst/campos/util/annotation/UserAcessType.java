package tst.campos.util.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Tipo de acesso que o Usuário (role: "user") pode ter na Entidade. O
 * Administrador (role: "admin"), deve se manter com acesso total
 *
 * @author Caio
 */
@Retention(value = RetentionPolicy.RUNTIME)
public @interface UserAcessType {

	public boolean create() default true;

	public boolean read() default true;

	public boolean update() default true;

	public boolean delete() default true;
}
