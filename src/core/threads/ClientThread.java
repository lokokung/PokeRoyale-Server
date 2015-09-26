package core.threads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import core.server.IRequestHandler;
import core.server.RequestPacket;
import core.server.login.Login;
import core.server.session.SessionHandler;

public class ClientThread extends Thread{
	
	private final Gson gson;
	private final SessionHandler sH;
	private final IRequestHandler lH;
	
	private final Socket socket;
	private final PrintWriter out;
	private final BufferedReader in;
	
	private volatile boolean keepAlive = true;
	private String sessionId = null;
	
	@Inject
	ClientThread(
			Gson gson,
			SessionHandler sH,
			@Login IRequestHandler lH,
			@Assisted Socket socket,
			@Assisted PrintWriter out,
			@Assisted BufferedReader in){
		this.gson = gson;
		this.sH = sH;
		this.lH = lH;
		
		this.socket = socket;
		this.out = out;
		this.in = in;
	}
	
	@Override
	public void run(){
		while(keepAlive){
			String requestJson = null;
			try {
				requestJson = in.readLine();
			} catch (IOException e) {
			}
			
			if(requestJson != null){
				// Parse request and then execute.
				RequestPacket request = 
						gson.fromJson(requestJson, 
								RequestPacket.class);
				
				switch(request.getRequestCode()){
					case SESSION:
						sessionId = sH.handlePacket(request);
						out.println(sessionId + "\n");
						System.out.println(sessionId);
						sH.mapSessionToThread(sessionId, this);
						break;
					case LOGIN:
						if(sH.sessionIsValid(request.getSessionId())){
							String loginFlag = lH.handlePacket(request);
							out.println(loginFlag + "\n");
						}
						else{
							out.println("false\n");
						}
						break;
					default:
						break;
				}
			}
			else{
				sH.endSession(sessionId);
				sessionId = null;
			}
		}
	}
	
	public void stopThread(){
		keepAlive = false;
		try{
			in.close();
			out.close();
			socket.close();
		}
		catch(Exception e){
		}
		
	}
	
}
