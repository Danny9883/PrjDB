package db03_2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class TestTMem {
	
	// 연결문자열
	private static String driver  = "oracle.jdbc.OracleDriver";
	private static String url     = "jdbc:oracle:thin:@localhost:1521:xe";
	private static String dbuid   = "sky";
	private static String dbpwd   = "1234";

	static Scanner in       =  new Scanner(System.in);

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
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
			
			TMemDTO   tmem   = null;
			int       aftcnt;
			String    uid;
			
			switch (choice) {
			case "1" :    // 회원 목록
			    ArrayList<TMemDTO> userList = getTMemList();
			    displayList(userList);
				break;
			case "2" :    // 회원 조회 (아이디)
				System.out.println("조회할 아이디를 입력하세요");
				uid              = in.nextLine();
				tmem             = getTMem( uid );
				display( tmem );
				break;
			case "3" :    // 회원 추가
				tmem             = inputData();
				aftcnt           = addTMem(tmem);
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
				aftcnt           = updateTMem( uid );
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
				aftcnt           = deleteTMem(uid);
				if (aftcnt !=0 )
					System.out.println( uid + " 계정이 삭제되었습니다.");
				else
					System.out.println("회원이 없거나, 삭제되지 않았습니다.");
				System.out.println("---- Press Enter key ---- ");
				in.nextLine();
				break;
			case "q" :    // 종료
				System.out.println("프로그램을 종료합니다.");
				System.exit(0);
				break;
			}
			
			
		} while( true );//do while
		
		
	

	}//main




	// 1. 회원목록 조회
	private static ArrayList<TMemDTO> getTMemList() throws ClassNotFoundException, SQLException {
		Class.forName(driver);
		Connection          conn     = DriverManager.getConnection(url, dbuid, dbpwd);
		String              sql      = " SELECT * FROM TMEM ";
		sql                         += " ORDER BY USERID ASC ";
		PreparedStatement   pstmt    = conn.prepareStatement(sql);
		ResultSet           rs       = pstmt.executeQuery();
		
		ArrayList<TMemDTO> userList  = new ArrayList<>(); 
		while ( rs.next() ) {
			int       usernum  = rs.getInt("usernum");
			String    username = rs.getString("username");
			String    userid   = rs.getString("userid");
			String    userpwd  = rs.getString("userpwd");
			String    email    = rs.getString("email");
			String    indate   = rs.getString("indate");
			TMemDTO   tmem     = new TMemDTO(usernum, username, userid, userpwd, email, indate);
			userList.add( tmem );
		}
		
		
		rs.close();
		pstmt.close();
		conn.close();
		
		return userList;
	}
	
	// 2. 입력받은 회원 조회
	private static TMemDTO getTMem(String uid) throws ClassNotFoundException, SQLException {
		Class.forName(driver);
		Connection        conn   = DriverManager.getConnection(url, dbuid, dbpwd);
		String            sql    = "SELECT * FROM TMEM  WHERE  UPPER(USERID) = ? ";
		PreparedStatement pstmt  = conn.prepareStatement(sql);
		pstmt.setString(1, uid.toUpperCase());
		
		ResultSet         rs     = pstmt.executeQuery();
		TMemDTO tmem = null;
		if ( rs.next() ) {  // 해당자료가 있는경우
			int       usernum  = rs.getInt("usernum");
			String    username = rs.getString("username");
			String    userid   = rs.getString("userid");
			String    userpwd  = rs.getString("userpwd");
			String    email    = rs.getString("email");
			String    indate   = rs.getString("indate");
			tmem               = new TMemDTO(usernum, username, userid, userpwd, email, indate);
		} else {            // 해당자료가 없는경우 : primary key
			
		}
		
		pstmt.close();
		conn.close();
		
		return tmem;
	}
	
	// 3. 입력받은 회원 추가 - DB
	private static int addTMem(TMemDTO tmem) throws ClassNotFoundException, SQLException {
		Class.forName(driver);
		Connection        conn   =  DriverManager.getConnection(url, dbuid, dbpwd);
		
		String            sql    = "";
		sql     +=  " INSERT  INTO  TMEM VALUES ((SELECT NVL(MAX(USERNUM),0)+1 FROM TMEM) "
				+ ", ? , ? , ? , ? , SYSDATE ) ";
		PreparedStatement pstmt  = conn.prepareStatement(sql);
		pstmt.setString(1, tmem.getUsername() );
		pstmt.setString(2, tmem.getUserid() );
		pstmt.setString(3, tmem.getUserpwd());
		pstmt.setString(4, tmem.getEmail());
		int               aftcnt = 0;
		
		try {
			 aftcnt = pstmt.executeUpdate();
		} catch (Exception e) {
			System.out.println("중복된 아이디 입니다. 다른 아이디를 사용해주세요.");
			return 0;
		}
		
		pstmt.close();
		conn.close();
		
		return            aftcnt;
	}
	
	// 4. 회원 정보 수정
	private static int updateTMem(String uid) throws ClassNotFoundException, SQLException {
		Class.forName(driver);
		Connection        conn   =  DriverManager.getConnection(url, dbuid, dbpwd);
		TMemDTO           tmem   = getTMem(uid);
		if (tmem == null)
			return 0;
		String            mempwd = tmem.getUserpwd();
		int               pwdcnt = 0;
		while (true) {
			if(pwdcnt == 3) {
				System.out.println("비밀번호 3회 오류. 처음으로 돌아갑니다.");
				return 0;
			}
			System.out.println("비밀번호를 입력하세요. 3회 실패시 종료됩니다. / 현재 "+ pwdcnt+"회 실패");
			String  pwd = in.nextLine();
			if (pwd.equals(mempwd))
				break;
			else {
				System.out.println("비밀번호가 틀립니다.");
				pwdcnt++;
			}
		}
		System.out.println("회원이름변경 : 1 / 이메일변경 : 2 / 패스워드 변경 : 3");
		String            cng    = in.nextLine();
		String            sql    = "";
		
		if ( cng.equals("1") ) {
			sql     +=  " UPDATE TMEM SET USERNAME = ? WHERE USERID = ? ";
		} else if (cng.equals("2") ) {
			sql     +=  " UPDATE TMEM SET EMAIL = ? WHERE USERID = ? ";
		} else if (cng.equals("3") ) {
			sql     +=  " UPDATE TMEM SET USERPWD = ? WHERE USERID = ? ";
		} else {
			System.out.println("잘못된 입력입니다. 처음으로 돌아갑니다.");
			return 0;
		}
		System.out.println("변경할 내용을 입력하세요 : ");
		String            txt    = in.nextLine();
		if (cng.equals("3")) {
			System.out.println("다시한번 입력하세요.");
			String chkpwd = in.nextLine();
			if (!txt.equals(chkpwd)) {
				System.out.println("비밀번호 재확인 불일치, 처음으로 돌아갑니다.");
				return 0;
			}
		}
		PreparedStatement pstmt  = conn.prepareStatement(sql);
		pstmt.setString(1, txt);
		pstmt.setString(2, uid.toUpperCase());
		
		int               aftcnt = pstmt.executeUpdate();
		
		pstmt.close();
		conn.close();
		
		return            aftcnt;
	}
	
	// 5. 회원 정보 삭제
	private static int deleteTMem(String uid) throws ClassNotFoundException, SQLException {
		Class.forName(driver);
		Connection        conn   =  DriverManager.getConnection(url, dbuid, dbpwd);
		TMemDTO           tmem   = getTMem(uid);
		if (tmem == null)
			return 0;
		String            mempwd = tmem.getUserpwd();
		int               pwdcnt = 0;
		while (true) {
			if(pwdcnt == 3) {
				System.out.println("비밀번호 3회 오류. 처음으로 돌아갑니다.");
				return 0;
			}
			System.out.println("비밀번호를 입력하세요. 3회 실패시 종료됩니다. / 현재 "+ pwdcnt+"회 실패");
			String  pwd = in.nextLine();
			if (pwd.equals(mempwd))
				break;
			else {
				System.out.println("비밀번호가 틀립니다.");
				pwdcnt++;
			}
		}
		
		System.out.println("정말 ID를 삭제하시겠습니까? Y / N");
		String            chk    = in.nextLine();
		if (chk.toUpperCase().equals("N") ) {
			return 0;
		} else if (!(chk.toUpperCase().equals("Y"))) {
			System.out.println("입력이 잘못되었습니다. 처음으로 돌아갑니다");
			return 0;
		}
			
		String            sql    = "";
		sql     +=  " DELETE FROM TMEM WHERE UPPER(USERID) = ? ";		
		PreparedStatement pstmt  = conn.prepareStatement(sql);
		pstmt.setString(1, uid.toUpperCase());

		
		int               aftcnt = pstmt.executeUpdate();
		
		pstmt.close();
		conn.close();
		
		return            aftcnt;
	}
	
//-------------------------------------------------------------------------
	// 1) 전체 목록 출력 
	private static void displayList(ArrayList<TMemDTO> userList) {
		if ( userList.size() == 0 ) {
			System.out.println("조회한 자료가 없습니다");
			return;
		}
		
		String     fmt  = "";
		String     msg  = "";
		for (TMemDTO tmem : userList) {
			int      usernum   = tmem.getUsernum();
			String   username  = tmem.getUsername();
			String   userid    = tmem.getUserid();
			String   email     = tmem.getEmail();
			String   indate    = tmem.getIndate();
			msg  = """
			%d  %s  %s  %s  %s
					""".formatted(usernum, username, userid, email, indate); 
			System.out.print( msg );
		}
		System.out.println("---- Press Enter key ---- ");
		in.nextLine();
	}
	
	// 2) 조회한 회원 목록 출력
	private static void display(TMemDTO tmem) {
		if ( tmem == null )
			System.out.println("조회한 자료가 없습니다.");
		else {
			String msg = String.format("%d  %s  %s  %s  %s", 
					tmem.getUsernum(), tmem.getUsername(), tmem.getUserid(),
					tmem.getEmail(), tmem.getIndate() );
			System.out.println(msg);
		}
		System.out.println("---- Press Enter key ---- ");
		in.nextLine();
		
	}
	
	// 3) 추가할 데이터 입력
	private static TMemDTO inputData() {
		System.out.println("아이디: ");
		String  userid   =  in.nextLine().toUpperCase();
		String userpwd = "";
		while (true) {
			System.out.println("비밀번호 설정:");
			userpwd = in.nextLine();
			System.out.println("비밀번호 재확인:");
			String tpwd = in.nextLine();
			if(userpwd.equals(tpwd))
				break;
			else
				System.out.println("비밀번호가 일치하지 않습니다. 다시 입력해주세요");
		}
		System.out.println("이름: ");
		String  username =  in.nextLine();
		System.out.println("이메일: ");
		String  email    =  in.nextLine();
		
		TMemDTO   tmem   = new TMemDTO(0, username, userid, userpwd, email, email);
		return    tmem;
	}


}//TestTMem class end
