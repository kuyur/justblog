package info.kuyur.justblog.servlet.filter;

import info.kuyur.justblog.dao.UserDao.Credentials;
import info.kuyur.justblog.models.user.UserRole;
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
import java.security.Principal;
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
import javax.ws.rs.core.SecurityContext;

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

		final boolean isSecure = uri.getScheme().equals("https");
		String account = null;
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
					if (entry.getKey().equals("account")) {
						account = URLDecoder.decode(entry.getValue().get(0), Config.DEFAULT_ENCODING);
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

		if (account == null || sentSignature == null || timeStamp == null) {
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
		final Credentials credentials = service.getCredentials(account);
		if (credentials == null) {
			throw new ClientMappableException("InvalidAccount", Status.UNAUTHORIZED);
		}

		String scheme = uri.getScheme();
		try {
			String signature = Signature.sign(sorted, credentials.getHashedKey(), algorithm, requestMethod,
					scheme + "://" + domain + path);
			if (sentSignature.equals(signature)) {
				request.setSecurityContext(new MySecurityContext(credentials, isSecure));
			} else {
				throw new ClientMappableException("IncorrectSignature", Status.UNAUTHORIZED);
			}
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

	private static class MySecurityContext implements SecurityContext {

		private final Credentials credentials;
		private final boolean isSecure;

		public MySecurityContext(Credentials credentials, boolean isSecure) {
			super();
			this.credentials = credentials;
			this.isSecure = isSecure;
		}

		@Override
		public Principal getUserPrincipal() {
			return new Principal() {
				@Override
				public String getName() {
					return credentials.getAccount();
				}
			};
		}

		@Override
		public boolean isUserInRole(String role) {
			UserRole userRole = credentials.getRole();
			return (userRole == null) ? false : userRole.containRole(role);
		}

		@Override
		public boolean isSecure() {
			return isSecure;
		}

		@Override
		public String getAuthenticationScheme() {
			return SecurityContext.BASIC_AUTH;
		}
	}
}
