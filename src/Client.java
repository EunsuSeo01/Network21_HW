/**
 * Create client program using socket.
 * @author user Eun Su Seo
 * last modified 2021-10-14
 */
import java.net.*;
import java.io.*;

public class Client {
	public static void main(String[] args) {
		// Declare variables for storing ip and port information.
		String IP = null;
		int port = 0;

		/* Client program have to read Server IP and port # from the file.
		 * So create the input stream to read a file. */
		String inputFileName = "serverInfo.txt";
		BufferedReader inputStream = null;

		try {
			inputStream = new BufferedReader(new FileReader(inputFileName));
			String[] serverInfo = inputStream.readLine().split(" ");

			// Store the file information.
			IP = serverInfo[0];
			port = Integer.parseInt(serverInfo[1]);
			
			inputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}

		// Declare variables for Socket Communication.
		Socket socket = null;
		BufferedReader inFromServer = null;
		BufferedWriter outToServer = null;
		BufferedReader stdin = null;
		
		try {
			// Create socket with the ip and port information.
			socket = new Socket(IP, port);

			/* Create BufferedReader, BufferedWriter
			 * to communicate with Server and to receive keyboard input from users. */
			inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			outToServer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			stdin = new BufferedReader(new InputStreamReader(System.in));

			// Outputs a message that the client and server are connected.
			System.out.println("- Connection to Server successful -");

			/* Start infinite loop
			 * to continue typing expression until you want to exit. */
			while (true) {
				// Get an (arithmetic) expression from client side.
				System.out.print("Enter an expression : ");
				String expression = stdin.readLine();

				// Send the expression to Server.
				outToServer.write(expression + "\n");
				outToServer.flush();

				// If the expression is not the exit command,
				if(!expression.equalsIgnoreCase("exit")) {
					// Wait for the response of Server.
					String inputMsg = inFromServer.readLine();
					// Display the response of Server.
					System.out.println(inputMsg);
				}
				// If the expression is the exit command,
				else {
					// Display the end message and escape the infinite loop.
					System.out.println("- Disconnection to Server -");
					break;
				}
			}
		} catch (IOException e) { // Print the error when a problem occurs.
			System.out.println(e.getMessage());
			e.printStackTrace();
		} finally {
			// Close all.
			try {
				if(inFromServer != null) inFromServer.close();
				if(outToServer != null) outToServer.close();
				if(stdin != null) stdin.close();
				if(socket != null) socket.close();
			}
			catch (IOException e) { // Print the error when there is a closing problem.
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
	}
}
