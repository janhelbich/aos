package cz.hel.aos.service.rest.security;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.jboss.resteasy.core.ResourceMethodInvoker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.hel.aos.service.UserManagement;

@Provider
public class SecurityInterceptor implements ContainerRequestFilter {
	
	private static final Logger logger = LoggerFactory.getLogger(SecurityInterceptor.class);
	
	private static final String AUTHORIZATION_PROPERTY = "Authorization";
	private static final String INVOKER_PROPERTY = "org.jboss.resteasy.core.ResourceMethodInvoker";
	private static final Response ACCESS_DENIED = Response.status(Response.Status.UNAUTHORIZED).build();
	private static final Response ACCESS_FORBIDDEN = Response.status(Response.Status.FORBIDDEN).build();

	@Inject
	private UserManagement userManagement;
	
	@Override
	public void filter(ContainerRequestContext ctx) {
		ResourceMethodInvoker methodInvoker = (ResourceMethodInvoker) ctx.getProperty(INVOKER_PROPERTY);
		Method method = methodInvoker.getMethod();
		
		// Access allowed for all
		if (method.isAnnotationPresent(PermitAll.class)) {
			return;
		}
		// Access denied for all
		if (method.isAnnotationPresent(DenyAll.class)) {
			ctx.abortWith(ACCESS_FORBIDDEN);
			return;
		}

		// Get request headers
		final MultivaluedMap<String, String> headers = ctx.getHeaders();
		// Fetch authorization header
		final List<String> authorization = headers.get(AUTHORIZATION_PROPERTY);

		// If no authorization information present; block access
		if (authorization == null || authorization.isEmpty()) {
			ctx.abortWith(ACCESS_DENIED);
			return;
		}

		// Get encoded username and password
		final String authHeader = authorization.get(0);
		
		CredentialsWrapper cw = null;
		try {
			cw = new CredentialsWrapper(authHeader);
		} catch (Exception e) {
			ctx.abortWith(ACCESS_DENIED);
			return;
		}

		// Verify user access
		if (method.isAnnotationPresent(RolesAllowed.class)) {
			RolesAllowed allowed = method.getAnnotation(RolesAllowed.class);
			Set<String> rolesAllowed = new HashSet<String>(Arrays.asList(allowed.value()));

			// Is user valid?
			if (!isUserAllowed(cw.getUsernameDecoded(), cw.getPasswordDecoded(), rolesAllowed)) {
				ctx.abortWith(ACCESS_DENIED);
				return;
			}
		}
	}

	private boolean isUserAllowed(	final String username,
									final String password,
									final Set<String> rolesAllowed) {
		try {
			return userManagement.isUserAllowed(username, password, rolesAllowed);
		} catch (Exception e) {
			logger.error("An error occurred while trying to authorize user {0}.", username, e);
			return false;
		}
	}

}