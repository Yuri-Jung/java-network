package ch14;

public class ChatProtocol2 {
//	(C->S) ID : aaa 프로토콜
//	(S->C) CHALLIST : aaa; bbb; ccc; 강호동;
	
	public static final String ID = "ID"; //앞에 프로토콜 붙여서 서버는 그것을 분석한다.
	
//	(C->S) CHAT: 받는 아이디;메세지 ex)CHAT:bbb;밥먹자
//	(S->C) CHAT: 보내는 아이디;메세지 ex)CHAT:aaa;밥먹자 //귓속말
	
	public static final String CHAT = "CHAT";

//	(C->S) CHATALL : 메세지
//	(S->C) CHATALL : [보낸 아이디]메세지
	public static final String CHATALL = "CHATALL";
	
//	(C->S) MESSAGE : 받는 아이디;쪽지내용 ex)MESSAGE:bbb;지금어디?
//	(S->C) MESSAGE : 보내는 아이디;쪽지내용 ex)MESSAGE:bbb;지금어디?
	public static final String MESSAGE = "MESSAGE";
	
//	(S->C) CHALLIST :aaa;bbb;ccc;강호동;
	public static final String CHATLIST = "CHATLIST";
	
	//구분지 -> 프로토콜:data(delimiter)delimeter
	public static final String DM =":";
}
