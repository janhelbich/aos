package cz.hel.aos.service.rest;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import cz.hel.aos.Constants;
import cz.hel.aos.entity.dto.FlightDTO;
import cz.hel.aos.service.FlightManagement;
import cz.hel.aos.service.ReferenceExistsException;
import cz.hel.aos.service.SimpleCriteria;

@Path(Constants.FLIGHT_RESOURCE_PATH)
@ApplicationScoped
public class FlightService {
	
	@Inject
	private FlightManagement flightManagement;

	@Path("")
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public List<FlightDTO> getAllFlights(@Context HttpHeaders headers) {
		return flightManagement.getAllFlights(RESTUtils.parseHeadersCriteria(headers));
	}
	
	@Path("{id}")
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public FlightDTO getFlight(@PathParam("id") Long id) {
		if (id == null || id.equals(0L)) {
			throw new BadRequestException("Fligh ID cannot be empty.");
		}
		
		return flightManagement.getFlight(id);
	}
	
	@Path("")
	@POST
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public FlightDTO createFlight(FlightDTO flight) {
		try {
			return flightManagement.createFlight(flight);
			
			// TODO
		} catch (Exception e) {
			throw new BadRequestException(e.getMessage());
		}
	}
	
	@Path("{id}")
	@PUT
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public FlightDTO updateFlight(@PathParam("id") Long id, FlightDTO flight) {
		if (id != null && !id.equals(flight.getId())) {
			throw new BadRequestException("Path and destination ID must match.");
		}
		
		try {
			return flightManagement.updateFlight(flight);
			
			// TODO
		} catch (Exception e) {
			throw new BadRequestException(e.getMessage());
		}
	}
	
	@Path("{id}")
	@DELETE
	public Response deleteFlight(@PathParam("id") Long id) {
		try {
			flightManagement.deleteFlight(id);
		} catch (ReferenceExistsException e) {
			throw new BadRequestException(
					"Cannot delete flight because some objects still reference it.", 
					Response.status(Response.Status.EXPECTATION_FAILED).build());
		}
		
		return Response.ok().build();
	}
	
}
