package ch14;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ChatMgr3 {
	private DBConnectionMgr pool;
	
	public ChatMgr3() {
		pool = DBConnectionMgr.getInstance();
	}
	
//	�α���
	public boolean loginChk(String id, String pwd) {
		Connection con = null; //.sql���� �޾ƿ;��Ѵ�.ctrl space�� ��
		PreparedStatement pstmt = null; //u,d,s���� �ʿ�
		ResultSet rs = null; //select���� �ʿ�
		String sql = null;
		boolean flag = false;
		try {
			con = pool.getConnection(); // Connection ������
			sql = "select id from tblRegister where id = ? and pwd=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id); //ù��° ?�� id ='aaa' �ڹ�, sql���� �������� ���� �ٸ���.
			pstmt.setString(2, pwd); //�ι�° ?�� pwd ='1234'
			rs = pstmt.executeQuery(); //���� ���๮
			flag  = rs.next(); //������� ������ true, ������ false->����� ����
			
		} catch (Exception e) {                               
			e.printStackTrace();
		} finally {
//			con�� �ݳ�,  pstmt, rs�� �� �� close
			pool.freeConnection(con, pstmt, rs);
		}
		return flag;
	}

}
