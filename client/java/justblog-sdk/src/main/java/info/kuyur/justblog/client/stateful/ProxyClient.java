package info.kuyur.justblog.client.stateful;

import java.io.File;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.cookie.Cookie;

public abstract class ProxyClient implements ISessionClient {

	private ISessionClient baseClient;

	public ProxyClient(ISessionClient baseClient) {
		if (baseClient == null) {
			throw new NullPointerException("Base client can not be null.");
		}
		this.baseClient = baseClient;
	}

	@Override
	public void login() {
		baseClient.login();
	}

	@Override
	public void logout() {
		baseClient.logout();
	}

	@Override
	public void close() {
		baseClient.close();
	}

	@Override
	public List<Cookie> getCookies() {
		return baseClient.getCookies();
	}

	@Override
	public File getFile(String pathToSave, String resourcePath, Collection<NameValuePair> queryParams) {
		return baseClient.getFile(pathToSave, resourcePath, queryParams);
	}

	@Override
	public <T> T postFile(Type clazz, String resourcePath, Collection<NameValuePair> queryParams,
			Collection<NameValuePair> bodyParams, File file, String fileField) {
		return baseClient.<T>postFile(clazz, resourcePath, queryParams, bodyParams, file, fileField);
	}

	@Override
	public <T> T postFiles(Type clazz, String resourcePath, Collection<NameValuePair> queryParams,
			Collection<NameValuePair> bodyParams, Map<String, File> files) {
		return baseClient.<T>postFiles(clazz, resourcePath, queryParams, bodyParams, files);
	}

	@Override
	public <T> T get(Type clazz, String resourcePath, Collection<NameValuePair> queryParams) {
		return baseClient.<T>get(clazz, resourcePath, queryParams);
	}

	@Override
	public <T> T post(Type clazz, String resourcePath, Collection<NameValuePair> queryParams,
			Collection<NameValuePair> bodyParams, Object bodyContent) {
		return baseClient.<T>post(clazz, resourcePath, queryParams, bodyParams, bodyContent);
	}

	@Override
	public <T> T put(Type clazz, String resourcePath, Collection<NameValuePair> queryParams, Object bodyContent) {
		return baseClient.<T>put(clazz, resourcePath, queryParams, bodyContent);
	}

	@Override
	public <T> T delete(Type clazz, String resourcePath, Collection<NameValuePair> queryParams) {
		return baseClient.<T>delete(clazz, resourcePath, queryParams);
	}
}
