package tst.campos.helper.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Classe com dados da requisições usadas no CRUDService
 *
 * @author Caio
 */
public class CRUDRequest {

	public String entity;
	public String id;
	public String special;
	public Object[] param;
	public Object data;

	@JsonCreator
	public CRUDRequest(
			@JsonProperty("entity") String entity,
			@JsonProperty("data") Object data,
			@JsonProperty("id") String id,
			@JsonProperty("special") String special,
			@JsonProperty("param") Object[] param
	) {
		this.entity = entity;
		this.data = data;
		this.id = id;
		this.special = special;
		this.param = param;
	}
}
