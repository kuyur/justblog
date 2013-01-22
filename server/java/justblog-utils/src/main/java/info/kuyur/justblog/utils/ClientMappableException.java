package info.kuyur.justblog.utils;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class ClientMappableException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4877877702393108407L;

	private final Response response;

	public static ClientMappableException create(String message, Throwable cause) {
		if (cause instanceof ClientMappableException) {
			return (ClientMappableException) cause;
		}
		return new ClientMappableException(message, cause);
	}

	private ClientMappableException(String message, Throwable cause) {
		super(message, cause);
		response = Response.status(Status.INTERNAL_SERVER_ERROR).entity(message).type(MediaType.TEXT_PLAIN).build();
	}

	public ClientMappableException(String message) {
		this(message, Status.INTERNAL_SERVER_ERROR);
	}

	public ClientMappableException(String message, Status status) {
		super(message);
		response = Response.status(status).entity(message).type(MediaType.TEXT_PLAIN).build();
	}

	public Response getResponse() {
		return response;
	}
}
