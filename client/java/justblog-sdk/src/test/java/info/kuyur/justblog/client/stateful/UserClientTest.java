package info.kuyur.justblog.client.stateful;

import static org.junit.Assert.fail;

import info.kuyur.justblog.client.util.ResourceCloser;

import java.io.InputStream;
import java.util.Properties;


import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

public class UserClientTest {

	private static final String PROPERTIES_PATH = "host-info.properties";
	private static UserClient client;

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
			client = new UserClient(hostname, Integer.valueOf(port), Boolean.valueOf(isHttps),
					basePath, username, password);
			client.login();
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
}
