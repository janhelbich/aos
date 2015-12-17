package cz.hel.aos.service.rest;

import javax.annotation.security.PermitAll;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.hel.aos.service.UserManagement;
import cz.hel.aos.service.rest.security.CredentialsWrapper;

@Path("/signUp")
@ApplicationScoped
public class LoginService {
	
	private static final Logger logger = LoggerFactory.getLogger(LoginService.class);
	
	@Inject
	private UserManagement userManagement;

	@PermitAll
	@Path("")
	@POST
	public Response signUp(@DefaultValue("") @HeaderParam("Authorization") String authHeader) {
		
		CredentialsWrapper cw = null;
		try {
			cw = new CredentialsWrapper(authHeader);
		} catch (Exception e) {
			logger.warn("Bad credentials format: {0}.", e.getMessage());
			throw new BadRequestException("Credentials were provided in invalid format.");
		}

		if (userManagement.checkCredentials(cw.getUsernameDecoded(), cw.getPasswordDecoded()) != null) {
			return Response.ok().build();
		} else {
			throw new NotAuthorizedException(Response.status(Response.Status.UNAUTHORIZED).build());
		}
	} 
}
