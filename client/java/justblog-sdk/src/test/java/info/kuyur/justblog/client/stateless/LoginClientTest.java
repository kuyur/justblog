package info.kuyur.justblog.client.stateless;

import static org.junit.Assert.fail;
import info.kuyur.justblog.client.util.ResourceCloser;
import info.kuyur.justblog.models.common.JustResult;

import java.io.InputStream;
import java.util.Properties;

import org.junit.BeforeClass;
import org.junit.Test;

public class LoginClientTest {

	private static final String PROPERTIES_PATH = "host-info.properties";
	private static LoginClient adminClient;
	private static LoginClient readerClient;

	@BeforeClass
	public static void setUpBeforeClass() {
		InputStream inputStream = null;
		try {
			inputStream = LoginClientTest.class.getClassLoader().getResourceAsStream(PROPERTIES_PATH);
			Properties properties = new Properties();
			properties.load(inputStream);
			String hostname = properties.getProperty("host");
			String port = properties.getProperty("port");
			String isHttps = properties.getProperty("https");
			String basePath = properties.getProperty("base");
			String username = properties.getProperty("account");
			String password = properties.getProperty("password");
			adminClient = new LoginClient(hostname, Integer.valueOf(port), Boolean.valueOf(isHttps),
					basePath, username, password);
			readerClient = new LoginClient(hostname, Integer.valueOf(port), Boolean.valueOf(isHttps),
					basePath, "reader", "reader");
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		} finally {
			ResourceCloser.close(inputStream);
		}
	}

	@Test
	public void testAdminLogin() {
		JustResult result = adminClient.login();
		System.out.println(result);
	}

	@Test
	public void testReaderLogin() {
		JustResult result = readerClient.login();
		System.out.println(result);
	}
}
