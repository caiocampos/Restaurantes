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

@Service
public class EntityInfoService {

	private final Map<String, Class<? extends MongoDocument>> modelClassMap = new HashMap<>();

	@Autowired
	private RepositoryService repositoryService;

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

	public EntityInfoResponse getEntityInfo(String name) {
		return getEntityInfo(getModelClass(name));
	}

	public EntityInfoResponse getEntityInfo(Class<?> clazz) {
		if (clazz == null) {
			return null;
		}

		DocumentInfo info = clazz.getAnnotation(DocumentInfo.class);
		if (info == null) {
			return null;
		}

		return new EntityInfoResponse(info);
	}
}
