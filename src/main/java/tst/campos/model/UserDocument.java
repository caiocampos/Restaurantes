package tst.campos.model;

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
import tst.campos.helper.rules.UserBusinessRule;
import tst.campos.helper.search.UserSearcher;
import tst.campos.util.MongoDocument;
import tst.campos.util.annotation.BusinessRule;
import tst.campos.util.annotation.DocumentInfo;
import tst.campos.util.annotation.FieldInfo;
import tst.campos.util.annotation.SpecialSearch;
import tst.campos.util.annotation.UserAcessType;

/**
 * Entidade de Usuário
 *
 * @author Caio
 */
@Document(collection = "user")
@DocumentInfo(
		title = "Usuários",
		descrption = "Usuários do Sistema",
		rule = @BusinessRule(UserBusinessRule.class),
		userAcess = @UserAcessType(create = false, read = true, update = false, delete = false),
		specialSearch = @SpecialSearch(searcher = UserSearcher.class, queries = "nomeParcialSemCaixa"),
		fields = {
			@FieldInfo(name = "nome", label = "Nome")
			, @FieldInfo(name = "sobrenome", label = "Sobrenome")
			, @FieldInfo(name = "username", label = "Nome de usuário")
			, @FieldInfo(name = "password", label = "Senha", type = FieldInfo.FieldType.PASS)
			, @FieldInfo(name = "roles", label = "Acessos", type = FieldInfo.FieldType.LIST)
			, @FieldInfo(name = "enabled", label = "Ativo", type = FieldInfo.FieldType.TOGGLE)
		}
)
public class UserDocument implements UserDetails, MongoDocument, Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Indexed(unique = true)
	private String username;

	@Transient
	private List<GrantedAuthority> authorities;

	private String[] roles;
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

	public void setRoles(String[] roles) {
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
