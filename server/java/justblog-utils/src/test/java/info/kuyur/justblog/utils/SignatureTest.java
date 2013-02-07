package info.kuyur.justblog.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

public class SignatureTest {

	private static String algorithm = "HmacSHA256";
	private static final Set<String> excludeSignatureParamSet = new HashSet<String>();
	static {
		// these parameters are not included in calculating the signature.
		// because client can't know these parameters before sending a request
		// to a server.
		// these parameters are created by ExtJS automatically.
		excludeSignatureParamSet.add("_dc");
	}

	private static String createStringToSign(
			SortedMap<String, String> paramMap, String sendingContent, String requestMethod,
			String domain, String path) throws UnsupportedEncodingException {
		StringBuilder sb = new StringBuilder();
		sb.append(requestMethod).append("\n").append(domain).append("\n").append(path).append("\n");
		int c = 0;
		for (Entry<String, String> entry : paramMap.entrySet()) {
			if (!excludeSignatureParamSet.contains(entry.getKey())) {
				if (c != 0) {
					sb.append("&");
				}
				sb.append(entry.getKey()).append("=").append(entry.getValue());
				++c;
			}
		}
		if (c > 0) {
			sb.append("\n");
		}
		if (StringUtils.isNotEmpty(sendingContent)) {
			sb.append(sendingContent).append("\n");
		}

		return sb.toString();
	}
	/**
	 * Standard Test
	 */
	@Test
	public void testHMACSign01() {
		String data = "hoge";
		String key = "test";
		String realSign = "KdmcaXAUagfsWo53ka3lF3pvdamAZgb+6js258CpYew=";
		try {
			String calcSign = Signature.hmacSign(data, key, algorithm);
			System.out.println(calcSign);
			assertEquals(calcSign, realSign);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Error happen.");
		}
	}

	/**
	 * Standard Test
	 */
	@Test
	public void testHMACSign02() {
		StringBuilder sb = new StringBuilder();
		sb.append("POST");
		sb.append("\n");
		sb.append("localhost:8080");
		sb.append("\n");
		sb.append("/justblog/rest/login");
		sb.append("\n");
		sb.append("account=admin&");
		sb.append("timestamp=2013-02-07T10%3A17%3A46.453Z");
		sb.append("\n");
		String key = "d033e22ae348aeb5660fc2140aec35850c4da997";
		String realSign = "nIuus0BeA2z5RjenEFWxpjZJSHGShkIonR/P0rz0rBs=";
		try {
			String calcSign = Signature.hmacSign(sb.toString(), key, algorithm);
			System.out.println(sb.toString());
			System.out.println(calcSign);
			assertEquals(calcSign, realSign);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Error happen.");
		}
	}

	/**
	 * Standard Test
	 */
	@Test
	public void testCreateSignatureToSign() {
		StringBuilder sb = new StringBuilder();
		sb.append("POST");
		sb.append("\n");
		sb.append("localhost:8080");
		sb.append("\n");
		sb.append("/justblog/rest/login");
		sb.append("\n");
		sb.append("account=admin&");
		sb.append("timestamp=2013-02-07T10%3A17%3A46.453Z");
		sb.append("\n");

		SortedMap<String, String> parameters = new TreeMap<String, String>();
		parameters.put("account", "admin");
		parameters.put("timestamp", "2013-02-07T10%3A17%3A46.453Z");
		String requestMethod = "POST";
		String domain = "localhost:8080";
		String path = "/justblog/rest/login";
		try {
			String stringToSign = createStringToSign(parameters, null, requestMethod, domain, path);
			System.out.println(sb.toString());
			System.out.println();
			System.out.println(stringToSign);
			assertEquals(stringToSign, sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Error happen.");
		}
	}

	/**
	 * Standard Test
	 */
	@Test
	public void testSign() {
		SortedMap<String, String> parameters = new TreeMap<String, String>();
		parameters.put("account", "admin");
		parameters.put("timestamp", "2013-02-07T10%3A17%3A46.453Z");
		String requestMethod = "POST";
		String domain = "localhost:8080";
		String path = "/justblog/rest/login";
		String secretAccessKey = "d033e22ae348aeb5660fc2140aec35850c4da997";
		String uri = "http://localhost:8080/justblog/rest/login";
		try {
			String stringToSign = createStringToSign(parameters, null, requestMethod, domain, path);
			System.out.println(stringToSign);
			String calcSign = Signature.sign(parameters, null, secretAccessKey, algorithm, requestMethod, uri);
			System.out.println(calcSign);
			String realSign = "nIuus0BeA2z5RjenEFWxpjZJSHGShkIonR/P0rz0rBs=";
			assertEquals(calcSign, realSign);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Error happen.");
		}
	}
}
