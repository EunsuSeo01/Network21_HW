import java.io.*;
import java.net.*;

public class ClientEx {
	public static void main(String[] args) {
		BufferedReader in = null;
		BufferedReader stin = null;
		BufferedWriter out = null;
		Socket socket = null;
		
		try {
			socket = new Socket("localhost", 9999);		// 클라이언트 소켓 생성.
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			stin = new BufferedReader(new InputStreamReader(System.in));
			out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			String outputMsg;
			
			while(true) {
				outputMsg = stin.readLine();
				if (outputMsg.equalsIgnoreCase("bye")) {	// 클라이언트가 보내는 게 bye.
					out.write(outputMsg);	// 그럼 서버에 bye 적어주고,
					out.flush();
					break;					// 클라이언트 종료.
				}
				
				out.write("클라이언트>" + outputMsg + "\n");
				out.flush();
				String inputMsg = in.readLine();	// 서버에서 한 행의 문자열을 읽어옴.
				System.out.println(inputMsg);		// 서버가 보낸 메세지를 화면에 출력.
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				socket.close();		// 클라이언트 소켓 닫기.
			} catch (IOException e) {
				System.out.println("서버와 채팅 중 오류가 발생했습니다.");
			}
		}
	}
}
