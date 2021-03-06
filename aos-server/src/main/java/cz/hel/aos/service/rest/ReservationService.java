package cz.hel.aos.service.rest;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJBException;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.security.auth.login.CredentialException;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import cz.hel.aos.Constants;
import cz.hel.aos.entity.dto.ReservationDTO;
import cz.hel.aos.service.ReservationManagement;
import cz.hel.aos.util.StringUtils;

@Path(Constants.RESERVATION_RESOURCE_PATH)
@ApplicationScoped
public class ReservationService {
	
	@Inject
	private ReservationManagement reservationManagement;

	@RolesAllowed({ "admin", "manager" })
	@Path("")
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public List<ReservationDTO> getAllReservations() {
		return reservationManagement.getAllReservations();
	}
	
	@PermitAll
	@Path("{id}")
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public ReservationDTO getReservation(@PathParam("id") Long id,
										  @HeaderParam("X-Password") String password) {

		if (id == null || id.equals(0L)) {
			throw new BadRequestException("Reservation ID cannot be empty.");
		}
		
		try {
			return reservationManagement.getReservation(id, password);
		} catch (CredentialException e) {
			throw new ForbiddenException();
		}
	}
	
	@PermitAll
	@Path("")
	@POST
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public ReservationDTO createFlight(ReservationDTO reservation) {
		try {
			return reservationManagement.createReservation(reservation);
			
			// TODO
		} catch (Exception e) {
			throw new BadRequestException(e.getMessage());
		}
	}
	
	@PermitAll
	@Path("{id}")
	@PUT
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public ReservationDTO updateFlight(@PathParam("id") Long id, 
										@HeaderParam("X-Password") String password,
										ReservationDTO reservation) {
		
		if (!id.equals(reservation.getId())) {
			throw new BadRequestException("Path and reservation ID must match.");
		}
		
		try {
			return reservationManagement.updateReservation(reservation, password);
		} catch (CredentialException e) {
			throw new ForbiddenException();
		} catch (EJBException e) {
			throw new BadRequestException(e.getMessage());
		}
	}
	
	@Path("{id}/{payment}")
	@POST
	public Response pay(@PathParam("id") Long id, @PathParam("payment") String payment) {
		try {
			reservationManagement.pay(id, payment);
		} catch (EJBException e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
		
		return Response.ok().build();
	}
	
	@RolesAllowed({ "admin", "manager" })
	@Path("{id}")
	@DELETE
	public Response deleteFlight(@PathParam("id") Long id) {
		
		try {
			reservationManagement.deleteReservation(id);
		} catch (EJBException e) {
			throw new BadRequestException(e.getMessage());
		}
		
		return Response.ok().build();
	}
	
	@PermitAll
	@Path("{id}/print")
	@GET
	public Response print(@PathParam("id") Long id, @QueryParam("email") String email) {
		if (StringUtils.isEmpty(email)) {
			throw new BadRequestException("An email has to be provided.");
		}
		reservationManagement.printReservation(id, email);
		
		return Response.ok().build();
	}
}
