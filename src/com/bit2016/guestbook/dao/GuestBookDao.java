package com.bit2016.guestbook.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.bit2016.guestbook.vo.GuestBookVo;

public class GuestBookDao {
	private Connection getConnection() throws SQLException {
		Connection conn = null;

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			String url = "jdbc:oracle:thin:@localhost:1521:xe";
			conn = DriverManager.getConnection(url, "webdb", "webdb");
		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패 :" + e);
		}
		return conn;
	}

	public void insert(GuestBookVo vo) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = getConnection();
			String sql = "insert into guestbook values(guestbook_seq.nextval, ?, ?, sysdate, ?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, vo.getName());
			pstmt.setString(2, vo.getPassword());
			pstmt.setString(3, vo.getContent());
			
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("errro: " + e);
		} finally {
			try {
				if(pstmt != null){
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("errro: " + e);
			}
		}
	}

	public List<GuestBookVo> getList() {
		List<GuestBookVo> list = new ArrayList<GuestBookVo>();
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = getConnection();
			stmt = conn.createStatement();
			String sql = "select no, name, password, to_char(reg_date, 'yyyy-mm-dd hh:mi:ss'), content from guestbook order by no desc";
			rs = stmt.executeQuery(sql);

			while (rs.next()) {
				Long no = rs.getLong(1);
				String name = rs.getString(2);
				String password = rs.getString(3);
				String regDate = rs.getString(4);
				String content = rs.getString(5);
				

				GuestBookVo vo = new GuestBookVo();
				vo.setNo(no);
				vo.setName(name);
				vo.setPassword(password);
				vo.setRegDate(regDate);
				vo.setContent(content);

				list.add(vo);
				
				
			}
		} catch (SQLException e) {
			System.out.println("error: " + e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("error: " + e);

			}
		}

		return list;
	}
	
	public boolean delete( Long no , String password ){
		Connection conn = null;
		PreparedStatement pstmt = null;
		int result = 0;
		
		try{
			conn = getConnection();
			String sql = "delete from guestbook where no = ? and password = ?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setLong(1, no);
			pstmt.setString(2, password);
			result = pstmt.executeUpdate();
			
		}catch(SQLException e){
			System.out.println("error: "+e);
		}finally{
			try{
				if(pstmt!=null){
					pstmt.close();
				}
				if(conn!=null){
					conn.close();
				}
			}catch(SQLException e){
				System.out.println("error: "+e);
			}
		}
		
		return result == 1;
	}
}
