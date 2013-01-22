package info.kuyur.justblog.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Signature {

	private static final Log log = LogFactory.getLog(Signature.class);

	public static final String DEFAULT_ENCODING = "UTF-8";
	private static final Charset default_Charset = Charset.forName(DEFAULT_ENCODING);

	private static final Set<String> excludeSignatureParamSet = new HashSet<String>();
	static {
		// these parameters are not included in calculating the signature.
		// because client can't know these parameters before sending a request
		// to a server.
		// these parameters are created by ExtJS automatically.
		excludeSignatureParamSet.add("_dc");
	}

	public static String hmacSign(String data, String key, String algorithm)
			throws NoSuchAlgorithmException, InvalidKeyException {
		Mac mac = Mac.getInstance(algorithm);
		mac.init(new SecretKeySpec(key.getBytes(default_Charset), algorithm));
		byte[] signatureBytes = Base64.encodeBase64(mac.doFinal(data.getBytes(default_Charset)));
		return new String(signatureBytes, default_Charset);
	}

	private static String createSignatureToSign(
			SortedMap<String, String> paramMap, String requestMethod,
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

		log.debug(sb);

		return sb.toString();
	}

	public static String sign(SortedMap<String, String> paramMap,
			String secretAccessKey, String algorithm, String requestMethod,
			String uri) throws NoSuchAlgorithmException, InvalidKeyException,
			URISyntaxException, UnsupportedEncodingException {
		URI u = new URI(uri);
		String domain = u.getHost();
		int port = u.getPort();
		if (port >= 0) {
			domain = domain + ":" + String.valueOf(port);
		}
		String path = u.getPath();
		return hmacSign(createSignatureToSign(paramMap, requestMethod, domain, path),
				secretAccessKey, algorithm);
	}

	public static String urlEncode(String value, boolean path) {
		try {
			String encoded = URLEncoder.encode(value, DEFAULT_ENCODING)
					.replace("+", "%20").replace("*", "%2A").replace("%7E", "~");
			if (path) {
				encoded = encoded.replace("%2F", "/");
			}

			return encoded;
		} catch (UnsupportedEncodingException ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * returns String from InputStream InputStream is read as UTF-8
	 *
	 * @param input
	 * @return
	 */
	public static String readInputStreamAsString(InputStream input) {
		StringBuilder sb = new StringBuilder();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(input, "UTF-8"));
			char[] buf = new char[1024];
			int numRead = 0;
			while ((numRead = reader.read(buf)) != -1) {
				sb.append(buf, 0, numRead);
			}
		} catch (IOException e) {
			log.error("Error while reading file", e);
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				log.error("Error while closing stream", e);
			}
		}
		return sb.toString();
	}

	/**
	 * returns InputStream from this order below.
	 * <ol>
	 * <li>classpath</li>
	 * <li>absolutefilepath</li>
	 * <li>url</li>
	 * </ol>
	 *
	 * @param resource
	 * @return
	 */
	public static InputStream getInputStream(String resource) {
		InputStream stream = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(resource);

		if (stream == null) {
			File file = new File(resource);
			try {
				return new FileInputStream(file);
			} catch (Exception ex) {
				log.info("No File found : " + resource);
			}
			try {
				URL sourceUrl = new URL(resource);
				if (sourceUrl != null) {
					stream = sourceUrl.openStream();
				}
			} catch (Exception e) {
				log.info("No URL found : " + resource);
				return null;
			}
		}
		return stream;
	}

	/**
	 * search the resource from the path specified in that order below and
	 * returns it as String. if nothing found, returns null.
	 * <ol>
	 * <li>classpath</li>
	 * <li>absolutefilepath</li>
	 * <li>url</li>
	 * </ol>
	 *
	 * @param path
	 * @return
	 */
	public static String readStringFromPath(String path) {
		InputStream is = getInputStream(path);
		if (is != null) {
			return readInputStreamAsString(is);
		}

		return null;
	}

	/**
	 * returns hashed password from userName, clearPassword
	 *
	 * @param userName
	 * @param clearPassword
	 * @return
	 */
	public static String hashPassword(String userName, String clearPassword) {
		String salt = DigestUtils.sha256Hex(userName);
		String saltAddedPassword = salt + clearPassword;
		String hashedPassword = DigestUtils.sha512Hex(saltAddedPassword);
		return hashedPassword;
	}

	private Signature() {}
}
