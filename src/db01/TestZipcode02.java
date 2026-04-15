package db01;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TestZipcode02 {
	
	// 연결 문자열 : Connection String
	private static  String  driver = "oracle.jdbc.OracleDriver";
	private static  String  url    = "jdbc:oracle:thin:@127.0.0.1:1521:xe";
	private static  String  dbuid  = "sky";
	private static  String  dbpwd  = "1234";
	

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		Class.forName( driver );
		Connection  conn  = DriverManager.getConnection(url, dbuid, dbpwd);
		
		Statement   stmt  = conn.createStatement();
		String      sql   = 
				"SELECT ZIPCODE, SIDO, GUGUN, DONG, NVL(BUNJI,' ') bj , SEQ "
				+ " FROM ZIPCODE "
				+ " WHERE DONG LIKE '%롯데백화점%' ";
		System.out.println( sql );
		ResultSet   rs    = stmt.executeQuery(sql);
		
		while( rs.next() != false ) {
			System.out.print( rs.getString(1)   + ",");
			System.out.print( rs.getString(2)   + ",");
			System.out.print( rs.getString(3)   + ",");
			System.out.print( rs.getString(4)   + ",");
			System.out.print( rs.getString(5)   + ",");
			System.out.print( rs.getInt(6)             );
			System.out.println();
			/*
			System.out.print( rs.getString("ZIPCODE") + ",");
			System.out.print( rs.getString("SIDO")    + ",");
			System.out.print( rs.getString("GUGUN")   + ",");
			System.out.print( rs.getString("DONG")    + ",");
			System.out.print( rs.getString("bj")      + ",");
			System.out.print( rs.getInt("SEQ")             );
			System.out.println();
			*/
		}
		
		
		
		
		rs.close();
		stmt.close();
		conn.close();
	}

}
