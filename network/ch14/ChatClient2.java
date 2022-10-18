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


//여러 명이랑 채팅가능. 여기서 f11로 창 여러 개 띄우면 같이 대화 가능.


public class ChatClient2 extends MFrame //MFrame 상속안받으니까 아무것도 뜨지않음.
implements ActionListener, Runnable {

	Button bt1, bt2, bt3, bt4;
	TextField tf1, tf2, tf3; //connect, save, message 등
	TextArea area; // 많이 적을 수 있는 곳.(darkgrary)
	List list;//오른쪽 흰공간
	Socket sock;
	BufferedReader in;
	PrintWriter out;
	
	String listTitle = "*******대화자명단*******";
	boolean flag = false;
	String filterList[] = {"바보","개새끼","새끼","자바","java"};

	public ChatClient2() {
		super(450, 500); //나타나는 창의 가로세로 크기
		setTitle("MyChat 2.0");
		Panel p1 = new Panel();
		p1.add(new Label("Host", Label.RIGHT)); //LEFT RIGHT차이 잘 모르겠다.
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
			String host = tf1.getText().trim(); //빈 공간없이 가져온다.
			int port = Integer.parseInt(tf2.getText().trim()); //정수로 변환하여 빈틈없이 가져옴
			connect(host,port); //host와 port를 연결. 밑에 메서드 있다.
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
         String cmd = line.substring(0, idx); //string 객체의 시작 인덱스로 위치부터 종료 인덱스 전 까지 문자열의 부분 문자열을 반환합니다.
         String data = line.substring(idx + 1);//위치 다음부터 끝까지
         if(cmd.equals(ChatProtocol2.CHATLIST)){//String line = in.readLine(); readLine():파일의 한 줄을 가져와 문자열로 반환합니다.
//        	 CHATLIST: 대화자명단
//        	 data = aaa;bbb;홍길동;ddd;이렇게 데이터가 넘어올것이다. getId로 형식만들어놓음
        	 list.removeAll();
        	 list.add(listTitle); //**대화자명단*** 이 한 문장
        	 StringTokenizer st = new StringTokenizer(data,";"); //StringTokenizer 아주 유용하다. 잘 사용해라
//        	 StringTokenizer: 문자열을 우리가 지정한 구분자로 문자열을 쪼개주는 클래스입니다. 그렇게 쪼개어진 문자열을 우리는 토큰(token)이라고 부릅니다.
        	 while(st.hasMoreTokens()) {
        		 list.add(st.nextToken());
        	 } //끝까지 돌다가 더 이상 없으면 false. while탈출.
         }else if(cmd.equals(ChatProtocol2.CHAT)|| //cmd : line.substring(0, idx);
        		 cmd.equals(ChatProtocol2.CHATALL)) {
        	 area.append(data + "\n");
         }else if(cmd.equals(ChatProtocol2.MESSAGE)) {
//        	 data = bbb;밥먹자.. <-이런 형식을 데이터 넘어올것이다.
        	 idx = data.indexOf(';'); //데이터를 쪼갠다.
        	 cmd = data.substring(0,idx); //bbb
        	 data = data.substring(idx+1);//밥먹자
        	 new Message("FROM:",cmd,data);
         }
		
	}//--routine
	
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if(obj == bt1) { //bt1 = connect버튼
			new Thread(this).start(); //run()호출 결과
			bt1.setEnabled(false);
			tf1.setEnabled(flag);
			tf2.setEnabled(flag);
			area.setText("");
		}else if(obj == bt2) { //save버튼 :대화내용저장
			try {
//				FileWriter : 파일생성이 자동으로된다.
				long file = System.currentTimeMillis();
				FileWriter fw = new FileWriter("ch14/" +file+".txt");
				fw.write(area.getText());
				fw.flush();
				fw.close();
				area.setText("");//초기화
				new MDialog(this, "Save", "대화내용을 저장하였습니다.");
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}else if(obj == bt3) { //message버튼, 누르면 따로 흰 창 생긴다.
			int idx = list.getSelectedIndex();
			if(idx ==-1 || idx==0) { //id 선택안하면 -1, 선택 고정되면 0
				new MDialog(this,"경고","아이디를 선택하세요");
			} else {
				new Message("TO:"); //이거 밑에꺼와 다르면 오류생긴다.
			}
			
		}else if(obj == bt4 || obj == tf3) {
			String str = tf3.getText().trim(); //맨 밑의 흰 창 값 가져오는 것.
			if(str.length()==0) {
				return;
			}
			if(filterMgr(str)) {
				new MDialog(this,"경고","입력하신 글자는 금지어입니다.");//this :default가 비활성화라서
				return;
			}
//			id입력 경우 또는 채팅
//			위에서 flag=false해놓음
			if(!flag) {//id입력
//				서버로 id에 값을 보낸다.
				sendMessage(ChatProtocol2.ID + ChatProtocol2.DM + str);
				setTitle(getTitle() + "-" + str + "님 반갑습니다.");
				area.setText("");
				tf3.setText("");
				tf3.requestFocus();
				flag = true;//여기까지 단체 채팅
			}else { //채팅
				int idx = list.getSelectedIndex();
//				System.out.println("idx : " + idx);
				//INDEX :순서값에 대한 값. ITEM 들어있는 값 가져오기 둘이 다르다!
				if(idx ==0 || idx ==-1) {//전체채팅
					sendMessage(ChatProtocol2.CHATALL + ChatProtocol2.DM + str);
				} else { //귓속말채팅(단체채팅 중 둘이서만 채팅하기 1:1채팅과는 다르다)
					String id = list.getSelectedItem();
					sendMessage(ChatProtocol2.CHAT+ChatProtocol2.DM + id +";"+str);
				}
				tf3.setText("");
				tf3.requestFocus(); //대화자 id에 focus가 없을 때 콘솔에 -1나옴
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
		boolean flag = false;//false이면 금지어 아님
		//StringTokenizer쌤이 사용했음, contains 준영씨가 사용함.
		for (int i = 0; i < filterList.length; i++) {
			if(msg.contains(filterList[i])) {
				flag = true;
//				break; 이거말구 밑에꺼 해도 될듯.
				return true;
			}
		}
//		return flag;말구. 이렇게 해야 다음 문장이 금지어아니게 된다.
		return false;
	}
	
	class Message extends Frame implements ActionListener {

		Button send, close;
		TextField name;
		TextArea ta;
		String mode;// to/from
		String id;

		public Message(String mode) {
			setTitle("쪽지보내기");
			this.mode = mode;
			id = list.getSelectedItem();
			layset("");
			validate();
		}
		public Message(String mode, String id, String msg) {
			setTitle("쪽지읽기");
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
			if (mode.equals("TO:")) { //위에 "TO"와 동일해야
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
						ChatProtocol2.DM+id+";"+ ta.getText()); //send이벤트
			}
			setVisible(false); //창을 화면에 띄울 것인지 묻는다.
			dispose();
		}
	}

	class MDialog extends Dialog implements ActionListener{
//		Dialog : 화면에 경고창같은 꺼 뜬다.
		
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
			   add(ok = new Button("확인"));
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
