package core.server;

import java.io.IOException;
import java.net.ServerSocket;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

public class ServerModule extends AbstractModule{

	@Override
	protected void configure() {
		
	}
	
	@Provides
	@Singleton
	Gson provideGson(){
		GsonBuilder gsonBuilder = new GsonBuilder();
		return gsonBuilder.serializeNulls().create();
	}
	
	@Provides
	@Singleton
	ServerSocket provideServerSocket(){
		try {
			ServerSocket serverSocket = new ServerSocket(5000);
			return serverSocket;
		} catch (IOException e) {
			System.out.println("Unable to bind to designated port.");
		}
		return null;
	}

}
