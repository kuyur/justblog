package info.kuyur.justblog.services;

import info.kuyur.justblog.dao.UserDao;
import info.kuyur.justblog.dao.UserDao.Credentials;
import info.kuyur.justblog.models.user.User;

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
}
