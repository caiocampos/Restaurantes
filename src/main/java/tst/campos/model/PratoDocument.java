package tst.campos.model;

import java.io.Serializable;
import java.math.BigDecimal;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import tst.campos.helper.search.PratoSearcher;
import tst.campos.util.MongoDocument;
import tst.campos.util.annotation.SpecialSearch;
import tst.campos.util.annotation.UserAcessType;

/**
 * Entidade de Prato
 *
 * @author Caio
 */
@Document(collection = "prato")
@UserAcessType(create = true, read = true, update = true, delete = true)
@SpecialSearch(searcher = PratoSearcher.class, queries = "nomeParcialSemCaixa")
public class PratoDocument implements MongoDocument, Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	private String nome;
	private BigDecimal preco;

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
