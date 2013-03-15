package info.kuyur.justblog.rest;

import java.util.List;

import info.kuyur.justblog.models.user.User;
import info.kuyur.justblog.models.user.UserRole;
import info.kuyur.justblog.services.UserService;
import info.kuyur.justblog.servlet.filter.AuthenticationFilter;

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
	public User createUser(User user) {
		// TODO
		return null;
	}

	@PUT
	@RolesAllowed("reader")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public User updateSelf(@Context SecurityContext sc, User user) {
		// TODO check user
		user.setAccount(sc.getUserPrincipal().getName());
		// TODO
		return null;
	}

	@PUT
	@Path("/{userid}")
	@RolesAllowed("admin")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public User updateUser(User user) {
		// TODO
		return null;
	}

	@DELETE
	@Path("/{userid}")
	@RolesAllowed("admin")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean deleteUser(@PathParam("userid")Long userId) {
		return new UserService().deleteUser(userId);
	}

	@PUT
	@Path("/{userid}/role/{role}")
	@RolesAllowed("admin")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean changeAuthLevel(@PathParam("userid")Long userId,
			@PathParam("role") String newLevel) {
		UserRole newRole = UserRole.toUserRole(newLevel);
		if (newRole == null) {
			return false;
		}
		// TODO
		return true;
	}

	@PUT
	@Path("/password")
	@RolesAllowed("reader")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean updatePassword(@Context SecurityContext sc,
			@QueryParam("hash")String encryptedMixedHash) {
		return new UserService().updatePassword(sc.getUserPrincipal().getName(), encryptedMixedHash);
	}
}