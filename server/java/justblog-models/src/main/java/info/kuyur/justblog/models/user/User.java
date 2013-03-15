package info.kuyur.justblog.models.user;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class User {

	@XmlElement(name="UserId")
	private Long userId;

	@XmlElement(name="Account")
	private String account;

	@XmlElement(name="Nicename")
	private String nicename;

	@XmlElement(name="Email")
	private String email;

	@XmlElement(name="Url")
	private String url;

	@XmlElement(name="Role")
	private UserRole role;

	public User() {
		super();
	}

	public User(Long userId, String account, String nicename, String email,
			String url, UserRole role) {
		super();
		this.userId = userId;
		this.account = account;
		this.nicename = nicename;
		this.email = email;
		this.url = url;
		this.role = role;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getNicename() {
		return nicename;
	}

	public void setNicename(String nicename) {
		this.nicename = nicename;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", account=" + account
				+ ", nicename=" + nicename + ", email=" + email + ", url="
				+ url + ", role=" + role + "]";
	}
}
