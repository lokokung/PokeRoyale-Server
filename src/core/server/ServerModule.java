package core.server;

import java.util.Random;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;

import core.server.session.ISessionGen;
import core.server.session.RandomSessionGen;

public class ServerModule extends AbstractModule{

	@Override
	protected void configure() {
		bind(ISessionGen.class).to(RandomSessionGen.class);
		
	}
	
	@Provides
	Random getRandomNumberGenerator(){
		Random r = new Random();
		r.setSeed(System.currentTimeMillis());
		return r;
	}
	
	@Provides
	@Named("Universe")
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
