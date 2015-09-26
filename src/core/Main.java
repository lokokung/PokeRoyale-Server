package core;

import java.io.IOException;

import com.google.inject.Guice;
import com.google.inject.Injector;

import core.db.DBModule;
import core.server.POROServer;
import core.server.ServerModule;
import core.server.login.ServerLoginModule;
import core.server.session.ServerSessionModule;

public class Main {
	private static Injector injector;
	
	public static void main(String[] args) throws IOException{
		injector = Guice.createInjector(
				new DBModule(),
				new ServerModule(),
				new ServerSessionModule(),
				new ServerLoginModule());
		
		POROServer server = injector.getInstance(POROServer.class);
		server.start();
	}
}
