package info.kuyur.justblog.models.user;

public enum UserRole {

	FOUNDER("founder"),
	ADMIN("admin"),
	EDITOR("editor"),
	AUTHOR("author"),
	READER("reader");

	private String role;

	private UserRole(String role) {
		this.role = role;
	}

	public String getRole() {
		return role;
	}

	@Override
	public String toString() {
		return role;
	}

	public static UserRole toUserRole(String role) {
		try {
			return UserRole.valueOf(role);
		} catch (Exception e) {
			return null;
		}
	}

	public boolean containRole(String role) {
		UserRole otherRole = toUserRole(role);
		return (otherRole == null) ? false : this.ordinal() <= otherRole.ordinal();
	}
}
