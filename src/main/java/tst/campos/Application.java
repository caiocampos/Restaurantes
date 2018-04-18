package tst.campos;

import java.security.Principal;
import java.util.Collection;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Classe principal da aplicação
 *
 * @author Caio
 */
@Controller
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

	@GetMapping(value = "/{path:[^\\.]*}")
	public String redirect() {
		return "forward:/";
	}

	@RequestMapping("/user")
	@ResponseBody
	public Object user() {
		return SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
}
