package info.kuyur.justblog.servlet.filter;

import com.sun.jersey.api.model.AbstractMethod;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import com.sun.jersey.spi.container.ResourceFilter;
import com.sun.jersey.spi.container.ResourceFilterFactory;

import info.kuyur.justblog.utils.ClientMappableException;

import java.util.Collections;
import java.util.List;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.Response.Status;

public class RolesAllowedFilterFactory implements ResourceFilterFactory {

	private @Context SecurityContext sc;

	private class Filter implements ResourceFilter, ContainerRequestFilter {

		private final boolean denyAll;
		private final String[] rolesAllowed;

		protected Filter() {
			this.denyAll = true;
			this.rolesAllowed = null;
		}

		protected Filter(String[] rolesAllowed) {
			this.denyAll = false;
			this.rolesAllowed = (rolesAllowed != null) ? rolesAllowed : new String[] {};
		}

		@Override
		public ContainerRequestFilter getRequestFilter() {
			return this;
		}

		@Override
		public ContainerResponseFilter getResponseFilter() {
			return null;
		}

		@Override
		public ContainerRequest filter(ContainerRequest request) {
			if (!denyAll) {
				for (String role : rolesAllowed) {
					if (sc.isUserInRole(role))
						return request;
				}
			}

			throw new ClientMappableException("Forbidden.", Status.FORBIDDEN);
		}
	}

	@Override
	public List<ResourceFilter> create(AbstractMethod am) {
		// DenyAll on the method take precedence over RolesAllowed and PermitAll
		if (am.isAnnotationPresent(DenyAll.class))
			return Collections.<ResourceFilter> singletonList(new Filter());

		// RolesAllowed on the method takes precedence over PermitAll
		RolesAllowed ra = am.getAnnotation(RolesAllowed.class);
		if (ra != null)
			return Collections.<ResourceFilter> singletonList(new Filter(ra.value()));

		// PermitAll takes precedence over RolesAllowed on the class
		if (am.isAnnotationPresent(PermitAll.class))
			return null;

		// RolesAllowed on the class takes precedence over PermitAll
		ra = am.getResource().getAnnotation(RolesAllowed.class);
		if (ra != null)
			return Collections.<ResourceFilter> singletonList(new Filter(ra.value()));

		// No need to check whether PermitAll is present.
		return null;
	}
}
