package info.kuyur.justblog.rest;

import java.util.List;

import info.kuyur.justblog.models.user.User;
import info.kuyur.justblog.models.user.UserRole;
import info.kuyur.justblog.services.UserService;
import info.kuyur.justblog.servlet.filter.AuthenticationFilter;
import info.kuyur.justblog.utils.ClientMappableException;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import com.sun.jersey.spi.container.ResourceFilters;

@ResourceFilters({ AuthenticationFilter.class })
@Path("/user")
public class UserRestService {

	@GET
	@RolesAllowed("admin")
	@Produces(MediaType.APPLICATION_JSON)
	public List<User> getAllUsers() {
		return new UserService().getAllUsers();
	}

	@POST
	@RolesAllowed("admin")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public User createUser() {
		// TODO
		return null;
	}

	@DELETE
	@Path("/{userid}")
	//@RolesAllowed("admin")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean deleteUser(@PathParam("userid")Long userId,
			@Context SecurityContext sc) {
		if (sc.isUserInRole("admin")) {
			return new UserService().deleteUser(userId);
		} else {
			throw new ClientMappableException("No authority");
		}
	}

	@PUT
	@Path("/{userid}")
	@RolesAllowed("reader")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public User updateUser(User user) {
		return null;
	}

	@PUT
	@Path("/{userid}/role/{role}")
	@RolesAllowed("admin")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean changeAuthLevel(@PathParam("userid")Long userId,
			@PathParam("role") UserRole level) {
		// TODO
		return false;
	}

	@PUT
	@Path("/{userid}/password")
	@RolesAllowed("reader")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean resetPassword(@PathParam("userid")Long userId,
			@QueryParam("pass")String encryptedPass) {
		// TODO
		return false;
	}
}