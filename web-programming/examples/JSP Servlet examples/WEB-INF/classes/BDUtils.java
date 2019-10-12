package utils;


import java.sql.*;


public class BDUtils {
	private String driver;
	private String connString;
	private String user;
	private String pass;
	private Statement stmt;
	
	public BDUtils() {
		this.driver = "org.gjt.mm.mysql.Driver";
		this.connString = "jdbc:mysql://www.scs.ubbcluj.ro/sdie1035";
		this.user = "sdie1035";
		this.pass = "sdie1035";
	}

	public void connect() {
		try {
			Class.forName(driver);
			Connection con = DriverManager.getConnection(connString,user,pass);
			stmt = con.createStatement();					
		} catch(Exception ex) {
			System.out.println("eroare la connect:"+ex.getMessage());
		}
	}

	public String showData() {
		String res = new String();
		try {
			//System.out.println("stmt="+stmt);
			ResultSet rs = stmt.executeQuery("select * from Students");
			//System.out.println("rs="+rs);
			while(rs.next()) {
				System.out.println(rs.getInt("id")+"  "+rs.getString("name") + " " +
					rs.getString("password") + " " +rs.getInt("group_id"));
				res = res + rs.getInt("id")+"  "+rs.getString("name") + " " +
					rs.getString("password") + " " +rs.getInt("group_id");
				res = res + "<br/>";
			}
		} catch(Exception ex) {
			System.out.println("eroare la showData:"+ex.getMessage());
		}
		return res;
	}


}
