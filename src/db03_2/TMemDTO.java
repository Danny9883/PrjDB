package db03_2;
/*  TMEM
회원관리
번호     숫자(6)   기본키    자동증가
이름     문자(30)  필수입력
아이디   문자(20)  필수입력  중복방지
암호     문자(20)  필수입력
이메일   문자(320) 중복방지   
가입일   날짜      기본값    오늘
*/


public class TMemDTO {
	// Field
	private int     usernum;
	private String  username; 
	private String  userid; 
	private String  userpwd; 
	private String  email;
	private String  indate;
	
	// Constructor
	public TMemDTO() {}
	public TMemDTO(int usernum, String username, String userid, String userpwd, String email, String indate) {
		this.usernum = usernum;
		this.username = username;
		this.userid = userid;
		this.userpwd = userpwd;
		this.email = email;
		this.indate = indate;
	}
	
	// Getter Setter
	public int getUsernum() {
		return usernum;
	}
	public void setUsernum(int usernum) {
		this.usernum = usernum;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getUserpwd() {
		return userpwd;
	}
	public void setUserpwd(String userpwd) {
		this.userpwd = userpwd;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getIndate() {
		return indate;
	}
	public void setIndate(String indate) {
		this.indate = indate;
	}
	
	// toString
	@Override
	public String toString() {
		return "TMemDTO [usernum=" + usernum + ", username=" + username + ", userid=" + userid + ", userpwd=" + userpwd
				+ ", email=" + email + ", indate=" + indate + "]";
	}
	
	
	

}
