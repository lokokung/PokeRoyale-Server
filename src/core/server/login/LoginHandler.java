package core.server.login;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.Provider;

import core.server.IRequestHandler;
import core.server.RequestPacket;

public class LoginHandler implements IRequestHandler {

	private final Gson gson;
	private final IPasswordHasher hasher;
	private final Provider<PreparedStatement> sqlProvider;
	
	@Inject
	LoginHandler(
			Gson gson, IPasswordHasher hasher,
			@Login Provider<PreparedStatement> sqlProvider){
		this.gson = gson;
		this.hasher = hasher;
		this.sqlProvider = sqlProvider;
	}

	@Override
	public String handlePacket(RequestPacket packet) {
		LoginData loginData = gson.fromJson(packet.getPacketData(), LoginData.class);
		System.out.println(loginData.getUsername());
		System.out.println(loginData.getPassword());
		PreparedStatement loginStatement = sqlProvider.get();
		try {
			loginStatement.setString(1, loginData.getUsername());
			ResultSet results = loginStatement.executeQuery();
			results.next();
			
			String hashedPassword = results.getString("Password");
			System.out.println(hashedPassword);
			String prepareSecondHash = packet.getSessionId() + hashedPassword + packet.getSessionId();
			String hashed = hasher.hash(prepareSecondHash);
			System.out.println(hashed);
			if(hashed.equals(loginData.getPassword()))
					return "true";
			return "false";
			
		} catch (SQLException e) {
			return "false";
		}
	}

}
