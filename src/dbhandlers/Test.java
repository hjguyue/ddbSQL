package dbhandlers;

import java.sql.ResultSet;
//import java.util.Random;

import dbhandlers.SQLBackend;

public class Test {

	public static void main(String args[]) throws Exception{
		SQLBackend sql = new SQLBackend();
		sql.connectMySQL("166.111.69.204", "sqlsugg", "sqlsugg", "glitter");
		
		int cnt = 0;
		double score = 0;
		String SQL = "select count(*) as cnt, avg(score) as score from score_table";
		ResultSet rs = sql.executeQuery(SQL);
		rs.next();
		cnt = rs.getInt("cnt");
		score=rs.getDouble("score");
		System.out.println(cnt + "\t" + score);
//		for(int i = 0; i < 1000; i++){
//			int a = new Random().nextInt(4)+7;
//			SQL = "insert into score_table values("+a+")";
//			sql.execute(SQL);
//		}
//		while(rs.next()){
//			double coordinate_x = rs.getInt("coordinate_x");
//			double coordinate_y = rs.getInt("coordinate_y");
//			String text = rs.getString("text");
//			System.out.println(coordinate_x + ", " + coordinate_y + ": " + text);
//		}
	}
	
	public void test() throws Exception{
		SQLBackend sql = new SQLBackend();
		sql.connectMySQL("166.111.69.204", "sqlsugg", "sqlsugg", "glitter");
		
		int cnt = 0;
		double score = 0;
		String SQL = "select count(*) as cnt, avg(score) as score from score_table";
		ResultSet rs = sql.executeQuery(SQL);
		rs.next();
		cnt = rs.getInt("cnt");
		score=rs.getDouble("score");
		System.out.println(cnt + "\t" + score);
	}
	
}
