package tst.campos.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.core.GenericTypeResolver;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

/**
 * Serviço de controle de Repositórios
 *
 * @author Caio
 */
@Service
public class RepositoryService {

	/**
	 * Fábrica de Beans, usada para buscar os repositórios Mongo existentes
	 */
	private final ListableBeanFactory beanFactory;

	/**
	 * Mapa de Repositórios indexados pela Classe
	 */
	private final Map<Class, MongoRepository> mongoRepositoryMap;

	/**
	 * Construtor do Helper, instancia a Fábrica de Beans e o Mapa de
	 * Repositórios
	 *
	 * @param factory Fábrica de Beans necessária na busca por Repositórios
	 */
	public RepositoryService(ListableBeanFactory factory) {
		this.beanFactory = factory;
		this.mongoRepositoryMap = mongoRepositoryMap();
	}

	/**
	 * Busca os Repositórios Mongo e instancia um mapa com elas indexadas pela
	 * Classe
	 */
	private Map<Class, MongoRepository> mongoRepositoryMap() {
		Map<Class, MongoRepository> repositories = new HashMap<>();
		Map<String, MongoRepository> repos = this.beanFactory.getBeansOfType(MongoRepository.class);
		repos.entrySet().forEach((entry) -> {
			Class<?>[] argumentClasses = GenericTypeResolver.resolveTypeArguments(entry.getValue().getClass(), MongoRepository.class);
			if (argumentClasses != null && argumentClasses.length > 0) {
				repositories.put(argumentClasses[0], entry.getValue());
			}
		});
		return repositories;
	}

	/**
	 * Busca os Repositórios Mongo e instancia um mapa com elas indexadas pela
	 * Classe
	 *
	 * @param clazz Classe a ser buscada no Mapa de Repositórios
	 * @return Repositório Mongo
	 */
	public MongoRepository<?, ?> getRepositoryFor(Class<?> clazz) {
		return mongoRepositoryMap.get(clazz);
	}

	/**
	 * Lista as classes de Documentos Mongo Existentes no sistema
	 *
	 * @return Classes de Documentos do Mongo
	 */
	public Set<Class> listMongoDocuments() {
		return mongoRepositoryMap.keySet();
	}
}
