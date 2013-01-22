package info.kuyur.justblog.stateful.servlet;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class RuntimeExceptionMapper implements ExceptionMapper<RuntimeException> {

	private static final String DEFAULT_MESSAGE = "Unhandled exception happens.";

	@Override
	public Response toResponse(RuntimeException e) {
		if (e instanceof WebApplicationException) {
			WebApplicationException wae = (WebApplicationException) e;
			return wae.getResponse();
		} else {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
			.entity(DEFAULT_MESSAGE).type(MediaType.TEXT_PLAIN).build();
		}
	}
}
