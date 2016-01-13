package cz.hel.aos.service.webservice;

import javax.jws.WebMethod;
import javax.jws.WebService;

import cz.hel.aos.entity.Reservation;

@WebService
public interface PrintingService {
	
	@WebMethod
	public String printReservation(Reservation res);
}
