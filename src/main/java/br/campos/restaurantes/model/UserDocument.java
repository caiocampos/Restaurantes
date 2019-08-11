package br.campos.restaurantes.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.campos.restaurantes.service.rules.UserBusinessRule;
import br.campos.restaurantes.service.search.UserSearcher;
import br.campos.restaurantes.util.MongoDocument;
import br.campos.restaurantes.util.annotation.BusinessRule;
import br.campos.restaurantes.util.annotation.DocumentInfo;
import br.campos.restaurantes.util.annotation.FieldInfo;
import br.campos.restaurantes.util.annotation.SpecialSearch;
import br.campos.restaurantes.util.annotation.UserAcessPermissions;

/**
 * Entidade de Usuário
 *
 * @author Caio
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = "user")
@DocumentInfo(
		title = "Usuários",
		descrption = "Usuários do Sistema",
		rule = @BusinessRule(UserBusinessRule.class),
		userAcess = @UserAcessPermissions(create = false, read = true, update = false, delete = false),
		specialSearch = @SpecialSearch(searcher = UserSearcher.class, queries = "nomeParcialSemCaixa"),
		fields = {
			@FieldInfo(name = "nome", label = "Nome", required = true)
			, @FieldInfo(name = "sobrenome", label = "Sobrenome")
			, @FieldInfo(name = "username", label = "Nome de usuário", required = true)
			, @FieldInfo(name = "password", label = "Senha", type = FieldInfo.FieldType.PASS, required = true)
			, @FieldInfo(name = "roles", label = "Acessos", type = FieldInfo.FieldType.LIST, options = {"user", "admin"})
			, @FieldInfo(name = "enabled", label = "Ativo", type = FieldInfo.FieldType.TOGGLE, size = 1)
		}
)
public class UserDocument implements UserDetails, MongoDocument, Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Indexed(unique = true)
	private String username;

	@Transient
	@JsonIgnore
	private List<GrantedAuthority> authorities;

	private String[] roles = {};
	private String password;
	private String nome;
	private String sobrenome;
	private Boolean enabled = true;

	public UserDocument() {
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		if (authorities == null) {
			authorities = AuthorityUtils.createAuthorityList(roles);
		}
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	public void setAuthorities(List<GrantedAuthority> authorities) {
		this.authorities = authorities;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	public String[] getRoles() {
		return roles;
	}

	public void setRoles(String... roles) {
		this.roles = roles;
		this.authorities = AuthorityUtils.createAuthorityList(roles);
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSobrenome() {
		return sobrenome;
	}

	public void setSobrenome(String sobrenome) {
		this.sobrenome = sobrenome;
	}
}
