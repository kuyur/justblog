package info.kuyur.justblog.models.user;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class User {

	@XmlElement(name="Account")
	private String account;

	@XmlElement(name="UserId")
	private Long userId;

	public User() {
		super();
	}

	public User(String account, long userId) {
		super();
		this.account = account;
		this.userId = userId;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "User [account=" + account + ", userId=" + userId + "]";
	}
}
