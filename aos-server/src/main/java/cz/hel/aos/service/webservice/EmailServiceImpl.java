package cz.hel.aos.service.webservice;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.Queue;
import javax.jws.WebService;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.hel.aos.entity.Flight;
import cz.hel.aos.entity.Reservation;
import cz.hel.aos.entity.Reservation.ReservationState;

@WebService(endpointInterface = "cz.hel.aos.service.webservice.EmailService")
public class EmailServiceImpl implements EmailService {
	
	private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);
	
	@Inject
    private JMSContext context;
    
    @Resource(mappedName="java:global/jms/myQueue")
    private Queue queue;
    
	@Override
	public void sendEmailReservation(String email, Reservation reservation) {
		String eticket = invokePrintingService(reservation);
		EmailMessage msg = new EmailMessage(email, eticket);
		context.createProducer().send(queue, msg);
	}
	
	private String invokePrintingService(Reservation res) {
		try {
			URL url = new URL("http://localhost:8080/aos-server/PrintingServiceImpl?wsdl");
			QName qname = new QName("http://webservice.service.aos.hel.cz/", "PrintingServiceImplService");
			Service service = Service.create(url, qname);
			PrintingService ps = service.getPort(PrintingService.class);
			return ps.printReservation(res);
		} catch (IOException e) {
			logger.error("An error occurred while invoking printing service.", e);
			return null;
		}
	}

	// pro testovani
	public static void main(String[] args) throws Exception {
		method1();
		method2();
	}

	private static void method2() {
		System.out.println(new EmailServiceImpl().invokePrintingService(getTestReservation()));
	}

	private static void method1() throws MalformedURLException {
		URL url = new URL("http://localhost:8080/aos-server/EmailServiceImpl?wsdl");
		QName qname = new QName("http://webservice.service.aos.hel.cz/", "EmailServiceImplService");
		Service service = Service.create(url, qname);
		EmailService es = service.getPort(EmailService.class);
		es.sendEmailReservation("janhelbich@gmail.com", getTestReservation());
	}

	private static Reservation getTestReservation() {
		Flight flight = new Flight();
		flight.setId(3544L);
		flight.setFlightName("Testovaci let");
		
		Reservation res = new Reservation();
		res.setId(23432L);
		res.setCreatedAt(new Date());
		res.setState(ReservationState.PAID);
		res.setFlight(flight);
		return res;
	}
}
