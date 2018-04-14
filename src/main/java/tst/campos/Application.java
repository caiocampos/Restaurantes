package tst.campos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

/**
 * Classe principal da aplicação
 *
 * @author Caio
 */
@EnableWebSecurity
@SpringBootApplication
@EnableMongoRepositories(basePackages = "tst.campos.repository")
public class Application {
 
	/**
	 * Inicia a aplicação
	 *
	 * @param args Argumentos passados na inicialização
	 */
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
