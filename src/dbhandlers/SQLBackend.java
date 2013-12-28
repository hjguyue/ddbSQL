package dbhandlers;
import java.sql.*;

public class SQLBackend {
	Connection conn;
	String dbName;
	
	public SQLBackend() {
        conn = null;
	}
	 
	public void connectMySQL (String server, String userName, String password, String dbName) 
		throws InstantiationException, SQLException, 
		ClassNotFoundException, IllegalAccessException{
		Class.forName("com.mysql.jdbc.Driver");
		conn = DriverManager.getConnection("jdbc:mysql://" + 
				server + 
				"/" + dbName + 
				"?useUnicode=true&characterEncoding=gbk&jdbcCompliantTruncation=false", 
				userName, password);
		this.dbName = dbName;
		this.useDB(dbName);
	}
	
	public void disconnectMySQL () throws SQLException{
		//System.out.println("disconnect...");
		conn.close();
		conn = null;
        //System.out.println("Database connection terminated");
	}
	
	public void useDB (String dbName) throws SQLException {
		String stat = "USE " + dbName;
		this.execute(stat);
	}

	public void execute (String statement) throws SQLException{
		//DebugPrinter.println("Now Execute: \n" + statement);
		Statement stat = this.conn.createStatement();
		stat.execute(statement);
	}
	
	public int executeUpdate (String statement) throws SQLException {
		//DebugPrinter.println("Now Execute: \n" + statement);
		Statement stat = this.conn.createStatement();
		return stat.executeUpdate(statement);
	}
	
	public ResultSet executeQuery (String statement) throws Exception {
		ResultSet rs = null;
		Statement stat = this.conn.createStatement();
		rs = stat.executeQuery(statement);
		return rs;
	}
}
