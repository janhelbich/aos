package cz.hel.aos.service.rest;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.spi.BadRequestException;

import cz.hel.aos.Constants;
import cz.hel.aos.entity.Destination;
import cz.hel.aos.service.DestinationManagement;
import cz.hel.aos.service.ReferenceExistsException;

@Path(Constants.DESTINATION_RESOURCE_PATH)
@ApplicationScoped
public class DestinationService {
	
	@Inject
	private DestinationManagement destinationManagement;
	
	@PermitAll
	@Path("")
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public List<Destination> getDestinations() {
		return destinationManagement.getAllDestinations();
	}

	@PermitAll
	@Path("{id}")
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Destination getDestination(@PathParam("id") String id) {
		if (id == null || "".equals(id)) {
			throw new BadRequestException("Destination ID cannot be empty.");
		}
		
		return destinationManagement.getDestination(id);
	}
	
	@RolesAllowed({ "admin" })
	@Path("")
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Destination createDestination(Destination destination) {
		try {
			return destinationManagement.createDestination(destination);
			
		// TODO
		} catch (Exception e) {
			throw new BadRequestException(e.getMessage());
		}
	}
	
	@RolesAllowed({ "admin" })
	@Path("{id}")
	@PUT
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Destination updateDestination(@PathParam("id") Long id, Destination destination) {
		if (id != null && !id.equals(destination.getId())) {
			throw new BadRequestException("Path and destination ID must match.");
		}
		
		try {
			return destinationManagement.updateDestination(destination);
			
		// TODO
		} catch (Exception e) {
			throw new BadRequestException(e.getMessage());
		}
	}
	
	@RolesAllowed({ "admin" })
	@Path("{id}")
	@DELETE
	public Response deleteDestination(@PathParam("id") String id) {
		if (id == null || "".equals(id)) {
			throw new BadRequestException("Destination ID to delete cannot be empty.");
		}
		
		try {
			destinationManagement.deleteDestination(id);
		} catch (ReferenceExistsException e) {
			throw new BadRequestException(
					"Cannot delete destination because some objects still reference it.", 
					Response.status(Response.Status.EXPECTATION_FAILED).build());
		} catch (Exception e) {
			throw new ServerErrorException(Response.Status.INTERNAL_SERVER_ERROR);
		}
		
		return Response.ok().build();
	}
}
