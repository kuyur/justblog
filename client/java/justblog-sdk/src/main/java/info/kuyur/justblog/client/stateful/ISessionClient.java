package info.kuyur.justblog.client.stateful;

import java.io.File;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.cookie.Cookie;

public interface ISessionClient {

	void login();
	void logout();
	List<Cookie> getCookies();
	File getFile(String pathToSave, String resourcePath, Collection<NameValuePair> queryParams);
	<T> T postFile(Type clazz, String resourcePath, Collection<NameValuePair> queryParams, Collection<NameValuePair> bodyParams, File file, String fileFiled);
	<T> T get(Type clazz, String resourcePath, Collection<NameValuePair> queryParams);
	<T> T post(Type clazz, String resourcePath, Collection<NameValuePair> queryParams, Collection<NameValuePair> bodyParams, Object bodyContent);
	<T> T put(Type clazz, String resourcePath, Collection<NameValuePair> queryParams, Object bodyContent);
	<T> T delete(Type clazz, String resourcePath, Collection<NameValuePair> queryParams);
}
