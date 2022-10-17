package ch14;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

//�� �ȵǴ� ��� top 4
//1. ���� �𸣴� ���� ��Ŭ���� ������ ���ư��� �ִ�. ��ġ���Ͽ����� ���� �ֿܼ� �ȶ��...->cmdâ���� Ȯ�� �� ��������
//2. ��Ÿ�� ��� 
//3. path�� �߸� ������ ���
//4. 17�� ������ �ߴµ�, path�� 11�̸� ��������.

public class ChatServer1 {
	
	ServerSocket server;
	int port = 8002;
	Vector<ClientThread1> vc; //VECTOR�� ClientThread1���� �ִ´�.
	
//	vector�� ��� Ŭ����Ÿ���� ������ �� �־���.������ �� ���׸� �ڿ� <>�� �ؼ� ������ �� �ִ�.
//	vector : ��ü�� ���� �� �ִ� ��ü.
	
//	socket�� ������ inputStream/outputStream���� �� �ִ�.
	
	public ChatServer1() {
		try {
			server = new ServerSocket(port);
			vc = new Vector<ClientThread1>(); //���� Ŭ�������̴ϱ� �տ� ChatServer1. ��� ��.
			
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error in Server");
			System.exit(1); //���������� ����. �Ű����� 0 : �������� ����
//			�ٸ� �ڹ� ���α׷����� �Ѱ��ٶ� �˷��ֱ� ���ؼ� 0 �Ǵ� 1�� ����Ѵ�.
		}
		System.out.println("*ChatServer1***********");
		System.out.println("*Client ������ ��ٸ��� �ֽ��ϴ�.**");
		System.out.println("******************");
		try {
			while(true) {
				Socket sock = server.accept();
				ClientThread1 ct = new ClientThread1(sock);
				ct.start(); //thread �����ٷ��� ��� -> run method ȣ��
				vc.addElement(ct); //ClientThread ��ü�� vector�� ����
//				vector����: vector �����͸� �־��� ���� ����� ����. ����� ������
				
//				��ü ������ �ؾ��� ���� �� ����. ȣ���� �� ���Ϳ� ����. �������� clientThread����.(���� �ְ�ޱ�)
			}
		} catch (Exception e) {
			System.err.println("Error in sock");
			e.printStackTrace();
		}
	}
	
//	��� ���ӵ� client���� �޼��� ����
	public void sendAllMessage(String msg) {
		for(int i =0; i<vc.size(); i++) {
//			vector�� ����� clidentThread�� ���������� ������
			ClientThread1 ct = vc.get(i);
//			clientThread ������ �ִ� ������ �޼��� ������ �޼ҵ� ȣ��. �ݺ�ó�� ��ο��� �޼����� ��������.
			ct.sendMessage(msg);
		}
	}
	
//	client�� ������ ���� �� �ִ�. -> vector���� ������ ������Ѵ�. �̻簣 ���� �������� ��.
//	������ ������ clientThread�� ���Ϳ��� ����
	public void removeClient(ClientThread1 ct) {
		vc.remove(ct);
	}
	
	class ClientThread1 extends Thread{ //���⼭����
		
		Socket sock;
		BufferedReader in;
		PrintWriter out;
		String id;
		
		public ClientThread1(Socket sock) {
//			echoThread���� ������.
			try {
				this.sock = sock;
				in = new BufferedReader(
						new InputStreamReader(sock.getInputStream()));
				out = new PrintWriter(sock.getOutputStream(), true);
				System.out.println(sock + "���ӵ�.");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		@Override
		public void run() { //���� �޼��� �ְ�޴� �κ�.
			try {
//				client�� ó������ �޴� �޼���.
				out.println("����Ͻ� ���̵� �Է��ϼ���.");
				id = in.readLine();
//				��� ����ڵ鿡�� ������ ����� welcome�޼��� ����
				sendAllMessage("[" + id +"���� �����ϼ̽��ϴ�.");
				String line = "";
				while(true) {
					line = in.readLine(); //�޼��� ���� ������ ������
					if(line == null)
						break;
					sendAllMessage("[" + id + "]" + line);
				} //~while
				in.close();
				out.close();
//				sock.close(); Ȥ�� �����ΰ�.
				removeClient(this);
				
			} catch (Exception e) {
				removeClient(this); //client���� Ź �������� ����� �´�.
//				this: ���� �� �ڽ�. �� �ڽ��� remove�ع����ڴ�.
				System.err.println(sock + "������.");
			}
			
		}
//		client���� �޼��� ���� �޼���
		public void sendMessage(String msg) {
			out.println(msg);
		}
	}
	// ������� ClientThread�̴�.

	public static void main(String[] args) {
		new ChatServer1();
		
	}

}
