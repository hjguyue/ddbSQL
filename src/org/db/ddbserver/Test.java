package org.db.ddbserver;

import java.io.IOException;
import java.io.PrintStream;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import dbhandlers.SQLBackend;

public class Test {

	/**
	 * @param args
	 * @throws ParseException
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		RmiServer s1, s2, s3, s4;
		try {
			Registry registry;
			s1 = new RmiServer(1);
			registry = LocateRegistry.createRegistry(20001);
			registry.rebind("s1", s1);
			s2 = new RmiServer(2);
			registry = LocateRegistry.createRegistry(20002);
			registry.rebind("s2", s2);
			s3 = new RmiServer(3);
			registry = LocateRegistry.createRegistry(20003);
			registry.rebind("s3", s3);
			s4 = new RmiServer(4);
			registry = LocateRegistry.createRegistry(20004);
			registry.rebind("s4", s4);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.print("start server failed");
		}
		System.out.println("----------------------");
		System.out.println("Servers are now online");
		System.out.println("----------------------");
		
		
		
		//SQLParser1 sp = new SQLParser1();
		List<String> ls = new ArrayList<String>();
		//query 1
		//ls.add("select customer.name " + "from Customer");
		//query 2
		//ls.add("select publisher.name from publisher");
		//ls.add("select publisher.name  "+ "from Publisher where publisher.id > 15000 and publisher.nation = 'usa'");
		//query 3
		//ls.add("select Book.title  " + "from Book  " + "where copies > 5000");
		//query 4
		//ls.add("select customer_id, quantity " + "from Orders "+ "where quantity < 8");
		/*ls.add("select  Book.title,Book.copies, Publisher.name,Publisher.nation "
				+ "from Book,Publisher "
				+ "where Book.id > 21000 and Publisher.id > 100 and Book.publisher_id=Publisher.id and Publisher.nation='USA' and Book.copies > 1000");*/
		//query 5
		/*ls.add("select Book.title,Book.copies,Publisher.name,Publisher.nation"
				+"from Book,Publisher"
				+"where Book.publisher_id=Publisher.id and Publisher.nation='USA' and Book.copies>1000");*/
		//query 6
		/*ls.add("select Customer.name,Orders.quantity "
				+ "from Customer,Orders "
				+ "where 		Customer.id=Orders.customer_id");*/
		
		//query 7
		/*ls.add("select Customer.name,Customer.rank,Orders.quantity"
				+"from Customer,Orders"
				+"where Customer.id=Orders.customer_id and Customer.rank=1");*/
				
				//query 8
				/*ls.add("Select 		Customer.name ,Orders.quantity,Book.title "
					+ "from 		Customer,Orders,Book "
						+ "where 		Customer.id=Orders.customer_id and  Book.id=Orders.book_id and  Customer.rank=1 and Book.copies>5000");*/
		
		ls.add("select Customer.name,Book.title,Publisher.name,Orders.quantity "
		+"from Customer,Book,Publisher,Orders "
		+"where Customer.id=Orders.customer_id and Book.id=Orders.book_id " +
		"and Book.publisher_id=Publisher.id and Book.id>220000 and "
		+"Publisher.nation='USA' and Orders.quantity>1");
		int i = 0;
		for (String sql : ls) {
			i++;
			SqlSentence s = new SqlSentence(sql);
			// System.out.println(new SQLParser(sql));
			// System.out.println(sp.parseSql(sql));
			SqlTree st = new SqlTree();
			// st.getRoot().setDetail("SqlTree #" + i);
			long begin=System.currentTimeMillis();
			st.buildTree(s);
			System.out.println("SqlTree #" + i);
			st.setGlobalId(st.getRoot());
			String tree=st.printCommand(st.getRoot(), 0, "");
			st.excuteTree(st.getRoot());
			long end=System.currentTimeMillis();
			String time=(end-begin)+"";
			String bytes=RmiServer.tongji+"";
			String  line=RmiServer.line+"";
			RmiServer.tongji=0;
			RmiServer.line=0;
			
			//long end=System.currentTimeMillis();
			//System.out.println("total time:"+(end-begin));
		}
		 
		/*
		RmiClient[] clients = RMIClients.getRMIClient();
		try {
			String[] ss = new String[2];
			ss[0] = "book";
			ss[1] = "";
			int id = clients[1].select(ss);
			System.out.println("----------------------");
			int id1 = clients[2].select(ss);
			System.out.println("----------------------");
			
			int id3 = clients[1].getFromSite(2, id);
			
			clients[1].printResult(id3);
			System.out.println("----------------------");
			int id4 = clients[1].union(id, id3);
			clients[1].printResult(id4);
			System.out.println("----------------------");
			int id5 = clients[1].project(id4, "book.id&book.title");
			clients[1].printResult(id4);
			System.out.println("----------------------");
			int id6 = clients[3].select(ss);
			int id7 = clients[1].getFromSite(3, id6);
			System.out.println("----------------------");
			clients[1].printResult(id7);
			id7 = clients[1].getFromSite(1, id7);
			System.out.println("----------------------");
			clients[1].printResult(id7);
			clients[1].project(id7, "book.title&book.id");
			System.out.println("----------------------");
			int id8 = clients[1].union(id7, id5);
			clients[1].printResult(id8);
			//clients[2].select("select * from book");
			//System.out.println("----------------------");
			//clients[3].select("select * from book");
			//System.out.println("----------------------");
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	*/
	}
	
	public void init() throws Exception{

		RmiServer s1, s2, s3, s4;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Registry registry;
			s1 = new RmiServer(1);
			registry = LocateRegistry.createRegistry(20001);
			registry.rebind("s1", s1);
			s2 = new RmiServer(2);
			registry = LocateRegistry.createRegistry(20002);
			registry.rebind("s2", s2);
			s3 = new RmiServer(3);
			registry = LocateRegistry.createRegistry(20003);
			registry.rebind("s3", s3);
			s4 = new RmiServer(4);
			registry = LocateRegistry.createRegistry(20004);
			registry.rebind("s4", s4);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.print("start server failed");
		}
	}
		
	public void query(){

		try {
	    	HttpServletResponse response = ServletActionContext.getResponse();
	    	HttpServletRequest request =  ServletActionContext.getRequest();
	    	String text = "";
			if(request.getParameter("text") != null){
				 text = request.getParameter("text");
			}
			String sql = text;
			SqlSentence s = new SqlSentence(sql);
			// System.out.println(new SQLParser(sql));
			// System.out.println(sp.parseSql(sql));
			SqlTree st = new SqlTree();
			// st.getRoot().setDetail("SqlTree #" + i);
			long begin = System.currentTimeMillis();
			st.buildTree(s);
			st.setGlobalId(st.getRoot());
			String tree = st.printCommand(st.getRoot(), 0, "");
			st.excuteTree(st.getRoot());
			long end = System.currentTimeMillis();
			String time = (end - begin) + "";
			String bytes = RmiServer.tongji + "";
			String line = RmiServer.line + "";
			RmiServer.tongji = 0;
			RmiServer.line = 0;
			
			
			tree = "<ul id=\"tree\">" + tree + "</ul>";
			String ans = time + "!@#@!" + line + "!@#@!" + bytes + "!@#@!" + tree;
			
			response.setContentType("text/html");
			PrintStream out;
			out = new PrintStream(response.getOutputStream());
			out.print(ans);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

}
