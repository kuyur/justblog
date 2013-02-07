package info.kuyur.justblog.client.http;

public enum Method {
	GET("GET"),
	POST("POST"),
	PUT("PUT"),
	DELETE("DELETE");

	private String name;
	private Method(String name) {
		this.name = name;
	}

	public String toString() {
		return name;
	}
}
