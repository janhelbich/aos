package cz.hel.aos.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import cz.hel.aos.util.HashProvider;

@Entity
@Table(name = "users")
@NamedQueries({
	@NamedQuery(
			name = "User.findByLogin",
			query = "SELECT u FROM User u WHERE u.userName = :userName"
	)
})
public class User extends AbstractEntity {

	private static final long serialVersionUID = 65669780725020413L;

	@Column(name = "user_name")
	private String userName;

	@Column(name = "password")
	private String password;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
			joinColumns = @JoinColumn(name = "role_id"),
			inverseJoinColumns = @JoinColumn(name = "user_id")
	)
	private List<Role> roles;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = HashProvider.hash(password);
	}

}
