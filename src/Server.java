/**
 * Create Server program using socket.
 * @author user Eun Su Seo
 * last modified 2021-10-14
 */
import java.net.*;
import java.io.*;

public class Server {
	public static void main(String[] args) {
		int port = 0;
		
		/* Server program have to read port # from the file.
		 * So create the input stream to read a file. */
		String inputFileName = "serverInfo.txt";
		BufferedReader inputStream = null;

		try {
			inputStream = new BufferedReader(new FileReader(inputFileName));
			String[] serverInfo = inputStream.readLine().split(" ");

			// Store the port #.
			port = Integer.parseInt(serverInfo[1]);
			
			inputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		// Declare object type variables for Socket Communication.
		ServerSocket listener = null;
		Socket connectionSocket = null;
		
		try {
			// Create listener ServerSocket that always listen to the request from clients.
			listener = new ServerSocket(port);
			System.out.println("The Account Server is running...");
			
			// Create Account object.
			Account a = new Account();

			// Infinite Loop to receive expressions from multiple clients.
			while (true) {
				// Wait for Client to send the expression.
				connectionSocket = listener.accept();

				/* Process Client requests with Thread.
				 * -> To support multiple users by threads.
				 */
				AccountThread thread = new AccountThread(connectionSocket, a);
				thread.start();
			}
		} catch (IOException e) { // Print the error when a problem occurs.
			System.out.println(e.getMessage());
			e.printStackTrace();
		} finally {
			// Close sockets.
			try {
                if (connectionSocket != null) connectionSocket.close();
                if (listener != null) listener.close();
			} catch (IOException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
	}

}

// Thread Class.
class AccountThread extends Thread {
	// Declare variables for Socket Communication.
	Socket connectionSocket = null;
	BufferedReader inFromClient = null;
	BufferedWriter outToClient = null;

	// Declare reference type variable for checking the Account object.
	Account a = null;
	
	public AccountThread(Socket connectionSocket, Account a) {
		// If Socket and Account are correct, set it to value.
		if(connectionSocket != null && a != null) {
			this.connectionSocket = connectionSocket;
			this.a = a;
		}
		// Otherwise, the thread is interrupted!
		else {
			System.out.println("Invalid!");
			this.interrupt();
		}
	}

	public void run() {
		// To distinguish which client it is when running multiple threads.
		String clientInfo = "[" + connectionSocket.getInetAddress() + ":" + connectionSocket.getPort() + "]";

		try {
			// Create BufferedReader, BufferedWriter to communicate with Client.
			inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
			outToClient = new BufferedWriter(new OutputStreamWriter(connectionSocket.getOutputStream()));

			// Print the message that successfully connected with Client.
			System.out.println(clientInfo + " - Connection to Client successful.");
			
			// Infinite Loop for continuous message response until Client sends 'exit' expression.
			while (true) {
				// Read an expression sent from the client.
				String inputMsg = inFromClient.readLine();

				// If the received expression is not the 'exit',
				if (!inputMsg.equalsIgnoreCase("exit")) {
					System.out.println(clientInfo + " Client send " + inputMsg);
					// Process the expression.
					String[] each = inputMsg.split(" ");

					// If there are more than 4 arguments, Send an error message.
					if(each.length > 4) {
						error(outToClient, 400);
					} // If there are less than 2 arguments, Send an error message.
					else if(each.length < 2) {
						error(outToClient, 500);
					}
					// Appropriate arguments. (each.length = 2 or 3 or 4)
					else {
						int value = 0;
						
						// If the expression is not the 'check' operation,
						if (each.length != 2) {
							// Translate String to Integer. (According to the protocol)
							try {
								value = Integer.parseInt(each[2]);
							} catch (NumberFormatException e) { // If it's not a number format,
								error(outToClient, 600);	// Send the error.
								continue;
							}
						}
						
						// Server solves the expression.
						// If the expression's length is 2,
						if (each.length == 2) {							
							sendOp1(outToClient, each[0], each[1].toUpperCase());
						}
						// If the expression's length is 3,
						else if (each.length == 3) {
							sendOp23(outToClient, each[0], each[1].toUpperCase(), value);
						}
						// If the expression's length is 4,
						else if (each.length == 4) {
							sendOp4(outToClient, each[0].toUpperCase(), each[1].toUpperCase(), value, each[3].toUpperCase());
						}
						else { 	// If it's the unsupported operation,
							error(outToClient, 200);
						}
						
					}
				} else {	// If the received expression is the 'exit',
					break;	// escape from the infinite loop.
				}
			}
		} catch(IOException e) { // Print the error when a problem occurs.
			System.out.println(e.getMessage());
			e.printStackTrace();
		} finally { 
			// Everything is done, disconnect with the client.
			System.out.println(clientInfo + " - Disconntion to Client.");
			try {	// Close.
				if (connectionSocket != null) connectionSocket.close();
				if (inFromClient != null) inFromClient.close();
				if (outToClient != null) outToClient.close();
			} catch (IOException e) {	// Print the error when there is a closing problem.
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
	}
	
	// Message format for length is 2. (including check operation)
    private void sendOp1(BufferedWriter writer, String operation, String name) throws IOException {
        if(writer != null) {
        	// If Client wants to use 'check' operation, check there is the account.
        	if (operation.equalsIgnoreCase("check")) {
        		if (a.check(name) == 0) {
        			System.out.println("Account found");
        			writer.write(name + " has " + a.account.get(name) + "$" + "\n");
        			writer.flush();
        		}
        		// If Server doesn't have that account,
        		else {
        			error(writer, 100);
        		}
        	}
        	else {	// If it's the unsupported operation,
        		error(writer, 200);
        	}
        }
    }
    
    // Message format for length is 3. (including deposit & withdraw operation)
    private void sendOp23(BufferedWriter writer, String operation, String name, int value) throws IOException {
        if(writer != null) {
        	if (a.check(name) == 0) {
        		/* If Client wants to use 'deposit' operation,
				 * deposit money(= value = each[2]) to each[1]'s account.
				 */
        		if (operation.equalsIgnoreCase("deposit")) {
					a.deposit(name, value);
					writer.write(name + " now has " + a.account.get(name) + "$" + "\n");
					writer.flush();
				}
				/* If Client wants to use 'withdraw' operation,
				 * withdraw money(= value = each[2]) from each[1]'s account.
				 */
				else if (operation.equalsIgnoreCase("withdraw")) {
					// If withdraw is possible, (There is sufficient balance)
					if (a.account.get(name) - value >= 0) {
						a.withdraw(name, value);
						writer.write(name + " now has " + a.account.get(name) + "$" + "\n");
						writer.flush();
					}
					else {	// lack of account balance.
						error(writer, 300);
					}
				}
				else {	// If it's the unsupported operation,
					error(writer, 200);
				}
			}
        	// If Server doesn't have that account,
        	else {
        		error(writer, 100);
        	}
        }
    }
    
    // Message format for length is 4. (including transfer operation)
    private void sendOp4(BufferedWriter writer, String operation, String senderName, int value, String receiverName) throws IOException {
        if(writer != null) {
        	// Check the sender and receiver have account.
        	if (a.check(senderName) == 0 && a.check(receiverName) == 0) {
        		/* If Client wants to use 'transfer' operation,
				 * transfer money(= value = each[2]) from each[1]'s account to each[3]'s account.
				 */
        		if (operation.equalsIgnoreCase("transfer")) {
        			// If the sender has the sufficient balance,
        			if (a.account.get(senderName) - value >= 0) {
        				a.transfer(senderName, value, receiverName);
    					writer.write(senderName + " now has " + a.account.get(senderName) + "$"
    							+ " " + receiverName + " now has " + a.account.get(receiverName) + "$" + "\n");
    					writer.flush();
        			}
        			// If the sender doesn't have the sufficient balance to send to the receiver,
					else {
						error(writer, 300);		// lack of account balance.
					}
				}
				else {	// If it's the unsupported operation,
					error(writer, 200);
				}
			}
        	// If Server doesn't have that account,
        	else {
        		error(writer, 100);
        	}
        }
    }
    
    // A method that informs that there is an error.
    private void error(BufferedWriter writer, int errorCode) throws IOException {
        String errorMessage = null;

        // My own error code format.
        switch (errorCode) {
            case 100:
                errorMessage = "Account not found!";
                break;
            case 200:
                errorMessage = "Not Supported Operation!";
                break;
            case 300:
            	errorMessage = "Impossible action! Lack of account balance.";
                break;
            case 400:
                errorMessage = "Too many arguments!";
                break;
            case 500:
                errorMessage = "Too few arguments!";
                break;
            case 600:
            	errorMessage = "Incorrect format!";
                break;
        }

        // Display the error.
        System.out.println("Error " + errorCode + " : " + errorMessage);
        // Send the error to Client.
        writer.write("Error " + errorCode + " : " + errorMessage + "\n");
        writer.flush();
    }

}