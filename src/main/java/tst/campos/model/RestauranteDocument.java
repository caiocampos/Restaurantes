package tst.campos.model;

import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import tst.campos.helper.rules.RestauranteBusinessRule;
import tst.campos.helper.search.RestauranteSearcher;
import tst.campos.util.MongoDocument;
import tst.campos.util.annotation.BusinessRule;
import tst.campos.util.annotation.DocumentInfo;
import tst.campos.util.annotation.FieldInfo;
import tst.campos.util.annotation.SpecialSearch;
import tst.campos.util.annotation.UserAcessType;

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
		userAcess = @UserAcessType(create = false, read = true, update = false, delete = false),
		specialSearch = @SpecialSearch(searcher = RestauranteSearcher.class, queries = "nomeParcialSemCaixa"),
		fields = {
			@FieldInfo(name = "nome", label = "Nome")
			, @FieldInfo(name = "telefone", label = "Telefone", type = FieldInfo.FieldType.PHONE)
			, @FieldInfo(name = "endereco", label = "Endereco", type = FieldInfo.FieldType.TEXTAREA)
		}
)
public class RestauranteDocument implements MongoDocument, Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private String id;

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
