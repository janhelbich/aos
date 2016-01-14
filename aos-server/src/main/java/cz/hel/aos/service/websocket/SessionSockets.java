package cz.hel.aos.service.websocket;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/sockets")
public class SessionSockets {
	
	private static final Set<Session> sessions = Collections.synchronizedSet(new HashSet<Session>());
	
	@OnOpen
	public void onOpen(Session session) {
		System.out.println("opening websocket session: " + session.getId());
		sessions.add(session);
		notifyListeners();
	}
	
	@OnClose
	public void onClose(Session session, CloseReason reason) {
		System.out.println("closing websocket session reason: " + reason.getReasonPhrase());
		System.out.println("closing websocket session code: " + reason.getCloseCode());
		sessions.remove(session);
		notifyListeners();
	}
	
	@OnError
	public void onError(Throwable t, Session session) {
		System.out.println("error websocket session: " + session.getId());
		sessions.remove(session);
		notifyListeners();
	}

	private void notifyListeners() {
		Iterator<Session> i = sessions.iterator();
		while (i.hasNext()) {
			Session s = i.next();
			try {
				s.getBasicRemote().sendText(sessions.size() + "");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
