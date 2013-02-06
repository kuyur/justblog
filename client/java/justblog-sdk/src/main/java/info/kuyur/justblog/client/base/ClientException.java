package info.kuyur.justblog.client.base;

public class ClientException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6398971382360361589L;

	/**
	 * @param message
	 */
	public ClientException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public ClientException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ClientException(String message, Throwable cause) {
		super(message, cause);
	}

	public static ClientException create(Throwable cause) {
		if (cause instanceof ClientException) {
			return (ClientException) cause;
		}
		return new ClientException(cause);
	}
}
