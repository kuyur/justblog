package info.kuyur.justblog.client.http;

public class ClientConfiguration {

	/**
	 * The default HTTP user agent header for JustBlog Java SDK clients.
	 */
	public static final String USER_AGENT = "justblog sdk 1.0";

	/**
	 * The maximum number of times that a retryable failed request (ex: a 5xx
	 * response from a service) will be retried.
	 */
	public static final int MAX_ERROR_RETRY = 2;

	/**
	 * The protocol to use when connecting to Amazon Web Services.
	 * <p>
	 * The default configuration is to use HTTPS for all requests for increased
	 * security.
	 */
	public static final String PROTOCOL = "http"; //Protocol.HTTPS;

	/** Optionally specifies the proxy host to connect through. */
	public static final String PROXY_HOST = null;

	/** Optionally specifies the port on the proxy host to connect through. */
	public static final int PROXY_PORT = -1;

	/** Optionally specifies the user name to use when connecting through a proxy. */
	public static final String PROXY_USERNAME = null;

	/** Optionally specifies the password to use when connecting through a proxy. */
	public static final String PROXY_PASSWORD = null;

	/** The maximum number of open HTTP connections. */
	public static final int MAX_CONNECTIONS = 50;

	/**
	 * The amount of time to wait (in milliseconds) for data to be transfered
	 * over an established, open connection before the connection is timed out.
	 * A value of 0 means infinity, and is not recommended.
	 */
	public static final int SOKET_TIMEOUT = 10 * 1000;

	/**
	 * The amount of time to wait (in milliseconds) when initially establishing
	 * a connection before giving up and timing out. A value of 0 means
	 * infinity, and is not recommended.
	 */
	public static final int CONNECTION_TIMEOUT = 10 * 1000;

	/**
	 * Uploading a big file may cost much time.
	 * The number shouldn't be so small.
	 */
	public static final long CONNECTION_KEEP_TIMEOUT = 100 * 1000;

	/**
	 * Optional size hint (in bytes) for the low level TCP send buffer. This is
	 * an advanced option for advanced users who want to tune low level TCP
	 * parameters to try and squeeze out more performance.
	 */
	public static final int SOCKET_SEND_BUFFER_SIZE_HINT = 0;

	/**
	 * Optional size hint (in bytes) for the low level TCP receive buffer. This
	 * is an advanced option for advanced users who want to tune low level TCP
	 * parameters to try and squeeze out more performance.
	 */
	public static final int SOCKET_RECEIVE_BUFFER_SIZE_HINT = 0;

	public static final String CONTENT_CHARSET = "utf-8";

}