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

public class ChatServer2 {
	
	ServerSocket server;
	int port = 8003;
	Vector<ClientThread2> vc; //VECTOR�� ClientThread1���� �ִ´�.
	
//	vector�� ��� Ŭ����Ÿ���� ������ �� �־���.������ �� ���׸� �ڿ� <>�� �ؼ� ������ �� �ִ�.
//	vector : ��ü�� ���� �� �ִ� ��ü.
	
//	socket�� ������ inputStream/outputStream���� �� �ִ�.
	
	public ChatServer2() {
		try {
			server = new ServerSocket(port);
			vc = new Vector<ClientThread2>(); //���� Ŭ�������̴ϱ� �տ� ChatServer1. ��� ��.
			
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error in Server");
			System.exit(1); //���������� ����. �Ű����� 0 : �������� ����
//			�ٸ� �ڹ� ���α׷����� �Ѱ��ٶ� �˷��ֱ� ���ؼ� 0 �Ǵ� 1�� ����Ѵ�.
		}
		System.out.println("*ChatServer2.0***********");
		System.out.println("*Client ������ ��ٸ��� �ֽ��ϴ�.**");
		System.out.println("******************");
		try {
			while(true) {
				Socket sock = server.accept();
				ClientThread2 ct = new ClientThread2(sock);
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
			ClientThread2 ct = vc.get(i);
//			clientThread ������ �ִ� ������ �޼��� ������ �޼ҵ� ȣ��. �ݺ�ó�� ��ο��� �޼����� ��������.
			ct.sendMessage(msg);
		}
	}
	
//	client�� ������ ���� �� �ִ�. -> vector���� ������ ������Ѵ�. �̻簣 ���� �������� ��.
//	������ ������ clientThread�� ���Ϳ��� ����
	public void removeClient(ClientThread2 ct) {

		vc.remove(ct);
	}
	
//	���ӵ� ��� id����Ʈ ex)aaa;bbb;��ȣ��;
//	vector�� client�� �����´�.
	public String getIds() {
		String ids ="";
		for (int i = 0; i < vc.size(); i++) {
			ClientThread2 ct = vc.get(i);
			ids += ct.id + ";";
		}
		return ids; //aaa;bbb;��ȣ��;�� ����
	}
	
//	������ ClientThread2 �˻�(idȰ��)
	public ClientThread2 findClient(String id) {
		ClientThread2 ct = null;
		for (int i = 0; i < vc.size(); i++) {
			ct = vc.get(i);
			if(id.equals(ct.id)) {
				break; //���ϴ� ���� ã���� �����. �� ���� �ʴ´�.
			} //--if
		} // -- for
		return ct;
	}
	
	class ClientThread2 extends Thread{ //���⼭����
		
		Socket sock;
		BufferedReader in;
		PrintWriter out;
		String id;
		
		public ClientThread2(Socket sock) {
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
				
//				�� �ּ��� �κ��� �� ������.
//				id = in.readLine();
//				��� ����ڵ鿡�� ������ ����� welcome�޼��� ����
//				sendAllMessage("[" + id +"���� �����ϼ̽��ϴ�.");
//				String line = "";
//				while(true) {
//					line = in.readLine(); //�޼��� ���� ������ ������
//					if(line == null)
//						break;
//					sendAllMessage("[" + id + "]" + line);
//				} //~while
				
				while(true) {
					String line = in.readLine();
					if(line == null)
						break;
					else
						routine(line);
				}
				
				
				in.close();//���� �������δ� �Ұ����� �ڵ�.
				out.close();
				sock.close(); 
				removeClient(this);
				
			} catch (Exception e) {
				removeClient(this); //client���� Ź �������� ����� �´�.
//				this: ���� �� �ڽ�. �� �ڽ��� remove�ع����ڴ�.
				System.err.println(sock + "������.");
			}
			
		}
		public void routine(String line) {
		//	CHATALL:[aaa] ������ �������Դϴ�. 
			System.out.println(line);
			int idx = line.indexOf(ChatProtocol2.DM);
			String cmd = line.substring(0,idx); //0���� idx���� = CHATALL
			String data = line.substring(idx + 1); //[aaa]������ �������Դϴ�. 
			if(cmd.equals(ChatProtocol2.ID)) {
				id = data; //aaa
				//���ο� �����ڰ� �߰��Ǿ��� ������ ����Ʈ ������
				sendAllMessage(ChatProtocol2.CHATLIST + ChatProtocol2.DM + getIds());
				//welcome �޼��� ����
				sendAllMessage(ChatProtocol2.CHATALL + ChatProtocol2.DM + "[" + id + "]���� �����Ͽ����ϴ�.");
			} else if(cmd.equals(ChatProtocol2.CHAT)) {
//				data :bbb;�����(aaa�� ����)
				idx = data.indexOf(';'); //;��ġ���� �������°� idx
				cmd = data.substring(0,idx);//bbb
				data = data.substring(idx + 1); //data
				ClientThread2 ct = findClient(cmd);
				if(ct != null) { //���� ������
//					ct�� bbb Ŭ���̾�Ʈ������
					ct.sendMessage(ChatProtocol2.CHAT + ChatProtocol2.DM +"["
							+ id + "(S)]" + data); //CHAT:[aaa(S)] �����=>bbb���� �Ѿ��.
					sendMessage(ChatProtocol2.CHAT + ChatProtocol2.DM +"["
							+ id + "(S)]" + data);
					}else { //bbb�� ������ �ȵ� ���
					sendMessage(ChatProtocol2.CHAT + ChatProtocol2.DM + "["
							+ cmd + "]�� �����ڰ� �ƴմϴ�.");
				}
			} else if(cmd.equals(ChatProtocol2.MESSAGE)) {
				idx = data.indexOf(';'); //;��ġ���� �������°� idx
				cmd = data.substring(0,idx);//bbb
				data = data.substring(idx + 1); //data
				ClientThread2 ct = findClient(cmd);
				if(ct != null) { //���� ������
//					ct�� bbb Ŭ���̾�Ʈ������
					ct.sendMessage(ChatProtocol2.MESSAGE + ChatProtocol2.DM +"["
					 + id + "(S)]" + data); //CHAT:[aaa(S)] �����=>bbb���� �Ѿ��.
					
					}else { //bbb�� ������ �ȵ� ���
					sendMessage(ChatProtocol2.MESSAGE + ChatProtocol2.DM + "["
					 + cmd + "]�� �����ڰ� �ƴմϴ�.");
					}
			}else if(cmd.equals(ChatProtocol2.CHATALL)) {
				sendAllMessage(ChatProtocol2.CHATALL + ChatProtocol2.DM +"["
					+ id + "]" + data);
			}
			 else if(cmd.equals(ChatProtocol2.CHATALL)) {
				sendAllMessage(ChatProtocol2.CHATALL + ChatProtocol2.DM +"["
				    + id + "]" + data);
			} 				
	    }

//		client���� �޼��� ���� �޼���
		public void sendMessage(String msg) {
		
	}
	// ������� ClientThread�̴�.

	public static void main(String[] args) {
		new ChatServer2();
		
	}

}
