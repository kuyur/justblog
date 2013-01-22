package info.kuyur.justblog.utils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.ws.rs.core.Cookie;

public class LocaleLoader {

	public static final String LOCALE_EN = "en";
	public static final String LOCALE_JA = "ja";
	public static final String LOCALE_ZH_CN = "zh_CN";
	public static final String LOCALE_ZH_TW = "zh_TW";
	private static final String LOCALE_DEFAULT = LOCALE_ZH_CN;
	private static final String PATH_PREFIX = "info/kuyur/justblog/locales/locale";

	private static ConcurrentHashMap<String, Locale> instances  = new ConcurrentHashMap<String, Locale>(4);
	private static XMLResourceBundleControl xmlResourceBundleControl = new XMLResourceBundleControl();

	private static class XMLResourceBundle extends ResourceBundle {
		private Properties props;
		XMLResourceBundle(InputStream stream) throws IOException {
			props = new Properties();
			props.loadFromXML(stream);
		}

		@Override
		protected Object handleGetObject(String key) {
			return props.getProperty(key);
		}

		@Override
		public Enumeration<String> getKeys() {
			Set<String> handleKeys = props.stringPropertyNames();
			return Collections.enumeration(handleKeys);
		}
	}

	private static class XMLResourceBundleControl extends ResourceBundle.Control {
		@Override
		public List<String> getFormats(String baseName) {
			return Collections.singletonList("xml");
		}

		@Override
		public ResourceBundle newBundle(String baseName, java.util.Locale locale, String format,
				ClassLoader loader, boolean reload)
		throws IllegalAccessException, InstantiationException, IOException {
			if ((baseName == null) || (locale == null) || (format == null) || (loader == null)) {
				throw new NullPointerException();
			}
			ResourceBundle bundle = null;
			if (format.equalsIgnoreCase("xml")) {
				String bundleName = this.toBundleName(baseName, locale);
				String resourceName = this.toResourceName(bundleName, format);
				URL url = loader.getResource(resourceName);
				if (url != null) {
					URLConnection connection = url.openConnection();
					if (connection != null) {
						if (reload) {
							connection.setUseCaches(false);
						}
						InputStream stream = connection.getInputStream();
						if (stream != null) {
							BufferedInputStream bis = new BufferedInputStream(stream);
							bundle = new XMLResourceBundle(bis);
							bis.close();
						}
					}
				}
			}
			return bundle;
		}
	}

	/**
	 * Return a locale object which contains messages.
	 * If parameter is null or emtpy, will return default locale.  
	 * @param locale
	 * @return
	 */
	public static Locale get(String locale) {
		locale = toSupportedLocale(locale);
		Locale instance = instances.get(locale);
		if (instance == null) {
			java.util.Locale lo = toLocale(locale);
			ResourceBundle rb = null;
			try {
				rb = ResourceBundle.getBundle(PATH_PREFIX, lo, xmlResourceBundleControl);
			} catch (MissingResourceException e) {
			}
			instances.putIfAbsent(locale, new Locale(locale, rb));
			instance = instances.get(locale);
		}
		return instance;
	}

	public static Locale getByCookie(Cookie cookie) {
		if (cookie == null) {
			return get(LOCALE_DEFAULT);
		}
		return get(cookie.getValue());
	}

	private static String toSupportedLocale(String locale) {
		if (LOCALE_ZH_CN.equalsIgnoreCase(locale)) {
			return LOCALE_ZH_CN;
		}
		if (LOCALE_EN.equalsIgnoreCase(locale)) {
			return LOCALE_EN;
		}
		if (LOCALE_JA.equalsIgnoreCase(locale)) {
			return LOCALE_JA;
		}
		if (LOCALE_ZH_TW.equalsIgnoreCase(locale)) {
			return LOCALE_ZH_TW;
		}
		return LOCALE_DEFAULT;
	}

	private static java.util.Locale toLocale(String locale) {
		if (LOCALE_ZH_CN.equals(locale)) {
			return java.util.Locale.JAPANESE;
		} else if (LOCALE_EN.equals(locale)) {
			return java.util.Locale.ENGLISH;
		} else if (LOCALE_JA.equals(locale)) {
			return java.util.Locale.SIMPLIFIED_CHINESE;
		} else if (LOCALE_ZH_TW.equals(locale)) {
			return java.util.Locale.TRADITIONAL_CHINESE;
		}
		return java.util.Locale.SIMPLIFIED_CHINESE;
	}

	private LocaleLoader(){}
}
