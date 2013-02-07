package info.kuyur.justblog.utils;

public class Config {

	/**
	 * Default encoding.
	 * You should not change it because justblog will only support UTF-8.
	 */
	public static final String DEFAULT_ENCODING = "UTF-8";

	/**
	 * Default validity period for per signature in millisecond.
	 */
	public static final int DEFAULT_VALIDITY_PERIOD = 100 * 1000;

	/**
	 * From JustBlog specification draft.<br>
	 * This default cookie token for logined-session is only used in stateful version. <br>
	 * Don't change it.
	 */
	public static final String LOGINED_SESSION_TOKEN = "JUSTBLOGSID";

	/**
	 * From JustBlog specification draft.<br>
	 * Don't change it.
	 */
	public static final String LANG_TOKEN = "lang";

	/**
	 * From JustBlog specification draft.<br>
	 * Don't change them.
	 */
	public static final String ACCOUNT_FIELD = "account";
	public static final String SIGN_FIELD = "sign";
	public static final String TIMESTAMP_FIELD = "timestamp";

	/**
	 * Signature algorithm.
	 */
	public static final String ALGORITHM = "HmacSHA256";

	/**
	 * Line separator.
	 */
	public static final String LINE_SEPARATOR = System.getProperty("line.separator");

	private Config(){}
}
