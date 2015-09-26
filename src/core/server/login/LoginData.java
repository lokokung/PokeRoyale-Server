package core.server.login;

public class LoginData {
	
	private final String username;
	private final String password;
	
	LoginData(String username, String password){
		this.username = username;
		this.password = password;
	}
	
	public String getUsername(){
		return username;
	}
	
	public String getPassword(){
		return password;
	}
	
}
