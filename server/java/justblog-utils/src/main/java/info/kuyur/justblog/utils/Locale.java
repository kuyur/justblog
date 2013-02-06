package info.kuyur.justblog.utils;

import java.text.MessageFormat;
import java.util.ResourceBundle;

public class Locale {

	private static final String MESSAGE_NOT_FOUND = "Message not found.";
	private static final String LOCALE_FILE_NOT_EXISTED = "Locale file locale_{1}.xml not existing.";

	private String locale;
	private ResourceBundle resourceBundle;

	// can not be constructed by external package
	Locale(String locale, ResourceBundle resourceBundle) {
		this.locale = locale;
		this.resourceBundle = resourceBundle;
	}

	public String getLocale() {
		return locale;
	}

	public String getString(String key) {
		if (resourceBundle == null) {
			return MessageFormat.format(LOCALE_FILE_NOT_EXISTED, locale);
		}
		try {
			return resourceBundle.getString(key);
		} catch (Exception e) {
			return MESSAGE_NOT_FOUND;
		}
	}
}
