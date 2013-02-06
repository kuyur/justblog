package info.kuyur.justblog.client.util;

import info.kuyur.justblog.utils.Signature;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.SortedMap;
import java.util.TimeZone;
import java.util.TreeMap;

import org.apache.http.NameValuePair;

public class SignUtils {

	public static final String ALGORITHM = "HmacSHA256";
	private SignUtils() {}

	public static String getFormattedTimestamp() {
		SimpleDateFormat df = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		df.setTimeZone(TimeZone.getTimeZone("UTC"));
		return df.format(new Date());
	}

	public static String sign(Collection<NameValuePair> notSortedParams, byte[] hashedKey,
			String method, String uri) {
		SortedMap<String, String> paramMap = new TreeMap<String, String>();
		try {
			for (NameValuePair param : notSortedParams) {
				paramMap.put(param.getName(),
					URLEncoder.encode(param.getValue(), Signature.DEFAULT_ENCODING));
			}
			String key = EncryptUtils.bytesToString(hashedKey);
			return Signature.sign(paramMap, key, ALGORITHM, method, uri);
		} catch (InvalidKeyException e) {
			throw new RuntimeException(e);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
}
