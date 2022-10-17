package ch14;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

//잘 안되는 경우 top 4
//1. 나도 모르는 사이 이클립스 서버가 돌아가고 있다. 배치파일에러가 나면 콘솔에 안뜬다...->cmd창에서 확인 후 서버꺼라
//2. 오타난 경우 
//3. path가 잘못 지정된 경우
//4. 17로 컴파일 했는데, path가 11이면 오류난다.

public class ChatServer1 {
	
	ServerSocket server;
	int port = 8002;
	Vector<ClientThread1> vc; //VECTOR에 ClientThread1값만 넣는다.
	
//	vector에 모든 클래스타입을 저장할 수 있었다.버전업 후 제네릭 뒤에 <>를 해서 지정할 수 있다.
//	vector : 객체를 담을 수 있는 객체.
	
//	socket을 만들어야 inputStream/outputStream만들 수 있다.
	
	public ChatServer1() {
		try {
			server = new ServerSocket(port);
			vc = new Vector<ClientThread1>(); //같은 클래스내이니까 앞에 ChatServer1. 없어도 됨.
			
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error in Server");
			System.exit(1); //비정상적인 종료. 매개변수 0 : 정상적인 종료
//			다른 자바 프로그램에게 넘겨줄때 알려주기 위해서 0 또는 1을 사용한다.
		}
		System.out.println("*ChatServer1***********");
		System.out.println("*Client 접속을 기다리고 있습니다.**");
		System.out.println("******************");
		try {
			while(true) {
				Socket sock = server.accept();
				ClientThread1 ct = new ClientThread1(sock);
				ct.start(); //thread 스케줄러에 등록 -> run method 호출
				vc.addElement(ct); //ClientThread 객체를 vector에 저장
//				vector장점: vector 데이터를 넣었다 뺐다 맘대로 가능. 사이즈가 유동적
				
//				전체 서버가 해야할 일은 다 끝남. 호출한 뒤 벡터에 저장. 나머지는 clientThread에서.(서로 주고받기)
			}
		} catch (Exception e) {
			System.err.println("Error in sock");
			e.printStackTrace();
		}
	}
	
//	모든 접속된 client에게 메세지 전달
	public void sendAllMessage(String msg) {
		for(int i =0; i<vc.size(); i++) {
//			vector에 저장된 clidentThread를 순차적으로 가져옴
			ClientThread1 ct = vc.get(i);
//			clientThread 가지고 있는 각각의 메세지 보내는 메소드 호출. 반복처럼 모두에게 메세지가 보내진다.
			ct.sendMessage(msg);
		}
	}
	
//	client가 접속을 끊을 수 있다. -> vector에서 접속을 끊어야한다. 이사간 집에 우편보내는 꼴.
//	접속이 끊어진 clientThread는 벡터에서 제거
	public void removeClient(ClientThread1 ct) {
		vc.remove(ct);
	}
	
	class ClientThread1 extends Thread{ //여기서부터
		
		Socket sock;
		BufferedReader in;
		PrintWriter out;
		String id;
		
		public ClientThread1(Socket sock) {
//			echoThread에서 가져옴.
			try {
				this.sock = sock;
				in = new BufferedReader(
						new InputStreamReader(sock.getInputStream()));
				out = new PrintWriter(sock.getOutputStream(), true);
				System.out.println(sock + "접속됨.");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		@Override
		public void run() { //고객들 메세지 주고받는 부분.
			try {
//				client가 처음으로 받는 메세지.
				out.println("사용하실 아이디를 입력하세요.");
				id = in.readLine();
//				모든 사용자들에게 접속한 사람의 welcome메세지 전달
				sendAllMessage("[" + id +"님이 입장하셨습니다.");
				String line = "";
				while(true) {
					line = in.readLine(); //메세지 들어올 때까지 대기상태
					if(line == null)
						break;
					sendAllMessage("[" + id + "]" + line);
				} //~while
				in.close();
				out.close();
//				sock.close(); 혹시 여기인가.
				removeClient(this);
				
			} catch (Exception e) {
				removeClient(this); //client쪽이 탁 놔버리면 여기로 온다.
//				this: 현재 나 자신. 나 자신을 remove해버리겠다.
				System.err.println(sock + "끊어짐.");
			}
			
		}
//		client에게 메세지 전달 메서드
		public void sendMessage(String msg) {
			out.println(msg);
		}
	}
	// 여기까지 ClientThread이다.

	public static void main(String[] args) {
		new ChatServer1();
		
	}

}
