package info.kuyur.justblog.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

public class LocaleLoaderTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test
	public void testGetLocaleExisting() {
		Locale locale1 = LocaleLoader.get(LocaleLoader.LOCALE_ZH_CN);
		assertTrue(LocaleLoader.LOCALE_ZH_CN.equals(locale1.getLocale()));
		assertTrue("zh_CN".equals(locale1.getLocale()));

		Locale locale2 = LocaleLoader.get("zH_Cn");
		assertTrue(LocaleLoader.LOCALE_ZH_CN.equals(locale2.getLocale()));
		assertTrue("zh_CN".equals(locale2.getLocale()));

		assertEquals(locale1, locale2);
	}

	@Test
	public void testGetLocaleNotExisting() {
		Locale locale1 = LocaleLoader.get("notexistinglocalestring");
		assertTrue(LocaleLoader.LOCALE_ZH_CN.equals(locale1.getLocale()));
		assertTrue("zh_CN".equals(locale1.getLocale()));

		Locale locale2 = LocaleLoader.get("");
		assertTrue(LocaleLoader.LOCALE_ZH_CN.equals(locale2.getLocale()));
		assertTrue("zh_CN".equals(locale2.getLocale()));

		Locale locale3 = LocaleLoader.get(null);
		assertTrue(LocaleLoader.LOCALE_ZH_CN.equals(locale3.getLocale()));
		assertTrue("zh_CN".equals(locale3.getLocale()));
	}

	@Test
	public void testGetStringDefined() {
		Locale locale = LocaleLoader.get(LocaleLoader.LOCALE_ZH_CN);
		System.out.println(locale.getString("InvalidAccount"));
	}

	@Test
	public void testGetStringUndefined() {
		Locale locale = LocaleLoader.get(LocaleLoader.LOCALE_ZH_CN);
		System.out.println(locale.getString("MessageNotDefined"));
	}
}
