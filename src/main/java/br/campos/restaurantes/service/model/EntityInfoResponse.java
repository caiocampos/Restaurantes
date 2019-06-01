package br.campos.restaurantes.service.model;

import java.util.ArrayList;

import br.campos.restaurantes.util.annotation.DocumentInfo;
import br.campos.restaurantes.util.annotation.FKInfo;
import br.campos.restaurantes.util.annotation.FieldInfo;
import br.campos.restaurantes.util.annotation.UserAcessPermissions;

/**
 * Classe com dados da resposta usada no EntityInfoService
 *
 * @author Caio
 */
public class EntityInfoResponse {

	public String entity;
	public String title;
	public String description;
	public EntityInfoAcess userAcess;
	public String[] queries = {};
	public EntityInfoField[] fields = {};

	public EntityInfoResponse(DocumentInfo info, String entity) {
		this.entity = entity;
		title = info.title();
		description = info.descrption();
		userAcess = new EntityInfoAcess(info.userAcess());
		if (info.specialSearch() != null && info.specialSearch().length > 0) {
			queries = info.specialSearch()[0].queries();
		}
		if (info.fields() != null) {
			ArrayList<EntityInfoField> fieldList = new ArrayList<>();
			for (FieldInfo fieldInfo : info.fields()) {
				fieldList.add(new EntityInfoField(fieldInfo));
			}
			fields = fieldList.toArray(fields);
		}
	}

	public static class EntityInfoAcess {

		public boolean create;
		public boolean read;
		public boolean update;
		public boolean delete;

		public EntityInfoAcess(UserAcessPermissions permissions) {
			if (permissions == null) {
				create = read = update = delete = true;
			} else {
				create = permissions.create();
				read = permissions.read();
				update = permissions.update();
				delete = permissions.delete();
			}
		}
	}

	public static class EntityInfoField {

		public String name;
		public String label;
		public String type;
		public String[] options;
		public EntityInfoFK fk;
		public boolean required;
		public int size;

		public EntityInfoField(FieldInfo info) {
			name = info.name();
			label = info.label();
			type = info.type().name();
			options = info.options();
			required = info.required();
			size = info.size();
			if (info.fk() != null && info.fk().length > 0) {
				fk = new EntityInfoFK(info.fk()[0]);
			}
		}
	}

	public static class EntityInfoFK {

		public String entity;
		public String search;
		public String param;

		public EntityInfoFK(FKInfo info) {
			entity = info.entity();
			search = info.search();
			param = info.param();
		}
	}
}
