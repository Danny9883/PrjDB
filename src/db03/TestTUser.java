package db03;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class TestTUser {
	// 연결문자열
	private static String driver  = "oracle.jdbc.OracleDriver";
	private static String url     = "jdbc:oracle:thin:@localhost:1521:xe";
	private static String dbuid   = "sky";
	private static String dbpwd   = "1234";

	static Scanner in       =  new Scanner(System.in);
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		// CRUD 예제 , Create , Read , Update , Delete
		do {
			// 화면출력
			System.out.println("==========================");
			System.out.println("        회원 정보         ");
			System.out.println("==========================");
			System.out.println("1. 회원 목록 ");
			System.out.println("2. 회원 조회 ");
			System.out.println("3. 회원 추가 ");
			System.out.println("4. 회원 수정 ");
			System.out.println("5. 회원 삭제 ");
			System.out.println("q. 종료 ");
			
			System.out.println("선택 : ");
			String  choice   =  in.nextLine();
			
			TUserDTO  tuser   = null;
			int       aftcnt;
			String    uid;
			
			switch (choice) {
			case "1" :    // 회원 목록
			    ArrayList<TUserDTO> userList = getTUserList();
			    displayList(userList);
				break;
			case "2" :    // 회원 조회 (아이디)
				System.out.println("조회할 아이디를 입력하세요");
				uid              = in.nextLine();
				tuser            = getTUser( uid );
				display( tuser );
				break;
			case "3" :    // 회원 추가
				tuser            = inputData();
				aftcnt           = addTUser(tuser);
				if (aftcnt != 0)
					System.out.println(aftcnt + "건 저장되었습니다.");
				else
					System.out.println("정상적으로 처리되지 않았습니다.");
				System.out.println("---- Press Enter key ---- ");
				in.nextLine();
				break;   
			case "4" :    // 회원 수정
				System.out.println("수정할 아이디를 입력하세요");
				uid              = in.nextLine();
				aftcnt           = updateTUser( uid );
				if ( aftcnt !=0 )
					System.out.println(uid + " 의 정보가 변경 되었습니다.");
				else
					System.out.println("회원이 없거나, 수정되지 않았습니다.");
				System.out.println("---- Press Enter key ---- ");
				in.nextLine();
				break;
			case "5" :    // 회원 삭제
				System.out.println("삭제할 회원의 아이디를 입력하세요 : ");
				uid              = in.nextLine();
				aftcnt           = deleteTUser(uid);
				System.out.println( aftcnt + "건 삭제되었습니다.");
				System.out.println("---- Press Enter key ---- ");
				in.nextLine();
				break;
			case "q" :    // 종료
				System.out.println("프로그램을 종료합니다.");
				System.exit(0);
				break;
			}
			
			
		} while( true ); // 무한반복 : 무한 루프 infinity roof 
		
		
	}// main





	// 1. 전체 목록 조회 - DB에서
	private static ArrayList<TUserDTO> getTUserList() throws SQLException, ClassNotFoundException {
		Class.forName(driver);
		Connection          conn     = DriverManager.getConnection(url, dbuid, dbpwd);
		String              sql      = " SELECT * FROM TUSER ";
		sql                         += " ORDER BY USERID ASC ";
		PreparedStatement   pstmt    = conn.prepareStatement(sql);
		ResultSet           rs       = pstmt.executeQuery();
		
		ArrayList<TUserDTO> userList = new ArrayList<>(); 
		while ( rs.next() ) {
			String    userid   = rs.getString("userid");
			String    username = rs.getString("username");
			String    email    = rs.getString("email");
			TUserDTO  tuser    = new TUserDTO(userid, username, email);
			userList.add( tuser );
		}
		
		
		rs.close();
		pstmt.close();
		conn.close();
		
		return userList;
	}

	// 2. 입력받은 아이디로 한줄을 db 에서 조회한다
	private static TUserDTO getTUser(String uid) throws ClassNotFoundException, SQLException {
		Class.forName(driver);
		Connection        conn   = DriverManager.getConnection(url, dbuid, dbpwd);
		String            sql    = "SELECT * FROM TUSER  WHERE  UPPER(USERID) = ? ";
		PreparedStatement pstmt  = conn.prepareStatement(sql);
		pstmt.setString(1, uid.toUpperCase());
		
		ResultSet         rs     = pstmt.executeQuery();
		TUserDTO tuser = null;
		if ( rs.next() ) {  // 해당자료가 있는경우
			String   userid    = rs.getString("USERID");
			String   username  = rs.getString("USERNAME");
			String   email     = rs.getString("EMAIL");
			tuser              = new TUserDTO(userid, username, email);
		} else {            // 해당자료가 없는경우 : primary key
			
		}
		
		pstmt.close();
		conn.close();
		
		return tuser;
	}

	// 3. DB에 insert 한다
	private static int addTUser(TUserDTO tuser) throws SQLException, ClassNotFoundException {
		Class.forName(driver);
		Connection        conn   =  DriverManager.getConnection(url, dbuid, dbpwd);
		
		String            sql    = "";
		sql     +=  " INSERT  INTO  TUSER VALUES (? , ? , ?) ";
		PreparedStatement pstmt  = conn.prepareStatement(sql);
		pstmt.setString(1, tuser.getUserid());
		pstmt.setString(2, tuser.getUsername());
		pstmt.setString(3, tuser.getEmail());
		int               aftcnt = 0;
		
		try {
			 aftcnt = pstmt.executeUpdate();
		} catch (Exception e) {
			System.out.println("중복된 아이디 입니다.");
			return 0;
		}
		
		pstmt.close();
		conn.close();
		
		return            aftcnt;
		
	}
	
	// 4. 회원 정보 수정 - DB에서
	private static int updateTUser(String uid) throws ClassNotFoundException, SQLException {
		Class.forName(driver);
		Connection        conn   =  DriverManager.getConnection(url, dbuid, dbpwd);
		System.out.println("회원이름변경 : 1 / 이메일변경 : 2");
		String            cng    = in.nextLine();
		String            sql    = "";
		
		if ( cng.equals("1") ) {
			sql     +=  " UPDATE TUSER SET USERNAME = ? WHERE USERID = ? ";
		} else if (cng.equals("2") ) {
			sql     +=  " UPDATE TUSER SET EMAIL = ? WHERE USERID = ? ";
		} else {
			System.out.println("잘못된 입력입니다. 처음으로 돌아갑니다.");
			return 0;
		}
		System.out.println("변경할 내용을 입력하세요 : ");
		String            txt    = in.nextLine();
		PreparedStatement pstmt  = conn.prepareStatement(sql);
		pstmt.setString(1, txt);
		pstmt.setString(2, uid.toUpperCase());
		
		int               aftcnt = pstmt.executeUpdate();
		
		pstmt.close();
		conn.close();
		
		return            aftcnt;
	}
	
	// 5. DB에서 삭제
	private static int deleteTUser(String uid) throws SQLException, ClassNotFoundException {
		Class.forName(driver);
		Connection        conn   =  DriverManager.getConnection(url, dbuid, dbpwd);
		String            sql    = "";
		sql     +=  " DELETE FROM TUSER WHERE UPPER(USERID) = ? ";
		PreparedStatement pstmt  = conn.prepareStatement(sql);
		pstmt.setString(1, uid.toUpperCase());
		
		int               aftcnt = pstmt.executeUpdate();
		
		pstmt.close();
		conn.close();
		
		return            aftcnt;
		
	}

	
//-------------------------------------------------------------------
	// 데이터를 키보드로 입력받는다 
	private static TUserDTO inputData() {
		System.out.println("아이디: ");
		String  userid   =  in.nextLine().toUpperCase();
		System.out.println("이름: ");
		String  username =  in.nextLine();
		System.out.println("이메일: ");
		String  email    =  in.nextLine();
		
		TUserDTO  tuser  = new TUserDTO(userid, username, email);
		return    tuser;
		
	}
	
	// TUser 한줄을 출력한다
	private static void display(TUserDTO tuser) {
		if ( tuser == null )
			System.out.println("조회한 자료가 없습니다.");
		else {
			String msg = String.format("%s %s %s", 
					tuser.getUserid(), tuser.getUsername(), tuser.getEmail() );
			System.out.println(msg);
		}
		System.out.println("---- Press Enter key ---- ");
		in.nextLine();
	}
	
	// 전체 목록을 출력한다
	private static void displayList(ArrayList<TUserDTO> userList) {
		if ( userList.size() == 0 ) {
			System.out.println("조회한 자료가 없습니다");
			return;
		}
		
		String     fmt  = "";
		String     msg  = "";
		for (TUserDTO tuser : userList) {
			String   userid    = tuser.getUserid();
			String   username  = tuser.getUsername();
			String   email     = tuser.getEmail();
			msg  = """
			%s %s %s
					""".formatted(userid, username, email); // java template 문자열
			System.out.print( msg );
		}
		System.out.println("---- Press Enter key ---- ");
		in.nextLine();
	}

}//TestTUser
