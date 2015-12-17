package cz.hel.aos.service.rest.security;

import java.io.IOException;
import java.util.StringTokenizer;

import org.jboss.resteasy.util.Base64;

/**
 * Wrapper for Base64 encoded credentials.
 *
 */
public class CredentialsWrapper {

	private final String username;
	private final String password;

	public CredentialsWrapper(String authHeader) throws IOException {
		String encodedBasicAuth = authHeader.replaceFirst("Basic ", "");
		
		// Decode username and password
		final String usernameAndPassword = new String(
				Base64.decode(encodedBasicAuth));

		// Split username and password tokens
		final StringTokenizer tokenizer = new StringTokenizer(
				usernameAndPassword, ":");
		this.username = tokenizer.nextToken();
		this.password = tokenizer.nextToken();

	}

	public String getUsernameDecoded() {
		return username;
	}

	public String getPasswordDecoded() {
		return password;
	}

}
