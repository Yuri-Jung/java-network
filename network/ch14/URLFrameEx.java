package ch14;


import java.awt.Button;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.URL;


public class URLFrameEx extends MFrame implements ActionListener {

	TextArea ta;
	TextField tf;
	Button connect;
	Button save;
	
// ȭ�鱸���κ�
	public URLFrameEx() {
		super(500, 500);
		setTitle("ViewHost");
		Panel p = new Panel();
		p.add(tf = new TextField("http://", 40));
		p.add(connect = new Button("connect"));
		p.add(save = new Button("save"));
		ta = new TextArea();
		add("North", p);
		add("Center", ta);
		save.setEnabled(false);
		connect.addActionListener(this);
		save.addActionListener(this);
		tf.addActionListener(this);
		validate();
	}

	@Override 
	
	public void actionPerformed(ActionEvent e) {
//		try {
//			URL url = new URL(null);
//			System.out.println("Protocol: " + url.getProtocol());
//			System.out.println("Host: " + url.getHost());
//			System.out.println("Port: " + url.getPort());
//			System.out.println("Path: " + url.getPath());
//			System.out.println("Query : " + url.getQuery());
//			System.out.println("FileName: " + url.getFile());
//			System.out.println("Ref: " + url.getRef());
//			
//			InputStream is = url.openStream();
//			
//		} catch (Exception e2) {
//			e2.printStackTrace();
//		}
		
		
//		Reader reader = new InputStreamReader(is);
//		while (true) {
//			try {
//				int i = reader.read();
//				if(i==-1) break;
//				System.out.println((char)i);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
		
		//�̰Ű� ���̴�.
		//textfiled�� button�� ���� ����
		Object obj = e.getSource();
		String host = tf.getText().trim();
		if(obj == tf || obj == connect) { //�Է�â�� connect��ư
			ta.setText(""); //���� ���� �ִٸ� �ʱ�ȭ

			connectHost(host);
			save.setEnabled(true); //save��ư Ȱ��ȭ
		}else if(obj == save) { //save ��ư
			createFile(host, ta.getText());
			save.setEnabled(false); //save ��ư ��Ȱ��ȭ
			tf.setText("http://");
			ta.setText("");
			ta.append("�����Ͽ����ϴ�.");
			tf.requestFocus();
		}
		
		
	}
	
	//�������� ����� �ִ��� ���������� ����. 
	public void connectHost(String host) {
		try {
			URL url = new URL(host);
			BufferedReader br = new BufferedReader(
					new InputStreamReader(url.openStream(), "UTF-8"));
			String line = "";
			while(true) {
				line = br.readLine();
				if(line==null) break;
				ta.append(line+"\n");
			}
			br.close();
		} catch (Exception e) {
			//e.printStackTrace();
			ta.append("�ش�Ǵ� ȣ��Ʈ�� �����ϴ�.");
		}
	}
	
	//ù��° �Ű������� ���ϸ� ���� ���� ����
	public void createFile(String file, String content) {
		try {
			 //http:// �����ϰ� �����̸� ����
			FileWriter fw = new FileWriter("ch14/"+file.substring(7)+".txt");
			fw.write(content);
			fw.flush();
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) {
		URLFrameEx ex = new URLFrameEx();
	}
}







