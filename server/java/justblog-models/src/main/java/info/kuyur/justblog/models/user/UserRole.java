package info.kuyur.justblog.models.user;

import java.util.HashMap;
import java.util.Map;

public enum UserRole {

	FOUNDER("founder"),
	ADMIN("admin"),
	EDITOR("editor"),
	AUTHOR("author"),
	READER("reader");

	private static final Map<String, UserRole> ROLES = new HashMap<String, UserRole>();
	static {
		ROLES.put(FOUNDER.toString(), FOUNDER);
		ROLES.put(ADMIN.toString(), ADMIN);
		ROLES.put(EDITOR.toString(), EDITOR);
		ROLES.put(AUTHOR.toString(), AUTHOR);
		ROLES.put(READER.toString(), READER);
	}

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
		return (role == null) ? null : ROLES.get(role.toLowerCase());
	}

	public boolean containRole(String role) {
		UserRole otherRole = toUserRole(role);
		return (otherRole == null) ? false : this.ordinal() <= otherRole.ordinal();
	}
}
