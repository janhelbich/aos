package cz.hel.aos.service.webservice;

import java.io.Serializable;

public class EmailMessage implements Serializable {
	
	private static final long serialVersionUID = -8158797628889932619L;
	
	private final String email;
	private final String content;

	public EmailMessage(String email, String content) {
		this.email = email;
		this.content = content;
	}

	public String getEmail() {
		return email;
	}

	public String getContent() {
		return content;
	}

}
