package core.server;

import com.google.gson.annotations.SerializedName;

public enum RequestCode {
	@SerializedName("0")
	SESSION,
	
	@SerializedName("1")
	LOGIN,
	
	@SerializedName("2")
	TEAMDATA,
	
	@SerializedName("3")
	STOREDATA,
	
	@SerializedName("4")
	STOREACTION,
	
	@SerializedName("5")
	MATCH
}
