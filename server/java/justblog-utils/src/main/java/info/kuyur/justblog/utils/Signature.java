package info.kuyur.justblog.utils;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
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
import org.apache.commons.lang.StringUtils;
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

		log.debug(sb);

		return sb.toString();
	}

	/**
	 * @param paramMap URL encoded
	 * @param sendingContent URL encoded
	 * @param secretAccessKey
	 * @param algorithm
	 * @param requestMethod
	 * @param uri
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 * @throws URISyntaxException
	 * @throws UnsupportedEncodingException
	 */
	public static String sign(SortedMap<String, String> paramMap, String sendingContent,
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
		return hmacSign(
				createStringToSign(paramMap, sendingContent, requestMethod, domain, path),
				secretAccessKey, algorithm);
	}

	private Signature() {}
}
