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
public @interface UserAcessPermissions {

	boolean create() default true;

	boolean read() default true;

	boolean update() default true;

	boolean delete() default true;
}
