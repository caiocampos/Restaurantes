package br.campos.restaurantes.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import br.campos.restaurantes.helper.SecurityHelper;
import br.campos.restaurantes.service.LoginService;

/**
 * Componente que configura a segurança de acesso do projeto
 *
 * @author Caio
 */
@Configuration
@EnableWebSecurity
public class LoginSecurityConfigurer extends WebSecurityConfigurerAdapter {

	/**
	 * Serviço de Login, que controla algumas informações de Usuário
	 */
	@Autowired
	private LoginService loginService;

	/**
	 * Helper de segurança que possui o encoder
	 */
	@Autowired
	private SecurityHelper securityHelper;

	/**
	 * Cofigura a autenticação
	 *
	 * @throws java.lang.Exception
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(loginService).passwordEncoder(securityHelper.getCryptPasswordEncoder());
	}

	/**
	 * Cofigura os acessos possíveis ao sistema, permite acesso ao login, ao
	 * logout e ao "/public", demeais requisições somente se autenticado
	 *
	 * @throws java.lang.Exception
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().cors()
				.and()
				.authorizeRequests()
				.antMatchers("/login", "/entity/list").permitAll()
				.anyRequest().authenticated()
				.and()
				.formLogin().successForwardUrl("/user")
				.usernameParameter("username").passwordParameter("password")
				.permitAll()
				.and()
				.logout()
        		.invalidateHttpSession(true)
        		.deleteCookies()
				.permitAll();
	}
}
