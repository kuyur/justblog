package info.kuyur.justblog.utils;

import static org.junit.Assert.assertEquals;
import info.kuyur.justblog.utils.EncryptUtils;

import org.junit.Test;

public class EncryptUtilsTest {

	@Test
	public void testToSHA1_1() {
		String password = "admin";
		byte[] hash = EncryptUtils.toSHA1(password);
		assertEquals(EncryptUtils.bytesToHex(hash), "d033e22ae348aeb5660fc2140aec35850c4da997");
	}

	@Test
	public void testToSHA1_2() {
		String password = "管理员";
		byte[] hash = EncryptUtils.toSHA1(password);
		assertEquals(EncryptUtils.bytesToHex(hash), "ef84e765e027ce1984c2be0aabaea66afb9e66eb");
	}

	@Test
	public void testBytesToHexToBytes() {
		byte[] hash = EncryptUtils.toSHA1("admin");
		String hex = EncryptUtils.bytesToHex(hash);
		byte[] hash2 = EncryptUtils.hexToBytes(hex);
		assertEquals(hash.length, hash2.length);
		for (int i=0; i<hash.length; ++i) {
			assertEquals(hash[i], hash2[i]);
		}
	}

	@Test
	public void testHexToBytesToHex() {
		String hex = "ef84e765e027ce1984c2be0aabaea66afb9e66eb";
		byte[] bytes = EncryptUtils.hexToBytes(hex);
		String hex2 = EncryptUtils.bytesToHex(bytes);
		assertEquals(hex, hex2);
	}

	@Test
	public void testMixHashes1() {
		// d033e22ae348aeb5660fc2140aec35850c4da997
		byte[] oldhash = EncryptUtils.toSHA1("admin");
		// 68eeb90e4bec1100ca7de94f07177f9f8dea7eaa
		byte[] newhash = EncryptUtils.toSHA1("newadmin");
		// b8dd5b24a8a4bfb5ac722b5b0dfb4a1a81a7d73d
		byte[] mixedhash1 = EncryptUtils.mixHashesWithXOR(oldhash, newhash);
		byte[] mixedhash2 = EncryptUtils.mixHashesWithXOR(newhash, oldhash);
		assertEquals(mixedhash1.length, mixedhash2.length);
		for (int i=0; i<mixedhash1.length; ++i) {
			assertEquals(mixedhash1[i], mixedhash2[i]);
		}
		System.out.println("old hash  : " + EncryptUtils.bytesToHex(oldhash));
		System.out.println("new hash  : " + EncryptUtils.bytesToHex(newhash));
		System.out.println("mixed hash: " + EncryptUtils.bytesToHex(mixedhash1));
	}

	@Test
	public void testMixHashes2() {
		String oldHash = "d033e22ae348aeb5660fc2140aec35850c4da997";
		String newHash = "68eeb90e4bec1100ca7de94f07177f9f8dea7eaa";
		String mixedHash = "b8dd5b24a8a4bfb5ac722b5b0dfb4a1a81a7d73d";

		byte[] mixedHashBytes = EncryptUtils.hexToBytes(mixedHash);
		byte[] oldHashBytes = EncryptUtils.hexToBytes(oldHash);

		byte[] newHashBytes = EncryptUtils.mixHashesWithXOR(mixedHashBytes, oldHashBytes);
		String newHash2 = EncryptUtils.bytesToHex(newHashBytes);

		assertEquals(newHash, newHash2);
	}
}
