package info.kuyur.justblog.utils;

public class Config {

	/**
	 * Default encoding.
	 * You should not change it because justblog only support UTF-8.
	 */
	public static final String DEFAULT_ENCODING = "UTF-8";

	/**
	 * Default validity period for per signature in millisecond.
	 */
	public static final int DEFAULT_VALIDITY_PERIOD = 100 * 1000;

	/**
	 * Default Cookie-Token for logined-session.
	 * It is only used in stateful version. 
	 */
	public static final String LOGINED_SESSION_TOKEN = "JUSTBLOGSID";

	private Config(){}
}
