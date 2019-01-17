package tst.campos.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tst.campos.service.model.EntityInfoResponse;
import tst.campos.util.MongoDocument;
import tst.campos.util.annotation.DocumentInfo;

/**
 * Serviço de controle de informações de Entidades
 *
 * @author Caio
 */
@Service
public class EntityInfoService {

	/**
	 * Pacote das entidades
	 */
	public static final String MODEL_PACKAGE = "tst.campos.model.";

	/**
	 * Sufixo das entidades
	 */
	public static final String SUFFIX = "Document";

	/**
	 * Mapa de entidades indexadas pelo Nome
	 */
	private final Map<String, Class<? extends MongoDocument>> modelClassMap = new HashMap<>();

	/**
	 * Serviço de controle de Repositórios
	 */
	@Autowired
	private RepositoryService repositoryService;

	/**
	 * Busca a Classe da entidade de nome "name"
	 *
	 * @param name Nome da entidade
	 * @return Classe da entidade
	 */
	@SuppressWarnings("unchecked")
	public Class<? extends MongoDocument> getModelClass(String name) {
		if (name == null || name.isEmpty()) {
			return null;
		} else if (!name.endsWith(SUFFIX)) {
			name = name.concat(SUFFIX);
		}
		try {
			Class<? extends MongoDocument> clazz = modelClassMap.get(name);
			if (clazz == null) {
				clazz = (Class<? extends MongoDocument>) Class.forName(MODEL_PACKAGE.concat(name));
				modelClassMap.put(name, clazz);
			}
			return clazz;
		} catch (ClassNotFoundException ex) {
			return null;
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * Lista as informações de entidades
	 * @return Informações das Entidades do Sistema
	 */
	public List<EntityInfoResponse> listEntityInfo() {
		List<EntityInfoResponse> list = new ArrayList<>();
		repositoryService.listMongoDocuments().stream()
				.map((c) -> getEntityInfo(c))
				.filter((res) -> !(res == null))
				.forEachOrdered((EntityInfoResponse res) -> {
					list.add(res);
				});
		return list;
	}

	/**
	 * Recupera as informações de um entidade pelo nome
	 * @param name nome da Entidade
	 * @return Informações da Entidade
	 */
	public EntityInfoResponse getEntityInfo(String name) {
		return getEntityInfo(getModelClass(name));
	}

	/**
	 * Recupera as informações de um entidade pela classe
	 * @param clazz Classe da Entidade
	 * @return Informações da Entidade
	 */
	public EntityInfoResponse getEntityInfo(Class<?> clazz) {
		if (clazz == null) {
			return null;
		}
		DocumentInfo info = clazz.getAnnotation(DocumentInfo.class);
		if (info == null) {
			return null;
		}
		String entity = clazz.getName().replace(MODEL_PACKAGE, "").replace(SUFFIX, "");
		return new EntityInfoResponse(info, entity);
	}
}
