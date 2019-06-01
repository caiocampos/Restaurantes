package br.campos.restaurantes.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import br.campos.restaurantes.service.search.PratoSearcher;
import br.campos.restaurantes.util.MongoDocument;
import br.campos.restaurantes.util.annotation.DocumentInfo;
import br.campos.restaurantes.util.annotation.FKInfo;
import br.campos.restaurantes.util.annotation.FieldInfo;
import br.campos.restaurantes.util.annotation.SpecialSearch;
import br.campos.restaurantes.util.annotation.UserAcessPermissions;

/**
 * Entidade de Prato
 *
 * @author Caio
 */
@Document(collection = "prato")
@DocumentInfo(
		title = "Pratos",
		descrption = "Pratos disponíveis",
		userAcess = @UserAcessPermissions(create = true, read = true, update = true, delete = true),
		specialSearch = @SpecialSearch(searcher = PratoSearcher.class, queries = "nomeParcialSemCaixa"),
		fields = {
			@FieldInfo(name = "nome", label = "Nome", required = true)
			, @FieldInfo(name = "preco", label = "Preço", type = FieldInfo.FieldType.VALUE)
			, @FieldInfo(name = "restaurante", label = "Restaurante", type = FieldInfo.FieldType.FOREIGN,
					fk = @FKInfo(entity = "RestauranteDocument", param = "nome", search = "nomeParcialSemCaixa")
			)
		}
)
public class PratoDocument implements MongoDocument, Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	private String nome;
	private BigDecimal preco = BigDecimal.ZERO;

	@DBRef
	private RestauranteDocument restaurante;

	public PratoDocument() {
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	public RestauranteDocument getRestaurante() {
		return restaurante;
	}

	public void setRestaurante(RestauranteDocument restaurante) {
		this.restaurante = restaurante;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public BigDecimal getPreco() {
		return preco;
	}

	public void setPreço(BigDecimal preco) {
		this.preco = preco;
	}
}
