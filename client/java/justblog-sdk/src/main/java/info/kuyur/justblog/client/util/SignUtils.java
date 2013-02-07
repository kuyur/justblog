package info.kuyur.justblog.client.util;

import info.kuyur.justblog.utils.Config;
import info.kuyur.justblog.utils.EncryptUtils;
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

import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

public class SignUtils {

	private SignUtils() {}

	public static String getFormattedTimestamp() {
		SimpleDateFormat df = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		df.setTimeZone(TimeZone.getTimeZone("UTC"));
		return df.format(new Date());
	}

	/**
	 * Notice: bodyParams's priority is higher than sendingObject's.
	 * @param queryParams not URL encoded
	 * @param bodyParams not URL encoded
	 * @param sendingObject not URL encoded. It should be a json string or a xml string.
	 * @param hashedKey
	 * @param method
	 * @param uri
	 * @return
	 */
	public static String sign(Collection<NameValuePair> queryParams,
			Collection<NameValuePair> bodyParams, String sendingObject,
			byte[] hashedKey, String method, String uri) {
		SortedMap<String, String> paramMap = new TreeMap<String, String>();
		try {
			if (queryParams != null) {
				for (NameValuePair param : queryParams) {
					paramMap.put(URLEncoder.encode(param.getName(), "UTF-8"),
						URLEncoder.encode(param.getValue(), "UTF-8"));
				}
			}
			String key = EncryptUtils.bytesToString(hashedKey);
			String sendingContent = null;
			if (bodyParams != null) {
				sendingContent = URLEncodedUtils.format(bodyParams, Consts.UTF_8);
			} else if (sendingObject != null) {
				sendingContent = URLEncoder.encode(sendingObject, "UTF-8");
			}
			return Signature.sign(paramMap, sendingContent, key, Config.ALGORITHM, method, uri);
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
