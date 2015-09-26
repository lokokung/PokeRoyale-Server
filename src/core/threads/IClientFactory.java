package core.threads;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public interface IClientFactory {
	ClientThread create(Socket socket, PrintWriter out, BufferedReader in);
}
