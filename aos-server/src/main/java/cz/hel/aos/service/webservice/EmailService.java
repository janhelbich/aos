package cz.hel.aos.service.webservice;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;

import cz.hel.aos.entity.Reservation;

@WebService
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL)
public interface EmailService {
	
	public void sendEmailReservation(String email, Reservation reservation);

}
