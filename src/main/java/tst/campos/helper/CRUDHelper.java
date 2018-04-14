package tst.campos.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.core.GenericTypeResolver;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import tst.campos.helper.model.CRUDRequest;
import tst.campos.util.Authority;
import tst.campos.util.BadRequestException;
import tst.campos.util.BusinessRuleAdapter;
import tst.campos.util.CRUDType;
import tst.campos.util.MongoDocument;
import tst.campos.util.Searcher;
import tst.campos.util.annotation.DocumentInfo;
import tst.campos.util.annotation.SpecialSearch;

/**
 * Helper de controle de Dados, define métodos de salvar, atualizar, apagar e
 * listar entidades
 *
 * @author Caio
 */
@Component
public class CRUDHelper {

	/**
	 * Fábrica de Beans, usada para buscar os repositórios Mongo existentes
	 */
	private final ListableBeanFactory beanFactory;
	/**
	 * Mapa de Repositórios indexados pela Classe
	 */
	private final Map<Class, MongoRepository> repositoryMap;

	/**
	 * Construtor do Helper, instancia a Fábrica de Beans e o Mapa de
	 * Repositórios
	 *
	 * @param factory Fábrica de Beans necessária na busca por Repositórios
	 */
	public CRUDHelper(ListableBeanFactory factory) {
		this.beanFactory = factory;
		this.repositoryMap = repositoryMap();
	}

	/**
	 * Busca os Repositórios Mongo e instancia um mapa com elas indexadas pela
	 * Classe
	 */
	private Map<Class, MongoRepository> repositoryMap() {
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
	public MongoRepository getRepositoryFor(Class<?> clazz) {
		return repositoryMap.get(clazz);
	}

	/**
	 * Busca uma instância de uma entidade pelo Id
	 *
	 * @param req requisição feita ao CRUDService
	 * @return Instancia da entidade buscada
	 * @throws tst.campos.util.BadRequestException
	 */
	public Object findOne(CRUDRequest req) throws BadRequestException {
		Class<?> clazz = getModelClass(req.entity);
		if (clazz != null && canCRUD(CRUDType.READ, clazz)) {
			MongoRepository repo = getRepositoryFor(clazz);
			BadRequestException.assertNotNull(repo, "Não foi possível carregar o Repositório.");
			try {
				return repo.findById(req.id).get();
			} catch (Exception ex) {
				return null;
			}
		} else {
			return null;
		}
	}

	/**
	 * Busca todos os registros de uma entidade
	 *
	 * @param req requisição feita ao CRUDService
	 * @return Lista de todos os registros da entidade buscada
	 * @throws tst.campos.util.BadRequestException
	 */
	public List<Object> findAll(CRUDRequest req) throws BadRequestException {
		Class<?> clazz = getModelClass(req.entity);
		if (clazz != null && canCRUD(CRUDType.READ, clazz)) {
			MongoRepository repo = getRepositoryFor(clazz);
			BadRequestException.assertNotNull(repo, "Não foi possível carregar o Repositório.");
			try {
				return repo.findAll();
			} catch (Exception ex) {
				return Collections.EMPTY_LIST;
			}
		} else {
			return Collections.EMPTY_LIST;
		}
	}

	/**
	 * Busca através de um critério os registros de uma entidade
	 *
	 * @param req requisição feita ao CRUDService
	 * @return Lista de registros que correspondem à busca na entidade buscada
	 * @throws tst.campos.util.BadRequestException
	 */
	public List<Object> findSpecial(CRUDRequest req) throws BadRequestException {
		Class<?> clazz = getModelClass(req.entity);
		if (clazz != null && canCRUD(CRUDType.READ, clazz)) {
			MongoRepository repo = getRepositoryFor(clazz);
			BadRequestException.assertNotNull(repo, "Não foi possível carregar o Repositório.");
			DocumentInfo info = clazz.getAnnotation(DocumentInfo.class);
			if (info == null || info.specialSearch() == null || info.specialSearch().length == 0) {
				return Collections.EMPTY_LIST;
			}
			SpecialSearch special = info.specialSearch()[0];
			if (special == null || special.searcher() == null) {
				return Collections.EMPTY_LIST;
			}
			try {
				Searcher searcher = this.beanFactory.getBean(special.searcher());
				return searcher.search(req.special, req.param);
			} catch (BeansException ex) {
				return Collections.EMPTY_LIST;
			} catch (Exception ex) {
				return Collections.EMPTY_LIST;
			}
		} else {
			return Collections.EMPTY_LIST;
		}
	}

	/**
	 * Persiste dados para uma entidade específica
	 *
	 * @param req requisição feita ao CRUDService
	 * @return Id do registro salvo
	 * @throws tst.campos.util.BadRequestException
	 */
	public String save(CRUDRequest req) throws BadRequestException {
		Class<?> clazz = getModelClass(req.entity);
		if (clazz == null || req.data == null) {
			throw new BadRequestException("Não foi possível salvar os dados, não foi possível recuperar alguns dados.");
		}
		ObjectMapper mapper = new ObjectMapper();
		MongoDocument data = null;
		try {
			data = (MongoDocument) mapper.readValue(mapper.writeValueAsString(req.data), clazz);
		} catch (IOException ex) {
			throw new BadRequestException("Não foi possível converter os dados enviados.");
		} catch (Exception ex) {
			throw new BadRequestException("Não foi possível converter os dados enviados.");
		}
		CRUDType type = data.getId() == null ? CRUDType.CREATE : CRUDType.UPDATE;
		if (canCRUD(type, clazz)) {
			MongoRepository repo = getRepositoryFor(clazz);
			BadRequestException.assertNotNull(repo, "Não foi possível carregar o Repositório.");
			try {
				DocumentInfo info = clazz.getAnnotation(DocumentInfo.class);
				if (info != null && info.rule().length > 0) {
					Class<? extends BusinessRuleAdapter> ruleClass = info.rule()[0].value();
					BusinessRuleAdapter rule = this.beanFactory.getBean(ruleClass);
					data = rule.onSave(data);
				}
				data = (MongoDocument) repo.save(data);
				return data.getId();
			} catch (BeansException ex) {
				throw new BadRequestException("Não foi possível validar as regras da entidade.");
			} catch (BadRequestException ex) {
				throw ex;
			} catch (Exception ex) {
				throw new BadRequestException("Não foi possível salvar os dados.");
			}
		} else {
			throw new BadRequestException("Não foi possível salvar os dados, ação não é permitida.");
		}
	}

	/**
	 * Apaga um registro pelo Id
	 *
	 * @param req requisição feita ao CRUDService
	 * @return Id do registro apagado
	 * @throws tst.campos.util.BadRequestException
	 */
	public String delete(CRUDRequest req) throws BadRequestException {
		Class<?> clazz = getModelClass(req.entity);
		if (clazz != null && canCRUD(CRUDType.DELETE, clazz)) {
			MongoRepository repo = getRepositoryFor(clazz);
			BadRequestException.assertNotNull(repo, "Não foi possível carregar o Repositório.");
			try {
				MongoDocument data = (MongoDocument) repo.findById(req.id).get();
				DocumentInfo info = clazz.getAnnotation(DocumentInfo.class);
				if (info != null && info.rule().length > 0) {
					Class<? extends BusinessRuleAdapter> ruleClass = info.rule()[0].value();
					BusinessRuleAdapter rule = this.beanFactory.getBean(ruleClass);
					rule.onDelete(data);
				}
				repo.deleteById(data.getId());
			} catch (BeansException ex) {
				throw new BadRequestException("Não foi possível validar as regras da entidade.");
			} catch (BadRequestException ex) {
				throw ex;
			} catch (Exception ex) {
				throw new BadRequestException("Não foi possível apagar os dados.");
			}
			return req.id;
		} else {
			throw new BadRequestException("Não foi possível apagar os dados, ação não é permitida.");
		}
	}

	/**
	 * Verifica se é permitido ao usuário efetuar a operação definida pelo
	 * parâmetro "type"
	 *
	 * @param type Tipo de Operação
	 * @param clazz Classe da Entidade
	 * @return "true" se é possível efetuar a operação, no outro caso "false"
	 */
	public boolean canCRUD(CRUDType type, Class<?> clazz) {
		Object details = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (details instanceof UserDetails) {
			Collection<? extends GrantedAuthority> authorities = ((UserDetails) details).getAuthorities();
			if (authorities == null || authorities.isEmpty()) {
				return false;
			} else if (authorities.contains(Authority.ADMIN)) {
				return true;
			} else {
				if (clazz == null) {
					return false;
				}
				DocumentInfo info = clazz.getAnnotation(DocumentInfo.class);
				if (info == null) {
					return false;
				}
				if (authorities.contains(Authority.USER)) {
					switch (type) {
						case CREATE:
							return info.userAcess().create();
						case READ:
							return info.userAcess().read();
						case UPDATE:
							return info.userAcess().update();
						case DELETE:
							return info.userAcess().delete();
						default:
							return false;
					}
				}
			}
		}
		return false;
	}

	private final HashMap<String, Class<? extends MongoDocument>> modelClassMap = new HashMap();

	/**
	 * Busca a Classe da entidade de nome "name"
	 *
	 * @param name Nome da entidade
	 * @return Classe da entidade
	 */
	public Class<? extends MongoDocument> getModelClass(String name) {
		if (name == null || name.isEmpty()) {
			return null;
		} else if (!name.endsWith("Document")) {
			name = name.concat("Document");
		}
		try {
			Class<? extends MongoDocument> clazz = modelClassMap.get(name);
			if (clazz == null) {
				clazz = (Class<? extends MongoDocument>) Class.forName("tst.campos.model.".concat(name));
				modelClassMap.put(name, clazz);
			}
			return clazz;
		} catch (ClassNotFoundException ex) {
			return null;
		} catch (Exception ex) {
			return null;
		}
	}
}
