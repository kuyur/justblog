package info.kuyur.justblog.client.util;

import static org.junit.Assert.assertEquals;
import info.kuyur.justblog.client.util.ClientUtils;
import info.kuyur.justblog.models.user.User;
import info.kuyur.justblog.models.user.UserRole;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


import org.junit.Test;

public class ClientUtilTest {

	@Test
	public void testObjectToJSONString() {
		User user = new User(1L, "admin", "馆里猿", "admin@justblog.org",
				"http://justblog.org", UserRole.ADMIN);
		System.out.println(ClientUtils.toJsonString(user));
	}

	@Test
	public void testArrayToJSONString() {
		User[] users = {
			new User(1L, "admin", "馆里猿", "admin@justblog.org",
					"http://justblog.org", UserRole.ADMIN),
			new User(4L, "reader", "访客", "reader@justblog.org",
					"http://justblog.org", UserRole.READER)
		};
		System.out.println(ClientUtils.toJsonString(users));
	}

	@Test
	public void testListToJSONString() {
		List<User> users = new ArrayList<User>();
		users.add(new User(1L, "admin", "馆里猿", "admin@justblog.org",
				"http://justblog.org", UserRole.ADMIN));
		users.add(new User(4L, "reader", "访客", "reader@justblog.org",
				"http://justblog.org", UserRole.READER));
		System.out.println(ClientUtils.toJsonString(users));
	}

	@Test
	public void testReadJSONFromString() throws UnsupportedEncodingException {
		String json = "{\"UserId\":1,\"Account\":\"admin\",\"Nicename\":\"馆里猿\",\"Email\":\"admin@justblog.org\",\"Url\":\"http://justblog.org\",\"Role\":\"ADMIN\"}";
		ByteArrayInputStream bais = new ByteArrayInputStream(json.getBytes("UTF-8"));
		User admin = ClientUtils.readFromJson(bais, User.class);
		System.out.println(admin);
		assertEquals(admin.getAccount(), "admin");
		assertEquals(admin.getEmail(), "admin@justblog.org");
		assertEquals(admin.getNicename(), "馆里猿");
		assertEquals(admin.getRole(), UserRole.ADMIN);
		assertEquals(admin.getUrl(), "http://justblog.org");
		assertEquals(admin.getUserId(), new Long(1));
	}
}
