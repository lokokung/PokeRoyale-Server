package core.server;

import com.google.inject.Inject;

public class POROServer {
	
	private final int port;
	private final int maxClients;
	
	@Inject
	POROServer(
			int port,
			int maxClients){
		this.port = port;
		this.maxClients = maxClients;
	}
}
