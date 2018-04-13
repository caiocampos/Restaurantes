package tst.campos.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import tst.campos.service.LoginService;

/**
 * Componente que configura a segurança de acesso do projeto
 *
 * @author Caio
 */
@Configuration
@EnableWebSecurity
public class LoginSecurityConfigurer extends WebSecurityConfigurerAdapter {

	/**
	 * Instância do codificador e decoficador usado na senha
	 */
	private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
	/**
	 * Serviço de Login, que controla algumas informações de Usuário
	 */
	@Autowired
	private LoginService loginService;

	/**
	 * Cofigura a autenticação
	 *
	 * @throws java.lang.Exception
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(loginService).passwordEncoder(bCryptPasswordEncoder);
	}

	/**
	 * Cofigura os acessos possíveis ao sistema, permite acesso ao login, ao
	 * logout e ao "/public", demeais requisições somente se autenticado
	 *
	 * @throws java.lang.Exception
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeRequests()
				.antMatchers("/public").permitAll()
				.anyRequest().authenticated()
				.and()
				.formLogin()
				.usernameParameter("username").passwordParameter("password")
				.permitAll()
				.and()
				.logout()
				.permitAll();
	}
}
