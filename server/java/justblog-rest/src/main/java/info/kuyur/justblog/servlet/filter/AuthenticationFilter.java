package info.kuyur.justblog.servlet.filter;

import info.kuyur.justblog.services.UserService;
import info.kuyur.justblog.utils.ClientMappableException;
import info.kuyur.justblog.utils.Config;
import info.kuyur.justblog.utils.Locale;
import info.kuyur.justblog.utils.LocaleLoader;
import info.kuyur.justblog.utils.Signature;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TimeZone;
import java.util.TreeMap;

import javax.ws.rs.core.Response.Status;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import com.sun.jersey.spi.container.ResourceFilter;

public class AuthenticationFilter implements ResourceFilter, ContainerRequestFilter {

	@Override
	public ContainerRequestFilter getRequestFilter() {
		return this;
	}

	@Override
	public ContainerResponseFilter getResponseFilter() {
		return null;
	}

	// TODO To tomcat logs
	@Override
	public ContainerRequest filter(ContainerRequest request) {
		Locale locale = LocaleLoader.getByCookie(request.getCookies().get("lang"));

		String requestMethod = request.getMethod();
		String domain = request.getHeaderValue("Host");
		URI uri = request.getRequestUri();
		String path = uri.getPath();
		String algorithm = "HmacSHA256";

		//final boolean isSecure = uri.getScheme().equals("https");
		String username = null;
		String sentSignature = null;
		String timeStamp = null;

		SortedMap<String, String> sorted = new TreeMap<String, String>();
		try {
			for (Entry<String, List<String>> entry : request.getQueryParameters(false).entrySet()) {
				if (entry.getValue().size() == 1) {
					if (entry.getKey().equals("signature")) {
						sentSignature = URLDecoder.decode(entry.getValue().get(0), Config.DEFAULT_ENCODING);
						continue;
					}
					if (entry.getKey().equals("username")) {
						username = URLDecoder.decode(entry.getValue().get(0), Config.DEFAULT_ENCODING);
					}
					if (entry.getKey().equals("timestamp")) {
						timeStamp =  URLDecoder.decode(entry.getValue().get(0), Config.DEFAULT_ENCODING);
					}
					sorted.put(entry.getKey(), entry.getValue().get(0));
				}
			}
		} catch (UnsupportedEncodingException e) {
			throw new ClientMappableException(locale.getString("Unauthorized"), Status.UNAUTHORIZED);
		}

		if (username == null || sentSignature == null || timeStamp == null) {
			throw new ClientMappableException(locale.getString("Unauthorized"), Status.UNAUTHORIZED);
		}

		try {
			Calendar now = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
			Date ts = getParseTimestamp(timeStamp);
			Calendar fromClient = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
			fromClient.setTime(ts);
			// signature will effective in 100 seconds.
			if (Math.abs(fromClient.compareTo(now)) > Config.DEFAULT_VALIDITY_PERIOD) {
				throw new ClientMappableException("InvalidSignature", Status.UNAUTHORIZED);
			}
		} catch (ParseException e1) {
			throw new ClientMappableException("Unauthorized", Status.UNAUTHORIZED);
		}

		// read from db.
		UserService service = new UserService();
		final String hash = service.getHashPassword(username);
		if (hash == null) {
			throw new ClientMappableException("InvalidAccount", Status.UNAUTHORIZED);
		}

		String scheme = uri.getScheme();
		try {
			String signature = Signature.sign(sorted, hash, algorithm, requestMethod,
					scheme + "://" + domain + path);
			if (sentSignature.equals(signature)) {
				// TODO
			} else {
				throw new ClientMappableException("IncorrectSignature", Status.UNAUTHORIZED);
			}
			// TODO permission check
		} catch (NoSuchAlgorithmException e) {
			throw new ClientMappableException("Unauthorized", Status.UNAUTHORIZED);
		} catch (InvalidKeyException e) {
			throw new ClientMappableException("Unauthorized", Status.UNAUTHORIZED);
		} catch (UnsupportedEncodingException e) {
			throw new ClientMappableException("Unauthorized", Status.UNAUTHORIZED);
		} catch (URISyntaxException e) {
			throw new ClientMappableException("Unauthorized", Status.UNAUTHORIZED);
		}
		return request;
	}

	/**
	 * Formats date as ISO 8601 timestamp
	 *
	 * @throws ParseException
	 */
	private Date getParseTimestamp(String source) throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		df.setTimeZone(TimeZone.getTimeZone("UTC"));
		return df.parse(source);
	}
}
