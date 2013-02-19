package info.kuyur.justblog.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptUtils {

	private EncryptUtils(){}

	public static byte[] toSHA1(String content) {
		final String algorithm = "SHA";
		try {
			MessageDigest md = MessageDigest.getInstance(algorithm);
			return md.digest(content.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e.getMessage());
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	public static String bytesToHex(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (byte b : bytes) {
			String hex = String.format("%02x", b);
			sb.append(hex);
		}
		return sb.toString();
	}

	public static byte[] hexToBytes(String hex) {
		if (hex == null || (hex.length() & 0x1) != 0) {
			throw new IllegalArgumentException("Invalid hex string.");
		}
		byte[] bytes = new byte[hex.length() / 2];
		for (int i = 0; i < bytes.length; ++i) {
			bytes[i] = (byte)Integer.parseInt(hex.substring(i*2, (i+1)*2), 16);
		}
		return bytes;
	}

	public static byte[] mixHashesWithXOR(byte[] hash1, byte[] hash2) {
		if (hash1 == null || hash2 == null) {
			throw new NullPointerException("Hash keys can not be null.");
		}
		if (hash1.length != hash2.length) {
			throw new IllegalArgumentException("Length of hash keys are not equaled.");
		}
		byte[] bytes = new byte[hash1.length];
		for (int i = 0; i < hash1.length; ++i) {
			bytes[i] = (byte) (hash1[i] ^ hash2[i]);
		}
		return bytes;
	}
}
