package info.kuyur.justblog.client.util;

import info.kuyur.justblog.client.util.ClientUtils;
import info.kuyur.justblog.models.user.User;

import java.util.ArrayList;
import java.util.List;


import org.junit.Test;

public class ClientUtilTest {

	@Test
	public void testObjectToJSONString() {
		User user = new User("admin", 1L);
		System.out.println(ClientUtils.toJsonString(user));
	}

	@Test
	public void testArrayToJSONString() {
		User[] users = {
			new User("admin", 1L),
			new User("reader", 4L)
		};
		System.out.println(ClientUtils.toJsonString(users));
	}

	@Test
	public void testListToJSONString() {
		List<User> users = new ArrayList<User>();
		users.add(new User("admin", 1L));
		users.add(new User("reader", 4L));
		System.out.println(ClientUtils.toJsonString(users));
	}
}
