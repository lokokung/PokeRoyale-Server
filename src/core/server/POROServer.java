package core.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import com.google.inject.Inject;
import com.google.inject.Provider;

import core.threads.ClientThread;
import core.threads.IClientFactory;

public class POROServer {
	
	private final ServerSocket server;
	private final Provider<IClientFactory> provider;
	
	private volatile boolean isRunning = true;
	
	@Inject
	POROServer(
			ServerSocket server,
			Provider<IClientFactory> provider){
		this.server = server;
		this.provider = provider;
		
	}
	
	public void start() throws IOException{
		if(server == null)
			return;
		System.out.println("Successfully started server.");
		while(isRunning){
            Socket clientSocket = server.accept();
            System.out.println("Connection with: " + clientSocket.getInetAddress());
            PrintWriter out =
    		    new PrintWriter(clientSocket.getOutputStream(), true);
    		BufferedReader in = new BufferedReader(
    		    new InputStreamReader(clientSocket.getInputStream()));
    		
    		IClientFactory cFactory = provider.get();
    		ClientThread cThread = cFactory.create(clientSocket, out, in);
    		cThread.run();
        }
	}
}
