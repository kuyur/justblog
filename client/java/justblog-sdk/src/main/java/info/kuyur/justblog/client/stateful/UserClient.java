package info.kuyur.justblog.client.stateful;

import info.kuyur.justblog.client.annotation.Deleter;
import info.kuyur.justblog.client.annotation.Creater;
import info.kuyur.justblog.client.stateful.BaseClient;
import info.kuyur.justblog.client.stateful.ISessionClient;
import info.kuyur.justblog.client.stateful.ProxyClient;
import info.kuyur.justblog.models.user.User;
import info.kuyur.justblog.models.user.UserRole;

import java.lang.reflect.Type;
import java.text.MessageFormat;
import java.util.List;

import org.codehaus.jackson.type.TypeReference;

public class UserClient extends ProxyClient{

	/** API list*/
	private static final String USER_LIST = "/rest/user";
	private static final String USER_ADD = "/rest/user";
	private static final String USER_DELETE = "/rest/user/{0}";
	private static final String USER_UPDATE = "/rest/user/{0}";
	private static final String USER_UPDATE_PASSWORD = "/rest/user/{0}/password";
	private static final String USER_CHANGE_ROLE = "/rest/user/{0}/role/{1}";

	public UserClient(String hostname, int port, boolean isSecure, String basePath,
			String account, String password) {
		super(new BaseClient(hostname, port, isSecure, basePath, account, password));
	}

	public UserClient(ISessionClient baseClient) {
		super(baseClient);
	}

	public List<User> getAllUsers() {
		Type type = new TypeReference<List<User>>(){}.getType();
		return super.get(type, USER_LIST, null);
	}

	@Creater
	public User addUser(User user) {
		return super.post(User.class, USER_ADD, null, null, user);
	}

	@Deleter
	public void deleteUser(Long userId) {
		String path = MessageFormat.format(USER_DELETE, String.valueOf(userId));
		super.delete(Void.class, path, null);
	}

	public User updateUser(User user) {
		String path = MessageFormat.format(USER_UPDATE, String.valueOf(user.getUserId()));
		return super.put(User.class, path, null, user);
	}

	public void updatePassword(Long userId, String newPassword) {
		String path = MessageFormat.format(USER_UPDATE_PASSWORD, String.valueOf(userId));
		// TODO
		super.put(Void.class, path, null, null);
	}

	public void changeRole(Long userId, UserRole role) {
		String path = MessageFormat.format(USER_CHANGE_ROLE, String.valueOf(userId), role.toString());
		super.put(Void.class, path, null, null);
	}
}
