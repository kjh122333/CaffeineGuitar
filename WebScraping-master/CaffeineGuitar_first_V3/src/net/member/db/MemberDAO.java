package net.member.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class MemberDAO {
	static Connection con;
	static PreparedStatement pstmt;
	static ResultSet rs;
	
	public MemberDAO() {
		try{
			Context init = new InitialContext();
	  		DataSource ds = (DataSource) init.lookup("java:comp/env/jdbc/OracleDB");
	  		con = ds.getConnection();
		}catch(Exception ex){
			System.out.println("DB ���� ���� : " + ex);
			return;
		}
	}
	
	//����� �Ӹ��� ���ϱ�.
	public int getListCount() {
		int x= 0;
		
		try{
			pstmt=con.prepareStatement("select count(*) from cgmember");
			rs = pstmt.executeQuery();
			
			if(rs.next()){
				x=rs.getInt(1);
			}
		}catch(Exception ex){
			System.out.println("getListCount ����: " + ex);			
		}finally{
			if(rs!=null) try{rs.close();}catch(SQLException ex){}
			if(pstmt!=null) try{pstmt.close();}catch(SQLException ex){}
		}
		return x;
	}
	
	//��� ��� ����.	
	public List getMemberList(int page,int limit){
		String member_list_sql="select * from "+
		"(select rownum rnum,ID,EMAIL,"+
		"PASSWORD,ORDERNO,PAYDAY,LECTURE"+
		"PAY, PAYWAY, STATUS"+
		"where rnum>=? and rnum<=?";
		
		List list = new ArrayList();
		
		int startrow=(page-1)*10+1; //�б� ������ row ��ȣ.
		int endrow=startrow+limit-1; //���� ������ row ��ȣ.		
		try{
			pstmt = con.prepareStatement(member_list_sql);
			pstmt.setInt(1, startrow);
			pstmt.setInt(2, endrow);
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				MemberBean member = new MemberBean();
				member.setID(rs.getString("ID"));
				member.setEMAIL(rs.getString("EMAIL"));
				member.setPASSWORD(rs.getString("PASSWORD"));
				member.setORDERNO(rs.getInt("ORDERNO"));
				member.setPAYDAY(rs.getString("PAYDAY"));
				member.setLECTURE(rs.getString("LECTURE"));
				member.setPAY(rs.getString("PAY"));
				member.setPAYWAY(rs.getString("PAYWAY"));
				member.setSTATUS(rs.getString("STATUS"));
				list.add(member);
			}
			
			return list;
		}catch(Exception ex){
			System.out.println("getMEMBERList ���� : " + ex);
		}finally{
			if(rs!=null) try{rs.close();}catch(SQLException ex){}
			if(pstmt!=null) try{pstmt.close();}catch(SQLException ex){}
		}
		return null;
	}
	
	//��� ���� ����.
	public MemberBean getDetail(String email) throws Exception{
		MemberBean member = null;
		try{
			pstmt = con.prepareStatement(
					"select * from CGMEMBER where EMAIL = ?");
			pstmt.setString(1, email);
			
			rs= pstmt.executeQuery();
			
			if(rs.next()){
				member = new MemberBean();
				member.setID(rs.getString("ID"));
				member.setEMAIL(rs.getString("EMAIL"));
				member.setPASSWORD(rs.getString("PASSWORD"));
				member.setORDERNO(rs.getInt("ORDERNO"));
				member.setPAYDAY(rs.getString("PAYDAY"));
				member.setLECTURE(rs.getString("LECTURE"));
				member.setPAY(rs.getString("PAY"));
				member.setPAYWAY(rs.getString("PAYWAY"));
				member.setSTATUS(rs.getString("STATUS"));
			}
			return member;
		}catch(Exception ex){
			System.out.println("getDetail ���� : " + ex);
		}finally{
			if(rs!=null)try{rs.close();}catch(SQLException ex){}
			if(pstmt !=null)try{pstmt.close();}catch(SQLException ex){}
		}
		return null;
	}
	
	//��� ���.
	public static boolean memberInsert(MemberBean member){
		String sql="";
		
		int result=0;
		
		try{
			
			sql="insert into CGMEMBER (ID,EMAIL,";
			sql+="PASSWORD)"+
				"values(?,?,?)";
			
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, member.getID());
			pstmt.setString(2, member.getEMAIL());
			pstmt.setString(3, member.getPASSWORD());
			
			result=pstmt.executeUpdate();
			if(result==0)return false;
			
			return true;
		}catch(Exception ex){
			System.out.println("MemberInsert ���� : "+ex);
		}finally{
			if(rs!=null) try{rs.close();}catch(SQLException ex){}
			if(pstmt!=null) try{pstmt.close();}catch(SQLException ex){}
		}
		return false;
	}
	
	//��� ����.
	public boolean memberDelete(String id){
		String member_delete_sql="delete from CGMEMBER where ID=?";
		
		int result=0;
		
		try{
			pstmt=con.prepareStatement(member_delete_sql);
			pstmt.setString(1, id);
			result=pstmt.executeUpdate();
			if(result==0)return false;
			
			return true;
		}catch(Exception ex){
			System.out.println("memberDelete ���� : "+ex);
		}finally{
			try{
				if(pstmt!=null)pstmt.close();
			}catch(Exception ex) {}
		}
		
		return false;
	}

}
