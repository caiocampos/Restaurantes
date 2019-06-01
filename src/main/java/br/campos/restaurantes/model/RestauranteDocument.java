package br.campos.restaurantes.model;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import br.campos.restaurantes.service.rules.RestauranteBusinessRule;
import br.campos.restaurantes.service.search.RestauranteSearcher;
import br.campos.restaurantes.util.MongoDocument;
import br.campos.restaurantes.util.annotation.BusinessRule;
import br.campos.restaurantes.util.annotation.DocumentInfo;
import br.campos.restaurantes.util.annotation.FieldInfo;
import br.campos.restaurantes.util.annotation.SpecialSearch;
import br.campos.restaurantes.util.annotation.UserAcessPermissions;

/**
 * Entidade de Restaurante
 *
 * @author Caio
 */
@Document(collection = "restaurante")
@DocumentInfo(
		title = "Restaurantes",
		descrption = "Restaurantes disponíveis",
		rule = @BusinessRule(RestauranteBusinessRule.class),
		userAcess = @UserAcessPermissions(create = false, read = true, update = false, delete = false),
		specialSearch = @SpecialSearch(searcher = RestauranteSearcher.class, queries = "nomeParcialSemCaixa"),
		fields = {
			@FieldInfo(name = "nome", label = "Nome", required = true)
			, @FieldInfo(name = "telefone", label = "Telefone", type = FieldInfo.FieldType.PHONE)
			, @FieldInfo(name = "endereco", label = "Endereço", type = FieldInfo.FieldType.TEXTAREA)
		}
)
public class RestauranteDocument implements MongoDocument, Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Indexed(unique = true)
	private String nome;

	private String telefone;
	private String endereco;

	public RestauranteDocument() {
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}
}
