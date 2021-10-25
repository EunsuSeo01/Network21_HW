import java.io.*;
import java.net.*;
import java.util.*;

public class DateServer {

	public static void main(String[] args) throws Exception {
		ServerSocket listener = new ServerSocket(59090);
		System.out.println("The date server is running...");
		
		while(true) {
			try (Socket socket = listener.accept()){
				var out = new PrintWriter(socket.getOutputStream(), true);
				out.println(new Date().toString());		// new Date().toString() = 현재 날짜.
			}
		}
	}

}
