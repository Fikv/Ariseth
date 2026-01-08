package fikv.ariseth.entities;

import static fikv.ariseth.Util.Constants.Messages.*;

import java.util.Date;
import java.util.Objects;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

/**TODO
 * WRTIE CONTROLLER SERVICE FOR CREATING USERS
 * TEST
 * WRITE TEST CONTROLLER, SERVICE
 */


@Table(name = "users")
@Entity
public class User extends BaseEntity {

	@Min(value = 4, message = VALUE_IS_TOO_SHORT)
	@Max(value = 16, message = VALUE_IS_TOO_LONG)
	@NotNull(message = VALUE_CANNOT_BE_NULL)
	@Column(name = "login", length = 16)
	private String login;

	@Min(value = 6, message = VALUE_IS_TOO_SHORT)
	@Max(value = 80, message = VALUE_IS_TOO_LONG)
	@NotNull(message = VALUE_CANNOT_BE_NULL)
	@Column(name = "password", length = 80)
	private String password;

	@Min(value = 8, message = VALUE_IS_TOO_SHORT)
	@Max(value = 90, message = VALUE_IS_TOO_LONG)
	@NotNull(message = VALUE_CANNOT_BE_NULL)
	@Column(name = "email", length = 90)
	private String email;


	@NotNull(message = VALUE_CANNOT_BE_NULL)
	@Column(name = "created_timestamp")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdTimestamp;

	public User(String login, String password, String email, Date createdTimestamp) {
		this.login = login;
		this.password = password;
		this.email = email;
		this.createdTimestamp = createdTimestamp;
	}

	public User() {}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getCreatedTimestamp() {
		return createdTimestamp;
	}

	public void setCreatedTimestamp(Date createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}

	@Override
	public String toString() {
		return "User{"
			   + "login='"
			   + login
			   + '\''
			   + ", password='"
			   + password
			   + '\''
			   + ", email='"
			   + email
			   + '\''
			   + ", createdTimestamp="
			   + createdTimestamp
			   + '}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof User user)) {
			return false;
		}
		return Objects.equals(getLogin(), user.getLogin())
			   && Objects.equals(getPassword(), user.getPassword())
			   && Objects.equals(getEmail(), user.getEmail())
			   && Objects.equals(getCreatedTimestamp(), user.getCreatedTimestamp());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getLogin(), getPassword(), getEmail(), getCreatedTimestamp());
	}
}
