package homework;

import java.awt.Button;
import java.awt.Color;
import java.awt.Font;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class TalkAWT extends MFrame implements ActionListener {

	TextField idTf, pwTf;
	Label logo, idl, pwl, msgl;
	Button logBtn;
	Socket sock;
	BufferedReader in;
	PrintWriter out;
	String id;
	String host = "127.0.0.1";
	int port = 8006;
	String title = "Talk 1.0";
	String label[] = {"ID와 PWD를 입력하세요.",
								"ID와 PWD를 확인하세요.",
								"이중 접속입니다."};

	public TalkAWT() {
		super(450, 400, new Color(100, 200, 100));
		setLayout(null);
		setTitle(title);
		logo = new Label(title);
		logo.setFont(new Font("Dialog", Font.BOLD, 50));

		idl = new Label("ID");
		pwl = new Label("PWD");
		idTf = new TextField("aaa");
		pwTf = new TextField("1234");
		logBtn = new Button("로그인");
		msgl = new Label(label[0]);
		logo.setBounds(100, 60, 270, 100);
		idl.setBounds(150, 200, 50, 20);
		idTf.setBounds(200, 200, 100, 20);
		pwl.setBounds(150, 230, 50, 20);
		pwTf.setBounds(200, 230, 100, 20);
		logBtn.setBounds(150, 260, 150, 40);
		msgl.setBounds(150, 320, 150, 40);
		logBtn.addActionListener(this);
		add(logo);
		add(idl);
		add(idTf);
		add(pwl);
		add(pwTf);
		add(logBtn);
		add(msgl);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		try {
			if(obj == logBtn) {
				connect();
				id = idTf.getText().trim();
				
//				서버로 ID와 pwd 전송
				out.println(id + ":" + id + ";" 
					+ pwTf.getText().trim());
				
//				굳이 스레드 만들필요 없다. 값을 받아야 되기 때문에
				String line = in.readLine(); //서버
				int idx = line.indexOf(id);
				String cmd = line.substring(0, idx);//CHATALL
			    String data = line.substring(idx+1);
			    
//			    if(cmd.equals(id)) {
//			    	
//			    	if(data.equals("F")) { //로그인 실패
//			    		msgl.setForeground(Color.RED);
//			    		msgl.setText(label[1]);
//			    	}else if(data.equals("C")) { // 이중접속
//			    		msgl.setForeground(Color.BLUE);
//			    		msgl.setText(label[2]);
//			    	} else if(data.equals("T")) { //로그인 성공
////			    		dispose(); //창이 사라짐
//			    		new TalkClient();
//			    	
//			    	
//			    	}
//			    }
			    
			    new TalkClient(in,out,id);
			    
			}
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		
	}
	
	public void connect() {
			try {
				if(sock == null) {
				sock = new Socket(host, port);
				in = new BufferedReader(
						new InputStreamReader(
								sock.getInputStream()));
				out = new PrintWriter(
						sock.getOutputStream(),true/*auto flush*/);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}//--connect
	
	
	public static void main(String[] args) {
		new TalkAWT();
	}
}
