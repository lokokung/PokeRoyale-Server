package core.server.session;

import java.util.Map;

import com.google.inject.Inject;

import core.server.IRequestHandler;
import core.server.RequestPacket;
import core.threads.ClientThread;

public class SessionHandler implements IRequestHandler {
	
	private final Map<String, Long> sessionTimeouts;
	private final Map<String, ClientThread> threadMap;
	private final long timeout;
	private final ISessionGen sessionGen;

	@Inject
	SessionHandler(
			@Session Map<String, Long> sessionTimeouts,
			@Session Map<String, ClientThread> threadMap,
			@Session long timeout, ISessionGen sessionGen){
		this.sessionTimeouts = sessionTimeouts;
		this.threadMap = threadMap;
		this.timeout = timeout;
		this.sessionGen = sessionGen;
	}
	
	@Override
	public String handlePacket(RequestPacket packet) {
		String newSession = sessionGen.generateSessionId();
		while(sessionTimeouts.containsKey(newSession))
			newSession = sessionGen.generateSessionId();
		
		sessionTimeouts.put(newSession, 
				System.currentTimeMillis());
		return newSession;
	}
	
	public boolean sessionIsValid(String sessionId){
		long lastActivity = sessionTimeouts.getOrDefault(sessionId, (long) -1);
		if(lastActivity == -1){
			return false;
		}
		long difference = System.currentTimeMillis() - 
				lastActivity;
		if(difference > timeout){
			sessionTimeouts.remove(sessionId);
			ClientThread sessionThread = threadMap.get(sessionId);
			if(sessionThread != null)
				sessionThread.stopThread();
			return false;
		}
		sessionTimeouts.put(sessionId, 
				System.currentTimeMillis());
		return true;
	}
	
	public void mapSessionToThread(String session, ClientThread client){
		threadMap.put(session, client);
	}
	
	public void endSession(String session){
		ClientThread sessionThread = threadMap.get(session);
		if(sessionThread != null){
			sessionThread.stopThread();
			threadMap.remove(session);
		}
	}

}
