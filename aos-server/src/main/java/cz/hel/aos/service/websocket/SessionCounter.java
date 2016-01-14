package cz.hel.aos.service.websocket;

import java.util.HashSet;
import java.util.Set;

public class SessionCounter {

	private final Set<String> sessionIds;
	
	private SessionCounter() {
		sessionIds = new HashSet<>();
	}
	
	private static class SessionCounterHolder {
		private static final SessionCounter INSTANCE = new SessionCounter();
	}
	
	public static SessionCounter getInstance() {
		return SessionCounterHolder.INSTANCE;
	}
	
	public int getSize() {
		return sessionIds.size();
	}
	
	public void add(String id) {
		if (!sessionIds.contains(id)) {
			synchronized (SessionCounter.class) {
				sessionIds.add(id);
			}
		}
	}
	
	public void remove(String id) {
		if (sessionIds.contains(id)) {
			synchronized (SessionCounter.class) {
				sessionIds.remove(id);
			}
		}
	}

}
