import java.io.*;
import java.net.*;

public class ServerEx {

	public static void main(String[] args) {
		BufferedReader in = null;
		BufferedReader stin = null;
		BufferedWriter out = null;
		ServerSocket listener = null;
		Socket socket = null;
		
		try {
			listener = new ServerSocket(9999);	// 서버 소켓 생성.
			socket = listener.accept();
			System.out.println("연결됨");
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));	// 클라이언트로부터의 입력.
			stin = new BufferedReader(new InputStreamReader(System.in));	// 키보드로부터의 입력.
			out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));	// 클라이언트로 출력.
			
			String inputMsg;
			while(true) {
				inputMsg = in.readLine();	// 클라이언트에게 읽어온 것.
				if (inputMsg.equalsIgnoreCase("bye")) {		// 그게 bye면 서버 종료.
					break;
				}
				
				System.out.println(inputMsg);
				
				String outputMsg = stin.readLine();
				if (outputMsg.equalsIgnoreCase("bye")) {
					out.write(outputMsg);
					out.flush();
					break;
				}
				out.write("서버>" + outputMsg + '\n');
				out.flush();
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				socket.close();
				listener.close();
			} catch (IOException e) {
				System.out.println("클라이언트와 채팅 중 오류가 발생했습니다.");
			}
		}
	}

}
