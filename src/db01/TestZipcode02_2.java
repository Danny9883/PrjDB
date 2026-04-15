package db01;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TestZipcode02_2 {
	
	// 연결 문자열 : Connection String
	private static  String  driver = "oracle.jdbc.OracleDriver";
	private static  String  url    = "jdbc:oracle:thin:@127.0.0.1:1521:xe";
	private static  String  dbuid  = "hr";
	private static  String  dbpwd  = "1234";

	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		Class.forName(driver);
		Connection   conn  = DriverManager.getConnection(url, dbuid, dbpwd);
		
		Statement  stmt    = conn.createStatement();
		String     sql     = "";
		
		ResultSet  rs      = stmt.executeQuery(sql);
		
		
		
		
		rs.close();
		stmt.close();
		conn.close();
		
		
	}

}
