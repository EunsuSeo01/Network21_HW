import java.util.Scanner;
import java.net.*;
import java.io.*;

public class DateClient {
	public static void main(String[] args) throws IOException {
		Socket socket = new Socket("127.0.0.1", 59090);
		Scanner in = new Scanner(socket.getInputStream());
		System.out.println("Server response: " + in.nextLine());
	}
}
 