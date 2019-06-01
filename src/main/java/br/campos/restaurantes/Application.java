package br.campos.restaurantes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Classe principal da aplicação
 *
 * @author Caio
 */
@Controller
@EnableWebSecurity
@SpringBootApplication
@EnableMongoRepositories(basePackages = "br.campos.restaurantes.repository")
public class Application {

	/**
	 * Inicia a aplicação
	 *
	 * @param args Argumentos passados na inicialização
	 */
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	/**
	 * Redirecionador
	 * @return apontamento para o login
	 */
	@GetMapping(value = "/{path:[^\\.]*}")
	public String redirect() {
		return "forward:/login";
	}
}
