package core.server;

public interface IRequestHandler {
	public String handlePacket(RequestPacket packet);
}
