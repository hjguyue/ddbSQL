package test;

import java.io.PrintStream;
import java.sql.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.ServletActionContext;
import com.opensymphony.xwork2.ActionSupport;

import dbhandlers.SQLBackend;


public class CalAction extends ActionSupport {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	int no1;
	int no2;
	int no3;
	String result;
	
	public void score(){
		
		try {
			SQLBackend sql = new SQLBackend();
			sql.connectMySQL("166.111.69.204", "sqlsugg", "sqlsugg", "glitter");
			String SQL = "select count(*) as cnt, avg(score) as score from score_table";
			ResultSet rs = sql.executeQuery(SQL);
			rs.next();
			int cnt = rs.getInt("cnt");
			double avgScore=rs.getDouble("score");
			
			HttpServletResponse response = ServletActionContext.getResponse();
			HttpServletRequest request =  ServletActionContext.getRequest();
			if(request.getParameter("score") != null){
				String scoreString = request.getParameter("score");
				double score = Double.parseDouble(scoreString);
				if (score > 0 && score <= 10) {
					SQL = "insert into score_table values ("+score+")";
					sql.execute(SQL);
					cnt ++;
					avgScore = ((cnt-1) * avgScore + score) / cnt;
				}
			}
			response.setContentType("text/html");
			PrintStream out;
			out = new PrintStream(response.getOutputStream());
			out.print(cnt + "\t");
			out.printf("%.3f", avgScore);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String execute() {
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("text/html");
			PrintStream out;
			out = new PrintStream(response.getOutputStream());
			out.println("1212313");
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return SUCCESS;
	}

	public double getNo1(){
		return no1;
	}
	
	public void setNo1(int no1){
		this.no1 = no1;
	}

	public int getNo2() {
		return no2;
	}


	public void setNo2(int no2) {
		this.no2 = no2;
	}


	public int getNo3() {
		return no3;
	}


	public void setNo3(int no3) {
		this.no3 = no3;
	}


	public String getResult() {
		return result;
	}


	public void setResult(String result) {
		this.result = result;
	}
}