package info.kuyur.justblog.client.stateful;

import java.io.File;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.cookie.Cookie;

public interface ISessionClient {

	void login();
	void logout();
	void close();
	List<Cookie> getCookies();
	File getFile(String pathToSave, String resourcePath, Collection<NameValuePair> queryParams);
	<T> T postFile(Type clazz, String resourcePath, Collection<NameValuePair> queryParams, Collection<NameValuePair> bodyParams, File file, String fileField);
	<T> T postFiles(Type clazz, String resourcePath, Collection<NameValuePair> queryParams, Collection<NameValuePair> bodyParams, Map<String, File> files);
	<T> T get(Type clazz, String resourcePath, Collection<NameValuePair> queryParams);
	<T> T post(Type clazz, String resourcePath, Collection<NameValuePair> queryParams, Collection<NameValuePair> bodyParams, Object bodyContent);
	<T> T put(Type clazz, String resourcePath, Collection<NameValuePair> queryParams, Object bodyContent);
	<T> T delete(Type clazz, String resourcePath, Collection<NameValuePair> queryParams);
}
