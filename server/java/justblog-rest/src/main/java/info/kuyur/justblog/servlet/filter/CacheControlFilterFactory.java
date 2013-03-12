package info.kuyur.justblog.servlet.filter;

import java.util.Collections;
import java.util.List;

import javax.ws.rs.core.HttpHeaders;

import com.sun.jersey.api.model.AbstractMethod;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import com.sun.jersey.spi.container.ResourceFilter;
import com.sun.jersey.spi.container.ResourceFilterFactory;

public class CacheControlFilterFactory implements ResourceFilterFactory {

	private static final List<ResourceFilter> NO_CACHE_FILTER = Collections.<ResourceFilter>singletonList(new CacheResponseFilter("no-cache"));

	@Override
	public List<ResourceFilter> create(AbstractMethod am) {
		CacheControlHeader cch = am.getAnnotation(CacheControlHeader.class);
		if (cch == null) {
			return NO_CACHE_FILTER;
		}
		return Collections.<ResourceFilter>singletonList(new CacheResponseFilter(cch.value()));
	}

	private static class CacheResponseFilter implements ResourceFilter, ContainerResponseFilter {
		private final String headerValue;

		public CacheResponseFilter(String headerValue) {
			this.headerValue = headerValue;
		}

		@Override
		public ContainerResponse filter(ContainerRequest request, ContainerResponse response) {
			response.getHttpHeaders().putSingle(HttpHeaders.CACHE_CONTROL, headerValue);
			return response;
		}

		@Override
		public ContainerRequestFilter getRequestFilter() {
			return null;
		}

		@Override
		public ContainerResponseFilter getResponseFilter() {
			return this;
		}
		
	}
}
