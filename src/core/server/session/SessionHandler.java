package core.server.session;

import java.util.Map;

import com.google.inject.Inject;

import core.server.IRequestHandler;
import core.server.RequestPacket;

public class SessionHandler implements IRequestHandler {
	
	private final Map<String, Long> sessionTimeouts;
	private final long timeout;
	private final ISessionGen sessionGen;

	@Inject
	SessionHandler(Map<String, Long> sessionTimeouts,
			long timeout, ISessionGen sessionGen){
		this.sessionTimeouts = sessionTimeouts;
		this.timeout = timeout;
		this.sessionGen = sessionGen;
	}
	
	@Override
	public String handlePacket(RequestPacket packet) {
		// Make sure that the request has no session.
		if(packet.getSessionId() != null)
			return null;
		
		String newSession = sessionGen.generateSessionId();
		while(sessionTimeouts.containsKey(newSession))
			newSession = sessionGen.generateSessionId();
		
		sessionTimeouts.put(newSession, 
				System.currentTimeMillis());
		return newSession;
	}
	
	public boolean sessionIsValid(String sessionId){
		long lastActivity = sessionTimeouts.get(sessionId);
		long difference = System.currentTimeMillis() - 
				lastActivity;
		if(difference > timeout){
			sessionTimeouts.remove(sessionId);
			return false;
		}
		sessionTimeouts.put(sessionId, 
				System.currentTimeMillis());
		return true;
	}

}
