package ch14;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.List;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.StringTokenizer;


//���� ���̶� ä�ð���. ���⼭ f11�� â ���� �� ���� ���� ��ȭ ����.


public class ChatClient2 extends MFrame //MFrame ��Ӿȹ����ϱ� �ƹ��͵� ��������.
implements ActionListener, Runnable {

	Button bt1, bt2, bt3, bt4;
	TextField tf1, tf2, tf3; //connect, save, message ��
	TextArea area; // ���� ���� �� �ִ� ��.(darkgrary)
	List list;//������ �����
	Socket sock;
	BufferedReader in;
	PrintWriter out;
	
	String listTitle = "*******��ȭ�ڸ��*******";
	boolean flag = false;
	String filterList[] = {"�ٺ�","������","����","�ڹ�","java"};

	public ChatClient2() {
		super(450, 500); //��Ÿ���� â�� ���μ��� ũ��
		setTitle("MyChat 2.0");
		Panel p1 = new Panel();
		p1.add(new Label("Host", Label.RIGHT)); //LEFT RIGHT���� �� �𸣰ڴ�.
		p1.add(tf1 = new TextField("127.0.0.1"));
		p1.add(new Label("Port", Label.RIGHT));
		p1.add(tf2 = new TextField("8003"));
		bt1 = new Button("connect");
		bt1.addActionListener(this);
		p1.add(bt1);
		add(BorderLayout.NORTH, p1);
		// //////////////////////////////////////////////////////////////////////////////////////////
		area = new TextArea("ChatClient 2.0");
		area.setBackground(Color.DARK_GRAY);
		area.setForeground(Color.PINK);
		area.setEditable(false);
		add(BorderLayout.CENTER, area);
		// /////////////////////////////////////////////////////////////////////////////////////////
		Panel p2 = new Panel();
		p2.setLayout(new BorderLayout());
		list = new List();
		list.add(listTitle);
		p2.add(BorderLayout.CENTER, list);
		Panel p3 = new Panel();
		p3.setLayout(new GridLayout(1, 2));
		bt2 = new Button("Save");
		bt2.addActionListener(this);
		bt3 = new Button("Message");
		bt3.addActionListener(this);
		p3.add(bt2);
		p3.add(bt3);
		p2.add(BorderLayout.SOUTH, p3);
		add(BorderLayout.EAST, p2);
		// ///////////////////////////////////////////////////////////////////////////////////////////
		Panel p4 = new Panel();
		tf3 = new TextField("", 50);
		tf3.addActionListener(this);
		bt4 = new Button("send");
		bt4.addActionListener(this);
		p4.add(tf3);
		p4.add(bt4);
		add(BorderLayout.SOUTH, p4);
		validate();
	}
	
	public void run() { 
		try {
			String host = tf1.getText().trim(); //�� �������� �����´�.
			int port = Integer.parseInt(tf2.getText().trim()); //������ ��ȯ�Ͽ� ��ƴ���� ������
			connect(host,port); //host�� port�� ����. �ؿ� �޼��� �ִ�.
			area.append(in.readLine() + "\n");
			while(true) {
				String line = in.readLine();
				if(line == null)
					break;
				else
					routine(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}//--run
	
	public void routine(String line) {
		 int idx = line.indexOf(ChatProtocol2.DM); 
         String cmd = line.substring(0, idx); //string ��ü�� ���� �ε����� ��ġ���� ���� �ε��� �� ���� ���ڿ��� �κ� ���ڿ��� ��ȯ�մϴ�.
         String data = line.substring(idx + 1);//��ġ �������� ������
         if(cmd.equals(ChatProtocol2.CHATLIST)){//String line = in.readLine(); readLine():������ �� ���� ������ ���ڿ��� ��ȯ�մϴ�.
//        	 CHATLIST: ��ȭ�ڸ��
//        	 data = aaa;bbb;ȫ�浿;ddd;�̷��� �����Ͱ� �Ѿ�ð��̴�. getId�� ���ĸ�������
        	 list.removeAll();
        	 list.add(listTitle); //**��ȭ�ڸ��*** �� �� ����
        	 StringTokenizer st = new StringTokenizer(data,";"); //StringTokenizer ���� �����ϴ�. �� ����ض�
//        	 StringTokenizer: ���ڿ��� �츮�� ������ �����ڷ� ���ڿ��� �ɰ��ִ� Ŭ�����Դϴ�. �׷��� �ɰ����� ���ڿ��� �츮�� ��ū(token)�̶�� �θ��ϴ�.
        	 while(st.hasMoreTokens()) {
        		 list.add(st.nextToken());
        	 } //������ ���ٰ� �� �̻� ������ false. whileŻ��.
         }else if(cmd.equals(ChatProtocol2.CHAT)|| //cmd : line.substring(0, idx);
        		 cmd.equals(ChatProtocol2.CHATALL)) {
        	 area.append(data + "\n");
         }else if(cmd.equals(ChatProtocol2.MESSAGE)) {
//        	 data = bbb;�����.. <-�̷� ������ ������ �Ѿ�ð��̴�.
        	 idx = data.indexOf(';'); //�����͸� �ɰ���.
        	 cmd = data.substring(0,idx); //bbb
        	 data = data.substring(idx+1);//�����
        	 new Message("FROM:",cmd,data);
         }
		
	}//--routine
	
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if(obj == bt1) { //bt1 = connect��ư
			new Thread(this).start(); //run()ȣ�� ���
			bt1.setEnabled(false);
			tf1.setEnabled(flag);
			tf2.setEnabled(flag);
			area.setText("");
		}else if(obj == bt2) { //save��ư :��ȭ��������
			try {
//				FileWriter : ���ϻ����� �ڵ����εȴ�.
				long file = System.currentTimeMillis();
				FileWriter fw = new FileWriter("ch14/" +file+".txt");
				fw.write(area.getText());
				fw.flush();
				fw.close();
				area.setText("");//�ʱ�ȭ
				new MDialog(this, "Save", "��ȭ������ �����Ͽ����ϴ�.");
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}else if(obj == bt3) { //message��ư, ������ ���� �� â �����.
			int idx = list.getSelectedIndex();
			if(idx ==-1 || idx==0) { //id ���þ��ϸ� -1, ���� �����Ǹ� 0
				new MDialog(this,"���","���̵� �����ϼ���");
			} else {
				new Message("TO:"); //�̰� �ؿ����� �ٸ��� ���������.
			}
			
		}else if(obj == bt4 || obj == tf3) {
			String str = tf3.getText().trim(); //�� ���� �� â �� �������� ��.
			if(str.length()==0) {
				return;
			}
			if(filterMgr(str)) {
				new MDialog(this,"���","�Է��Ͻ� ���ڴ� �������Դϴ�.");//this :default�� ��Ȱ��ȭ��
				return;
			}
//			id�Է� ��� �Ǵ� ä��
//			������ flag=false�س���
			if(!flag) {//id�Է�
//				������ id�� ���� ������.
				sendMessage(ChatProtocol2.ID + ChatProtocol2.DM + str);
				setTitle(getTitle() + "-" + str + "�� �ݰ����ϴ�.");
				area.setText("");
				tf3.setText("");
				tf3.requestFocus();
				flag = true;//������� ��ü ä��
			}else { //ä��
				int idx = list.getSelectedIndex();
//				System.out.println("idx : " + idx);
				//INDEX :�������� ���� ��. ITEM ����ִ� �� �������� ���� �ٸ���!
				if(idx ==0 || idx ==-1) {//��üä��
					sendMessage(ChatProtocol2.CHATALL + ChatProtocol2.DM + str);
				} else { //�ӼӸ�ä��(��üä�� �� ���̼��� ä���ϱ� 1:1ä�ð��� �ٸ���)
					String id = list.getSelectedItem();
					sendMessage(ChatProtocol2.CHAT+ChatProtocol2.DM + id +";"+str);
				}
				tf3.setText("");
				tf3.requestFocus(); //��ȭ�� id�� focus�� ���� �� �ֿܼ� -1����
			}
		}
	}//--actionPerformed
	
	public void connect(String host, int port) {
		try {
			sock = new Socket(host, port);
			in = new BufferedReader(
					new InputStreamReader(
							sock.getInputStream()));
			out = new PrintWriter(
					sock.getOutputStream(),true/*auto flush*/);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}//--connect
	
	public void sendMessage(String msg) {
		out.println(msg);
	}

	public boolean filterMgr(String msg){
		boolean flag = false;//false�̸� ������ �ƴ�
		//StringTokenizer���� �������, contains �ؿ����� �����.
		for (int i = 0; i < filterList.length; i++) {
			if(msg.contains(filterList[i])) {
				flag = true;
//				break; �̰Ÿ��� �ؿ��� �ص� �ɵ�.
				return true;
			}
		}
//		return flag;����. �̷��� �ؾ� ���� ������ ������ƴϰ� �ȴ�.
		return false;
	}
	
	class Message extends Frame implements ActionListener {

		Button send, close;
		TextField name;
		TextArea ta;
		String mode;// to/from
		String id;

		public Message(String mode) {
			setTitle("����������");
			this.mode = mode;
			id = list.getSelectedItem();
			layset("");
			validate();
		}
		public Message(String mode, String id, String msg) {
			setTitle("�����б�");
			this.mode = mode;
			this.id = id;
			layset(msg);
			validate();
		}
		public void layset(String msg) {
			 addWindowListener(new WindowAdapter() {
				   public void windowClosing(WindowEvent e) {
				    dispose();
				   }
			});
			Panel p1 = new Panel();
			p1.add(new Label(mode, Label.CENTER));
			name = new TextField(id, 20);
			p1.add(name);
			add(BorderLayout.NORTH, p1);
			
			ta = new TextArea("");
			add(BorderLayout.CENTER, ta);
			ta.setText(msg);
			Panel p2 = new Panel();
			if (mode.equals("TO:")) { //���� "TO"�� �����ؾ�
				p2.add(send = new Button("send"));
				send.addActionListener(this);
			}
			p2.add(close = new Button("close"));
			close.addActionListener(this);
			add(BorderLayout.SOUTH, p2);
			
			setBounds(200, 200, 250, 250);
			setVisible(true);
		}

		public void actionPerformed(ActionEvent e) {
			if(e.getSource()==send){
				sendMessage(ChatProtocol2.MESSAGE+
						ChatProtocol2.DM+id+";"+ ta.getText()); //send�̺�Ʈ
			}
			setVisible(false); //â�� ȭ�鿡 ��� ������ ���´�.
			dispose();
		}
	}

	class MDialog extends Dialog implements ActionListener{
//		Dialog : ȭ�鿡 ���â���� �� ���.
		
		Button ok;
		ChatClient2 ct2;
		
		public MDialog(ChatClient2 ct2,String title, String msg) {
			super(ct2, title, true);
			this.ct2 = ct2;
			 //////////////////////////////////////////////////////////////////////////////////////////
			   addWindowListener(new WindowAdapter() {
			    public void windowClosing(WindowEvent e) {
			     dispose();
			    }
			   });
			   /////////////////////////////////////////////////////////////////////////////////////////
			   setLayout(new GridLayout(2,1));
			   Label label = new Label(msg, Label.CENTER);
			   add(label);
			   add(ok = new Button("Ȯ��"));
			   ok.addActionListener(this);
			   layset();
			   setVisible(true);
			   validate();
		}
		
		public void layset() {
			int x = ct2.getX();
			int y = ct2.getY();
			int w = ct2.getWidth();
			int h = ct2.getHeight();
			int w1 = 150;
			int h1 = 100;
			setBounds(x + w / 2 - w1 / 2, 
					y + h / 2 - h1 / 2, 200, 100);
		}

		public void actionPerformed(ActionEvent e) {
			tf3.setText("");
			dispose();
		}
	}
	
	public static void main(String[] args) {
		new ChatClient2();
	}
}
