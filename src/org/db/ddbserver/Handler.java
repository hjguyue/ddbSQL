package org.db.ddbserver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

public class Handler {
	public Handler() {

	}

	/**
	 * 
	 * @param r1
	 * @param r2
	 * @param s
	 *            select_int book.copies>5000 from book
	 * @return
	 */
	public SqlResult join(SqlResult r1, SqlResult r2, String s) {
		return null;
	}

	/**
	 * 
	 * @param r
	 * @param s
	 * @return
	 */
	public SqlResult project(SqlResult r, String s) {
		return null;
	}

	//获得MySql数据库连接
    private static Connection getMySqlConnection(){
        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();    //加载驱动
            //String url = "jdbc:mysql://"+ip+":3306/"+dbName+"?useUnicode=true&characterEncoding=utf8";
           // conn = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("MySql驱动没找到");
        //} catch (SQLException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }        
        return conn;
    }
	
	/**
	 * 
	 * @param table
	 * @param where
	 * @return
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public SqlResult select(String table, String where) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Connection con;
		Statement statement;
		GDD gdd = new GDD();
		try {
			con = DriverManager.getConnection(gdd.host[0], gdd.username, gdd.password);
		
		statement = con.createStatement();
		String query = "select * from orders";
		ResultSet resultSet = statement.executeQuery( query );
		ResultSetMetaData rsmd = resultSet.getMetaData() ; 
		int colomn_num = rsmd.getColumnCount();
		SqlResult sr = new SqlResult();
		for(int i = 0; i < colomn_num; i++) {
			SqlColomn sc = new SqlColomn(rsmd.getColumnName(i + 1));	
			sr.getL().add(sc);
		}
		while(resultSet.next()) {
			for(int i = 0; i < colomn_num; i++) {
				sr.getL().get(i).getL().add(resultSet.getString(i + 1));
			}
		}
		
		resultSet.last();
		System.out.println(resultSet.getRow());
		
		for(int i = 0; i < 10; i++) {
			for(int j = 0; j < colomn_num; j++) {
				System.out.print(sr.getL().get(j).getL().get(i) + " ");
			}
			System.out.println();
		}
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private void displayResultSet(ResultSet rs) throws SQLException {
		// 定位到达第一条记录
		boolean moreRecords = rs.next();
		// 如果没有记录，则提示一条消息
		if (!moreRecords) {
			System.out.print("no record");
			return;
		}
		Vector columnHeads = new Vector();
		Vector rows = new Vector();
		try {
			// 获取字段的名称
			ResultSetMetaData rsmd = rs.getMetaData();
			for (int i = 1; i <= rsmd.getColumnCount(); ++i)
				columnHeads.addElement(rsmd.getColumnName(i));
			// 获取记录集
			do {
				rows.addElement(getNextRow(rs, rsmd));
			} while (rs.next());

		} catch (SQLException sqlex) {
			sqlex.printStackTrace();
		}
	}

	private Vector getNextRow(ResultSet rs, ResultSetMetaData rsmd)
			throws SQLException {
		Vector currentRow = new Vector();
		for (int i = 1; i <= rsmd.getColumnCount(); ++i)
			currentRow.addElement(rs.getString(i));
		// 返回一条记录
		return currentRow;
	}

	/**
	 * 
	 * @param r1
	 * @param r2
	 * @return
	 */
	public SqlResult union(SqlResult r1, SqlResult r2) {
		return null;
	}
}
