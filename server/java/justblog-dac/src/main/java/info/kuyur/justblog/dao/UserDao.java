package info.kuyur.justblog.dao;

import info.kuyur.justblog.models.user.UserRole;

public class UserDao {

	public static final class Credentials {
		private final String account;
		private final String hashedKey;
		private final UserRole role;

		private Credentials(String account, String hashedKey, UserRole role) {
			this.account = account;
			this.hashedKey = hashedKey;
			this.role = role;
		}

		public String getAccount() {
			return account;
		}

		public String getHashedKey() {
			return hashedKey;
		}

		public UserRole getRole() {
			return role;
		}

		@Override
		public String toString() {
			return "Credentials [account=" + account + ", hashedKey="
					+ hashedKey + ", role=" + role + "]";
		}
	}

	public Credentials getCredentials(String account) {
		// TODO
		if ("admin".equals(account)) {
			return new Credentials("admin", "admin", UserRole.ADMIN);
		}
		if ("reader".equals(account)) {
			return new Credentials("reader", "reader", UserRole.READER);
		}
		return null;
	}
}
