package ch14;

public class ChatProtocol2 {
//	(C->S) ID : aaa ��������
//	(S->C) CHALLIST : aaa; bbb; ccc; ��ȣ��;
	
	public static final String ID = "ID"; //�տ� �������� �ٿ��� ������ �װ��� �м��Ѵ�.
	
//	(C->S) CHAT: �޴� ���̵�;�޼��� ex)CHAT:bbb;�����
//	(S->C) CHAT: ������ ���̵�;�޼��� ex)CHAT:aaa;����� //�ӼӸ�
	
	public static final String CHAT = "CHAT";

//	(C->S) CHATALL : �޼���
//	(S->C) CHATALL : [���� ���̵�]�޼���
	public static final String CHATALL = "CHATALL";
	
//	(C->S) MESSAGE : �޴� ���̵�;�������� ex)MESSAGE:bbb;���ݾ��?
//	(S->C) MESSAGE : ������ ���̵�;�������� ex)MESSAGE:bbb;���ݾ��?
	public static final String MESSAGE = "MESSAGE";
	
//	(S->C) CHALLIST :aaa;bbb;ccc;��ȣ��;
	public static final String CHATLIST = "CHATLIST";
	
	//������ -> ��������:data(delimiter)delimeter
	public static final String DM =":";
}
