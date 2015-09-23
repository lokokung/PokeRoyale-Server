package core.server;

public class RequestPacket {
	
	private RequestCode requestCode;
	private String packetData;
	private String sessionId;
	
	public RequestCode getRequestCode(){
		return requestCode;
	}
	
	public String getPacketData(){
		return packetData;
	}
	
	public String getSessionId(){
		return sessionId;
	}
}
