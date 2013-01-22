package info.kuyur.justblog.servlet.filter;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import com.sun.jersey.spi.container.ResourceFilter;

public class AuthenticationFilter implements ResourceFilter, ContainerRequestFilter {

	@Override
	public ContainerRequest filter(ContainerRequest request) {
		// TODO Auto-generated method stub
		return request;
	}

	@Override
	public ContainerRequestFilter getRequestFilter() {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public ContainerResponseFilter getResponseFilter() {
		// TODO Auto-generated method stub
		return null;
	}

}
