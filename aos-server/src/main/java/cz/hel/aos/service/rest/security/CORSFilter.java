package cz.hel.aos.service.rest.security;

import java.io.IOException;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;

@Provider
@Priority(Priorities.HEADER_DECORATOR)
public class CORSFilter implements ContainerResponseFilter {
	
	@Override
	public void filter(final ContainerRequestContext requestContext,
	                    final ContainerResponseContext cres) throws IOException {
		MultivaluedMap<String,Object> headers = cres.getHeaders();
		
		addHeader(headers, "Access-Control-Allow-Origin", "*");
		addHeader(headers, "Access-Control-Allow-Headers", "origin, content-type, accept, Authorization, X-Order, X-Filter, X-Password");
		addHeader(headers, "Access-Control-Allow-Credentials", "true");
		addHeader(headers, "Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
		addHeader(headers, "Access-Control-Max-Age", "1209600");
	}
	
	private void addHeader(MultivaluedMap<String, Object> headers, String key, String value) {
		if (headers.containsKey(key)) {
			headers.remove(key);
		}
		headers.add(key, value);
	}

}