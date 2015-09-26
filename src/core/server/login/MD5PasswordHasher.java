package core.server.login;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class MD5PasswordHasher implements IPasswordHasher {
	
	private final Provider<StringBuffer> sbProvider;
	
	@Inject
	MD5PasswordHasher(
			Provider<StringBuffer> sbProvider){
		this.sbProvider = sbProvider;
	}
	
	@Override
	public String hash(String arg0) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(md == null){
			return null;
		}
		
		md.update(arg0.getBytes());
		byte[] digest = md.digest();
		StringBuffer sb = sbProvider.get();
		for (byte b : digest) {
			sb.append(String.format("%02x", b & 0xff));
		}
		return sb.toString();
	}

}
