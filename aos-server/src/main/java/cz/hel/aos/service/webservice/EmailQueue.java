package cz.hel.aos.service.webservice;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@MessageDriven(
	mappedName = "java:global/jms/myQueue",
	activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
	    @ActivationConfigProperty(propertyName="destination", propertyValue="java:global/jms/myQueue")
	}
)
public class EmailQueue implements MessageListener {

	private static final Logger logger = LoggerFactory.getLogger(EmailQueue.class);

	@Override
	public void onMessage(Message message) {
		try {
			// Send an email (print its contents to console)
			EmailMessage em = message.getBody(EmailMessage.class);

			logger.info("Received message in EmailQueue, sending to: " + em.getEmail());
			logger.info(em.getContent());
			
		} catch (JMSException ex) {
			logger.warn("Cannot retrieve message from JMS Queue");
		}
	}
}