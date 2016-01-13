package cz.hel.aos.service.webservice;

import java.net.URL;

import javax.jws.WebService;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import cz.hel.aos.entity.Reservation;

@WebService(endpointInterface = "cz.hel.aos.service.webservice.EmailService")
public class EmailServiceImpl implements EmailService {

	@Override
	public void sendEmailReservation(String email, Reservation reservation) {
		System.out.println("---EmailServiceImpl ahoj: " + email);
	}

	// klient pro testovani
	public static void main(String[] args) throws Exception {
		URL url = new URL("http://localhost:8080/aos-server/EmailServiceImpl?wsdl");
		QName qname = new QName("http://webservice.service.aos.hel.cz/", "EmailServiceImplService");
		Service service = Service.create(url, qname);
		EmailService es = service.getPort(EmailService.class);
		es.sendEmailReservation("janhelbich@gmail.com", null);
	}
}
