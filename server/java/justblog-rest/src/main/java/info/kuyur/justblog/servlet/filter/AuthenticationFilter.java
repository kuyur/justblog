package info.kuyur.justblog.servlet.filter;

import info.kuyur.justblog.dao.UserDao.Credentials;
import info.kuyur.justblog.models.user.UserRole;
import info.kuyur.justblog.services.UserService;
import info.kuyur.justblog.utils.ClientMappableException;
import info.kuyur.justblog.utils.Config;
import info.kuyur.justblog.utils.Locale;
import info.kuyur.justblog.utils.LocaleLoader;
import info.kuyur.justblog.utils.Signature;
import info.kuyur.justblog.utils.StreamUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sun.jersey.core.header.MediaTypes;
import com.sun.jersey.core.util.ReaderWriter;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import com.sun.jersey.spi.container.ResourceFilter;

public class AuthenticationFilter implements ResourceFilter, ContainerRequestFilter {

	private final Log logger = LogFactory.getLog(getClass());

	@Override
	public ContainerRequestFilter getRequestFilter() {
		return this;
	}

	@Override
	public ContainerResponseFilter getResponseFilter() {
		return null;
	}

	@Override
	public ContainerRequest filter(ContainerRequest request) {
		Locale locale = LocaleLoader.getByCookie(request.getCookies().get(Config.LANG_TOKEN));

		String requestMethod = request.getMethod();
		String domain = request.getHeaderValue("Host");
		URI uri = request.getRequestUri();
		String path = uri.getPath();

		final boolean isSecure = uri.getScheme().equals("https");
		String account = null;
		String sentSignature = null;
		String timeStamp = null;

		SortedMap<String, String> sorted = new TreeMap<String, String>();
		try {
			for (Entry<String, List<String>> entry : request.getQueryParameters(false).entrySet()) {
				if (entry.getValue().size() == 1) {
					if (entry.getKey().equals(Config.SIGN_FIELD)) {
						sentSignature = URLDecoder.decode(entry.getValue().get(0), Config.DEFAULT_ENCODING);
						continue;
					}
					if (entry.getKey().equals(Config.ACCOUNT_FIELD)) {
						account = URLDecoder.decode(entry.getValue().get(0), Config.DEFAULT_ENCODING);
					}
					if (entry.getKey().equals(Config.TIMESTAMP_FIELD)) {
						timeStamp =  URLDecoder.decode(entry.getValue().get(0), Config.DEFAULT_ENCODING);
					}
					sorted.put(entry.getKey(), entry.getValue().get(0));
				}
			}
		} catch (UnsupportedEncodingException e) {
			throw new ClientMappableException(locale.getString("Unauthorized"), Status.UNAUTHORIZED);
		}
		String contentBody = null;
		if (MediaTypes.typeEquals(MediaType.APPLICATION_FORM_URLENCODED_TYPE, request.getMediaType()) ||
				MediaTypes.typeEquals(MediaType.APPLICATION_JSON_TYPE, request.getMediaType())) {
			contentBody = getContentBodyFromRequest(request);
		} else if (MediaTypes.typeEquals(MediaType.MULTIPART_FORM_DATA_TYPE, request.getMediaType())) {
			// ignoring. Multipart form data may be so big.
		}

		if (account == null || sentSignature == null || timeStamp == null) {
			throw new ClientMappableException(locale.getString("Unauthorized"), Status.UNAUTHORIZED);
		}

		try {
			Calendar now = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
			Date ts = getParseTimestamp(timeStamp);
			Calendar fromClient = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
			fromClient.setTime(ts);
			// signature will be effective in 100 seconds.
			if (Math.abs(fromClient.compareTo(now)) > Config.DEFAULT_VALIDITY_PERIOD) {
				throw new ClientMappableException("Obsolete signature.", Status.UNAUTHORIZED);
			}
		} catch (ParseException e1) {
			throw new ClientMappableException("Invalid timestamp.", Status.UNAUTHORIZED);
		}

		// read from db.
		UserService service = new UserService();
		final Credentials credentials = service.getCredentials(account);
		if (credentials == null) {
			throw new ClientMappableException("Invalid account.", Status.UNAUTHORIZED);
		}

		String scheme = uri.getScheme();
		try {
			String signature = Signature.sign(sorted, contentBody, credentials.getHashedKey(), Config.ALGORITHM, requestMethod,
					scheme + "://" + domain + path);
			if (sentSignature.equals(signature)) {
				request.setSecurityContext(new MySecurityContext(credentials, isSecure));
			} else {
				logger.debug("Signature sent: " + sentSignature);
				logger.debug("Signature calculated: " + signature);
				throw new ClientMappableException("Incorrect signature.", Status.UNAUTHORIZED);
			}
		} catch (NoSuchAlgorithmException e) {
			throw new ClientMappableException(locale.getString("Unauthorized"), Status.UNAUTHORIZED);
		} catch (InvalidKeyException e) {
			throw new ClientMappableException(locale.getString("Unauthorized"), Status.UNAUTHORIZED);
		} catch (UnsupportedEncodingException e) {
			throw new ClientMappableException(locale.getString("Unauthorized"), Status.UNAUTHORIZED);
		} catch (URISyntaxException e) {
			throw new ClientMappableException(locale.getString("Unauthorized"), Status.UNAUTHORIZED);
		}
		return request;
	}

	private String getContentBodyFromRequest(ContainerRequest request) {
		InputStream in = request.getEntityInputStream();
		if (in == null) {
			return null;
		}
		if (in.getClass() != ByteArrayInputStream.class) {
			// Buffer input
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			try {
				ReaderWriter.writeTo(in, byteArrayOutputStream);
			} catch (IOException e) {
				return null;
			}
			in = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
			request.setEntityInputStream(in);
		}
		ByteArrayInputStream bais = (ByteArrayInputStream) in;
		String contentBody = StreamUtils.readInputStream(bais, Charset.forName(Config.DEFAULT_ENCODING));
		bais.reset();
		return contentBody;
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
			return SecurityContext.FORM_AUTH;
		}
	}
}
