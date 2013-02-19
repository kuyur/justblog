package info.kuyur.justblog.client.stateless;

import info.kuyur.justblog.client.annotation.Deleter;
import info.kuyur.justblog.client.annotation.Creater;
import info.kuyur.justblog.models.user.User;
import info.kuyur.justblog.models.user.UserRole;
import info.kuyur.justblog.utils.EncryptUtils;

import java.lang.reflect.Type;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.codehaus.jackson.type.TypeReference;

public class UserClient extends HmacClient{

	/** API list*/
	private static final String USER_LIST = "/rest/user";
	private static final String USER_ADD = "/rest/user";
	private static final String USER_UPDATE_SELF = "/rest/user";
	private static final String USER_UPDATE = "/rest/user/{0}";
	private static final String USER_DELETE = "/rest/user/{0}";
	private static final String USER_CHANGE_ROLE = "/rest/user/{0}/role/{1}";
	private static final String USER_UPDATE_PASSWORD = "/rest/user/password";

	public UserClient(String hostname, int port, boolean isSecure, String basePath,
			String account, String password) {
		super(hostname, port, isSecure, basePath, account, password);
	}

	public List<User> getAllUsers() {
		Type type = new TypeReference<List<User>>(){}.getType();
		return super.get(type, USER_LIST, null);
	}

	@Creater
	public User addUser(User user) {
		return super.post(User.class, USER_ADD, null, null, user);
	}

	public User updateSelf(User user) {
		User updated = super.put(User.class, USER_UPDATE_SELF, null, user);
		super.updateAccount(updated.getAccount());
		return updated;
	}

	public User updateUser(User user) {
		String path = MessageFormat.format(USER_UPDATE, String.valueOf(user.getUserId()));
		return super.put(User.class, path, null, user);
	}

	@Deleter
	public boolean deleteUser(Long userId) {
		String path = MessageFormat.format(USER_DELETE, String.valueOf(userId));
		return super.delete(boolean.class, path, null);
	}

	public boolean changeRole(Long userId, UserRole role) {
		String path = MessageFormat.format(USER_CHANGE_ROLE, String.valueOf(userId), role.toString());
		return super.put(boolean.class, path, null, null);
	}

	public boolean updatePassword(String newPassword) {
		byte[] oldHash = super.getHashedKey();
		byte[] newHash = EncryptUtils.toSHA1(newPassword);
		String mixedHashString = EncryptUtils.bytesToHex(EncryptUtils.mixHashesWithXOR(oldHash, newHash));
		Collection<NameValuePair> queryParams = new ArrayList<NameValuePair>();
		queryParams.add(new BasicNameValuePair("hash", mixedHashString));
		if (super.put(boolean.class, USER_UPDATE_PASSWORD, queryParams, null)) {
			super.updateHashedKey(newHash);
			return true;
		} else {
			return false;
		}
	}
}
