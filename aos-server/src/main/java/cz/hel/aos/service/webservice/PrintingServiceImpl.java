package cz.hel.aos.service.webservice;

import javax.jws.WebService;

import cz.hel.aos.entity.Reservation;

@WebService
public class PrintingServiceImpl implements PrintingService {

	private static final String NEWLINE = "\n";

	@Override
	public String printReservation(Reservation res) {
		StringBuilder sb = new StringBuilder();
		sb.append("Rezervace cislo. ").append(res.getId()).append(NEWLINE);
		sb.append("-------------------------------------").append(NEWLINE);
		sb.append("Pocet mist: ").append(res.getNumberOfSeats()).append(NEWLINE);
		sb.append("Stav rezervace: ").append(res.getState()).append(NEWLINE);
		sb.append(NEWLINE);
		sb.append("-------------------------------------").append(NEWLINE);
		sb.append("Informace o letu").append(NEWLINE);
		sb.append("-------------------------------------").append(NEWLINE);
		sb.append("Nazev letu: ").append(res.getFlight().getFlightName()).append(NEWLINE);
		
		
		return sb.toString();
	}
}
