package info.kuyur.justblog.client.http;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
	// Query parameters
	private Map<String, String> queryParameters = new HashMap<String, String>();
	// Form parameters
	private Map<String, String> bodyParameters = new HashMap<String, String>();
	// Request headers
	private Map<String, String> headers = new HashMap<String, String>();
	// Method name
	private String methodName;
	// Resource path
	private String resourcePath;
	// JSON Object to send.
	private Object bodyContent;
	// File Object to send.
	private File fileObject;

	/**
	 * Supported methods: GET, POST, PUT, DELETE
	 * @param methodName
	 */
	public HttpRequest(String methodName, String resourcePath) {
		this.methodName = methodName;
		this.resourcePath = resourcePath;
	}

	public String getMethodName() {
		return methodName;
	}

	public Map<String, String> getQueryParameters() {
		return queryParameters;
	}

	public Map<String, String> getBodyParameters() {
		return bodyParameters;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public void addHeader(String name, String value) {
		headers.put(name, value);
	}

	public void removeHeader(String name) {
		headers.remove(name);
	}

	public void addQueryParameter(String name, String value) {
		queryParameters.put(name, value);
	}

	public void setQueryParameters(Map<String, String> parameters) {
		this.queryParameters = parameters;
	}

	public HttpRequest withParameter(String name, String value) {
		addQueryParameter(name, value);
		return this;
	}

	public String getResourcePath() {
		return resourcePath;
	}

	public void setResourcePath(String resourcePath) {
		this.resourcePath = resourcePath;
	}

	public Object getBodyContent() {
		return bodyContent;
	}

	public void setBodyContent(Object bodyContent) {
		this.bodyContent = bodyContent;
	}

	public File getFileObject() {
		return fileObject;
	}

	public void setFileObject(File fileObject) {
		this.fileObject = fileObject;
	}

	public void addBodyParameter(String name, String value) {
		bodyParameters.put(name, value);
	}

	public void setBodyParameters(Map<String, String> bodyParameters) {
		this.bodyParameters = bodyParameters;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		builder.append(getMethodName().toString() + " ");

		builder.append((getResourcePath() != null ? getResourcePath() : "") + " ");

		if (!getQueryParameters().isEmpty()) {
			builder.append("Parameters: (");
			for (String key : getQueryParameters().keySet()) {
				String value = getQueryParameters().get(key);
				builder.append(key + ": " + value + ", ");
			}
			builder.append(") ");
		}

		if (!getHeaders().isEmpty()) {
			builder.append("Headers: (");
			for (String key : getHeaders().keySet()) {
				String value = getHeaders().get(key);
				builder.append(key + ": " + value + ", ");
			}
			builder.append(") ");
		}

		return builder.toString();
	}
}
