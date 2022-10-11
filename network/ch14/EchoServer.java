package ch14;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServer {
	
	int port = 8001;
	int cnt = 0;
	
	public EchoServer() {
		try {
			ServerSocket server = new ServerSocket(port);
			System.out.println("ServerSocket Start....");
			while(true) {
				Socket sock = server.accept(); //Client�� ������ ������ ������(thread) client�� ���� ��� �����ϴ� ���� �ٷ� �۵�
				EchoThread et = new EchoThread(sock);
				et.start();
				cnt++;
				System.out.println("Client" + cnt + "Socket");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	//�ܺ�Ŭ������ ���� ������ ���谡 �ִ� Ŭ����, ������ ����� �����ϴ�.
	//����Ŭ���� EchoServer$EchoThread.class �̷��� ����Ǿ��ִ�.
	class EchoThread extends Thread{
		
		Socket sock;
		BufferedReader in;
		PrintWriter out;
		
		public EchoThread(Socket sock) {
			try {
				this.sock = sock;
				in = new BufferedReader(
						new InputStreamReader(sock.getInputStream()));
				out = new PrintWriter(sock.getOutputStream(), true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		@Override
		public void run() {
			try {
//				client�� ������ �ϸ� ���� ���� �޴� �޼���
				out.println("Hello Enter Bye to exit");//print �ڿ� ln������ �ؾ��Ѵ�!!
				while(true) {
//					Client�� �޼��� ������ ����
					String line = in.readLine();
					if(line == null)
						break;
					else {
						out.println("Echo : " + line);
						if(line.equals("BYE"))
							break;
					}
				}
				in.close();
				out.close();
				sock.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}	
	}

	public static void main(String[] args) {
		new EchoServer();
	}

}
