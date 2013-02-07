package info.kuyur.justblog.client.stateless;

import static org.junit.Assert.fail;
import info.kuyur.justblog.client.util.ResourceCloser;
import info.kuyur.justblog.models.user.User;

import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import org.junit.BeforeClass;
import org.junit.Test;

public class UserClientTest {
	private static final String PROPERTIES_PATH = "host-info.properties";
	private static UserClient adminClient;
	private static UserClient readerClient;

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
			adminClient = new UserClient(hostname, Integer.valueOf(port), Boolean.valueOf(isHttps),
					basePath, username, password);
			readerClient = new UserClient(hostname, Integer.valueOf(port), Boolean.valueOf(isHttps),
					basePath, "reader", "reader");
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		} finally {
			ResourceCloser.close(inputStream);
		}
	}

	@Test
	public void testGetAllUsers1() {
		List<User> users = adminClient.getAllUsers();
		System.out.println(users);
	}

	@Test
	public void testGelAllUsers2() {
		List<User> users = readerClient.getAllUsers();
		System.out.println(users);
	}

	@Test
	public void testDeleteUser1() {
		adminClient.deleteUser(1L);
	}

	@Test
	public void testDeleteUser2() {
		readerClient.deleteUser(1L);
	}
}
