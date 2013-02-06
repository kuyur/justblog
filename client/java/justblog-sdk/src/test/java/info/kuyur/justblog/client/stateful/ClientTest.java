package info.kuyur.justblog.client.stateful;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import info.kuyur.justblog.client.stateful.BaseClient;
import info.kuyur.justblog.client.util.ResourceCloser;
import info.kuyur.justblog.utils.Config;

import java.io.InputStream;
import java.util.List;
import java.util.Properties;


import org.apache.http.cookie.Cookie;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class ClientTest {

	private static final String PROPERTIES_PATH = "host-info.properties";
	private static BaseClient client;

	@BeforeClass
	public static void setUpBeforeClass() {
		InputStream inputStream = null;
		try {
			inputStream = ClientTest.class.getClassLoader().getResourceAsStream(PROPERTIES_PATH);
			Properties properties = new Properties();
			properties.load(inputStream);
			String hostname = properties.getProperty("host");
			String port = properties.getProperty("port");
			String isHttps = properties.getProperty("https");
			String basePath = properties.getProperty("base");
			String username = properties.getProperty("account");
			String password = properties.getProperty("password");
			client = new BaseClient(hostname, Integer.valueOf(port), Boolean.valueOf(isHttps),
					basePath, username, password);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		} finally {
			ResourceCloser.close(inputStream);
		}
	}

	@AfterClass
	public static void tearDownAfterClass() {
	}

	@Before
	public void setUp() {
	}

	@After
	public void tearDown() {
	}

	@Ignore
	@Test
	public void testLoginSuccess() {
		List<Cookie> beforeLoginCookies = client.getCookies();
		assertTrue(beforeLoginCookies.isEmpty());
		client.login();
		List<Cookie> afterLoginCookies = client.getCookies();
		boolean loginSuccess = false;
		for (Cookie cookie : afterLoginCookies) {
			if (Config.LOGINED_SESSION_TOKEN.equals(cookie.getName())) {
				loginSuccess =true;
			}
			System.out.println(cookie);
		}
		assertTrue(loginSuccess);
	}
}
