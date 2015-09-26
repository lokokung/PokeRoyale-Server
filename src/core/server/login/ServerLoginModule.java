package core.server.login;

import com.google.inject.AbstractModule;

import core.server.IRequestHandler;

public class ServerLoginModule extends AbstractModule{

	@Override
	protected void configure() {
		bind(IPasswordHasher.class).to(MD5PasswordHasher.class);
		bind(IRequestHandler.class).annotatedWith(Login.class)
			.to(LoginHandler.class);
	}

}
