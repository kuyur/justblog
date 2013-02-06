package info.kuyur.justblog.client.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.junit.Test;

public class SignUtilsTest {

	@Test
	public void testGetFormattedTimestamp() throws UnsupportedEncodingException {
		String timestamp = SignUtils.getFormattedTimestamp();
		System.out.println(timestamp);
		System.out.println(URLEncoder.encode(timestamp, "UTF-8"));
	}
}
