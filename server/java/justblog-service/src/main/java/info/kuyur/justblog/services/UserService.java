package info.kuyur.justblog.services;

import info.kuyur.justblog.dao.UserDao;
import info.kuyur.justblog.dao.UserDao.Credentials;
import info.kuyur.justblog.models.user.User;
import info.kuyur.justblog.utils.EncryptUtils;

import java.util.Collections;
import java.util.List;

public class UserService {

	public Credentials getCredentials(String account) {
		UserDao dao = new UserDao();
		return dao.getCredentials(account);
	}

	public List<User> getAllUsers() {
		// TODO
		return Collections.emptyList();
	}

	public boolean deleteUser(Long userId) {
		// TODO
		return false;
	}

	public boolean updatePassword(String account, String encryptedMixedHash) {
		// TODO
		UserDao dao = new UserDao();
		Credentials credentials = dao.getCredentials(account);
		byte[] oldHash = EncryptUtils.hexToBytes(credentials.getHashedKey());
		byte[] mixedHash = EncryptUtils.hexToBytes(encryptedMixedHash);
		byte[] newHash = EncryptUtils.mixHashesWithXOR(oldHash, mixedHash);
		dao.updateHashedKey(account, EncryptUtils.bytesToHex(newHash));

		return false;
	}
}
