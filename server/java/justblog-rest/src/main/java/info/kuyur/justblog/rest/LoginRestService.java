package info.kuyur.justblog.rest;

import info.kuyur.justblog.models.common.JustResult;
import info.kuyur.justblog.servlet.filter.AuthenticationFilter;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sun.jersey.spi.container.ResourceFilters;

@ResourceFilters({ AuthenticationFilter.class })
@Path("/login")
public class LoginRestService {

	private static final JustResult RESULT = new JustResult(true);

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public JustResult login() {
		return RESULT;
	}
}
