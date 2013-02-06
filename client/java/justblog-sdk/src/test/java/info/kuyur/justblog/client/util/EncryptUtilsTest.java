package info.kuyur.justblog.client.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class EncryptUtilsTest {

	@Test
	public void testToSHA1_1() {
		String password = "admin";
		byte[] hash = EncryptUtils.toSHA1(password);
		assertEquals(EncryptUtils.bytesToString(hash), "d033e22ae348aeb5660fc2140aec35850c4da997");
	}

	@Test
	public void testToSHA1_2() {
		String password = "管理员";
		byte[] hash = EncryptUtils.toSHA1(password);
		assertEquals(EncryptUtils.bytesToString(hash), "ef84e765e027ce1984c2be0aabaea66afb9e66eb");
	}
}
