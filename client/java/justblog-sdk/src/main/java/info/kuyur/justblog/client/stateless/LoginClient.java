package info.kuyur.justblog.client.stateless;

import info.kuyur.justblog.models.common.JustResult;

public class LoginClient extends HmacClient {

	/** API list*/
	private static final String LOGIN = "/rest/login";

	public LoginClient(String hostname, int port, boolean isSecure, String basePath,
			String account, String password) {
		super(hostname, port, isSecure, basePath, account, password);
	}

	public JustResult login() {
		return super.post(JustResult.class, LOGIN, null, null, null);
	}
}
