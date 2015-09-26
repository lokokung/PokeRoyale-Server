package core.server.session;

import java.util.Random;

import com.google.inject.Inject;

public class RandomSessionGen implements ISessionGen {
	
	private final Random r;
	private final String idUniverse;

	@Inject
	RandomSessionGen(
			Random r,
			@Session String idUniverse){
		this.r = r;
		this.idUniverse = idUniverse;
	}
	
	@Override
	public String generateSessionId() {
		String sessionId = "";
		while(sessionId.length() != 48){
			int i = r.nextInt(idUniverse.length());
			sessionId += idUniverse.charAt(i);
		}
		return sessionId;
	}

}
