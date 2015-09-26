package core.server.session;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.assistedinject.FactoryModuleBuilder;

import core.server.IRequestHandler;
import core.threads.ClientThread;
import core.threads.IClientFactory;

public class ServerSessionModule extends AbstractModule{

	@Override
	protected void configure() {
		bind(ISessionGen.class).to(RandomSessionGen.class);
		bind(IRequestHandler.class)
			.annotatedWith(Session.class)
			.to(SessionHandler.class);
		bind(SessionHandler.class);
		
		install(new FactoryModuleBuilder()
	     	.build(IClientFactory.class));
	}
	
	@Provides
	@Session
	long getTimeoutValue(){
		return 2700000;
	}
	
	@Provides
	@Session
	@Singleton
	Map<String, Long> provideSessionMap(){
		return new ConcurrentHashMap<String, Long>();
	}
	
	@Provides
	@Session
	@Singleton
	Map<String, ClientThread> provideThreadMap(){
		return new ConcurrentHashMap<String, ClientThread>();
	}
	
	// Random session generator setup.
	@Provides
	Random getRandomNumberGenerator(){
		Random r = new Random();
		r.setSeed(System.currentTimeMillis());
		return r;
	}
	
	@Provides
	@Session
	String getSessionIdUnivers(){
		String universe = "";
		for(char c = 'a'; c <= 'z'; ++c)
			universe += c;
		for(char c = 'A'; c <= 'Z'; ++c)
			universe += c;
		for(char c = '0'; c <= '9'; ++c)
			universe += c;
		return universe;
	}

}
