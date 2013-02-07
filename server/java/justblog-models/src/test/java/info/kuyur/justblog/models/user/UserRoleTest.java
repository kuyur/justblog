package info.kuyur.justblog.models.user;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class UserRoleTest {

	@Test
	public void testContainRole() {
		UserRole founder = UserRole.FOUNDER;
		UserRole admin = UserRole.ADMIN;
		UserRole editor = UserRole.EDITOR;
		UserRole author = UserRole.AUTHOR;
		UserRole reader = UserRole.READER;

		assertTrue(founder.containRole(founder.toString()));
		assertTrue(founder.containRole(admin.toString()));
		assertTrue(founder.containRole(editor.toString()));
		assertTrue(founder.containRole(author.toString()));
		assertTrue(founder.containRole(reader.toString()));

		assertFalse(admin.containRole(founder.toString()));
		assertTrue(admin.containRole(admin.toString()));
		assertTrue(admin.containRole(editor.toString()));
		assertTrue(admin.containRole(author.toString()));
		assertTrue(admin.containRole(reader.toString()));

		assertFalse(editor.containRole(founder.toString()));
		assertFalse(editor.containRole(admin.toString()));
		assertTrue(editor.containRole(editor.toString()));
		assertTrue(editor.containRole(author.toString()));
		assertTrue(editor.containRole(reader.toString()));

		assertFalse(author.containRole(founder.toString()));
		assertFalse(author.containRole(admin.toString()));
		assertFalse(author.containRole(editor.toString()));
		assertTrue(author.containRole(author.toString()));
		assertTrue(author.containRole(reader.toString()));

		assertFalse(reader.containRole(founder.toString()));
		assertFalse(reader.containRole(admin.toString()));
		assertFalse(reader.containRole(editor.toString()));
		assertFalse(reader.containRole(author.toString()));
		assertTrue(reader.containRole(reader.toString()));
	}
}
