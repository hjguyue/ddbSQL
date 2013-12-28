package org.db.ddbserver;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Vector;

public class RmiServer extends UnicastRemoteObject implements RmiClient {
	private LinkedList<SqlResult> result;
	private GDD gdd;
	private int site;
	private Connection con;
	private Statement statement;
	private RmiClient[] clients;
	public static int tongji=0;
	public static int line; 
	protected RmiServer(int site) throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
		this.site = site;
		result = new LinkedList<SqlResult>();

		gdd = new GDD();
		
		try {
			/***
			 * server use 1 2 3 4 to be the site, they don't use 0 and what is more,
			 * in the fragmantation, we do not use 0 as well 
			 */
			con = DriverManager.getConnection(gdd.host[this.site], gdd.username,
					gdd.password);
			statement = con.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -2277476012354348708L;

	@Override
	public int select(String[] query) throws RemoteException {
		// TODO Auto-generated method stub
		System.out.println("---------start select---------");
		long time1 = System.currentTimeMillis();
		ResultSet resultSet;
		try {
			String q = "select * from " + query[0] + query[1];
			resultSet = statement.executeQuery(q);
			ResultSetMetaData rsmd = resultSet.getMetaData();
			int colomn_num = rsmd.getColumnCount();
			SqlResult sr = new SqlResult();
			for (int i = 0; i < colomn_num; i++) {
				SqlColomn sc = new SqlColomn(query[0] + "." + rsmd.getColumnName(i + 1));
				sr.getName().add(query[0] + "." + rsmd.getColumnName(i + 1));
				sr.getL().add(sc);
			}
			while (resultSet.next()) {
				for (int i = 0; i < colomn_num; i++) {
					sr.getL().get(i).getL().add(resultSet.getString(i + 1));
				}
			}

			resultSet.last();
			System.out.println(resultSet.getRow());
			result.add(sr);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long time2 = System.currentTimeMillis();
		System.out.println("---------finish select---------" + (time2 - time1) + " site " + this.site + " result " + (result.size() - 1) + " num " + getNum(result.size() - 1));
		return result.size() - 1;
	}

	@Override
	public SqlResult getTableById(int id) throws RemoteException {
		// TODO Auto-generated method stub
		return result.get(id);
	}

	/**
	 * sites are always 1 2 3 4
	 * and the clients are always 1 2 3 4
	 */
	@Override
	public int getFromSite(int site, int id,int len) throws RemoteException {
		// TODO Auto-generated method stub
		System.out.println("---------start transfer---------");
		long time1 = System.currentTimeMillis();
		System.out.println("getFromSite " + site + " asking " + id);
		clients = RMIClients.getRMIClient();
		result.add(clients[site].getTableById(id));
		long time2 = System.currentTimeMillis();
		System.out.println("---------finish transfer---------time " + (time2 - time1) + " site " + this.site + " result " + (result.size() - 1) + " num " + getNum(result.size() - 1));
		tongji+=getNum(result.size() - 1)*len;
		return result.size() - 1;
	}

	@Override
	public void printResult(int id) throws RemoteException {
		// TODO Auto-generated method stub
		//int line=0;
		System.out.println("---------start print---------");
		long time1 = System.currentTimeMillis();
		SqlResult sr = result.get(id);
		line=sr.getL().get(0).getL().size();
		System.err.println("total lines: " + sr.getL().get(0).getL().size());
		for(int i = 0; i < sr.getL().size(); i++) {
			System.out.print(sr.getName().get(i) + " ");
		}
		System.out.println();
		long time2 = System.currentTimeMillis();
		System.out.println("---------finish print---------" + (time2 - time1));
	}

	@Override
	public int union(int id1, int id2) throws RemoteException {
		// TODO Auto-generated method stub
		System.out.println("---------start union---------");
		long time1 = System.currentTimeMillis();
		SqlResult r1 = result.get(id1);
		SqlResult r2 = result.get(id2);
		SqlResult r = new SqlResult();
		ArrayList<SqlColomn> colomnList1, colomnList2, colomnList;
		ArrayList<String> colomn1, colomn2, colomn;
		SqlColomn sqlcolomn;
		int sqlcolomnCount;
		String name;
		
		colomnList1 = r1.getL();
		colomnList2 = r2.getL();
		sqlcolomnCount = colomnList1.size();
		colomnList = new ArrayList<SqlColomn>();
		
		for(int i=0; i<sqlcolomnCount; i++) {
			name = colomnList1.get(i).getColomnName();
			sqlcolomn = new SqlColomn(name);
			r.getName().add(name);
			colomn = new ArrayList<String>();
			colomn1 = colomnList1.get(i).getL();
			colomn2 = colomnList2.get(i).getL();
			colomn.addAll(colomn1);
			colomn.addAll(colomn2);
			
			sqlcolomn.setL(colomn);
			colomnList.add(sqlcolomn);
		}
		
		r.setL(colomnList);
		result.add(r);
		long time2 = System.currentTimeMillis();
		System.out.println("---------finish union---------" + (time2 - time1) + " site " + this.site + " result " + (result.size() - 1) + " num " + getNum(result.size() - 1));
		return result.size() - 1;
	}

	@Override
	public int project(int id, String s) throws RemoteException {
		// TODO Auto-generated method stub
		System.out.println("---------start project---------");
		long time1 = System.currentTimeMillis();
		SqlResult r = result.get(id);
		SqlColomn sqlcolomn;
		ArrayList<SqlColomn> colomns;
		String name, colomnNames[];
		
		colomnNames = s.split("&");
		for(int i = 0; i < colomnNames.length; i++) {
			System.out.print(colomnNames[i] + " ");
		}
		System.out.println();
		//System.out.println("\n" + colomnNames.length);
		colomns = r.getL();
		for(int i = 0; i < colomns.size(); i++) {
			sqlcolomn = colomns.get(i);
			name = sqlcolomn.getColomnName();
			
			if(!this.isIn(colomnNames, name)) { 
				colomns.remove(i);
				r.getName().remove(i);
				i--;
			}
		}
		long time2 = System.currentTimeMillis();
		System.out.println("---------finish project---------" + (time2 - time1) + " site " + this.site + " result " + id + " num " + getNum(id));
		return id;
	}
	
	private int getNum(int id) {
		return result.get(id).getL().get(0).getL().size();
	}
	
	private boolean isIn(String[] ss, String s) {
		for(int i = 0; i < ss.length; i++) {
			if(s.equals(ss[i])) {
				//System.out.println(s + " ** " + ss[i]);
				return true;
			}
		}
		
		return false;
	}

	@Override
	public int hash_join(int id1, int id2, String s) throws RemoteException {
		System.out.println("---------start join---------");
		System.err.println(s);
		System.out.println();
		long time1 = System.currentTimeMillis();
		SqlResult r1 = result.get(id1);
		SqlResult r2 = result.get(id2);
		SqlResult tempResult, r = new SqlResult();
		SqlColomn sqlColumn;
		String clauses[];
		String value1, value2, name1, name2;
		ArrayList<SqlColomn> sqlColumnList1, sqlColumnList2, sqlColumnList;
		ArrayList<String> column1, column2, tempColumn, column;
		HashMap<String, Vector<Integer>> hashMap;
		Vector<Integer> vector;
		Vector<Integer> resultVector1, resultVector2;
		int index1, index2, attributeSize1, attributeSize2;

		column1 = new ArrayList<String>();
		column2 = new ArrayList<String>();
		hashMap = new HashMap<String, Vector<Integer>>();
		resultVector1 = new Vector<Integer>();
		resultVector2 = new Vector<Integer>();
		sqlColumnList = new ArrayList<SqlColomn>();

		clauses = s.replaceAll(" ", "").split("=");
		// get columns to join
		sqlColumnList1 = r1.getL();
		sqlColumnList2 = r2.getL();
		attributeSize1 = sqlColumnList1.size();
		attributeSize2 = sqlColumnList2.size();
		// System.out.println("attribute1: " + attributeSize1);
		// System.out.println("attribute2: " + attributeSize2);
		for (int i = 0; i < sqlColumnList1.size(); i++) {
			name1 = sqlColumnList1.get(i).getColomnName();
			// System.out.println("1\t" + name1 + ":" + clauses[0]);
			if (name1.equals(clauses[0])) {
				column1 = sqlColumnList1.get(i).getL();
				break;
			}
		}
		for (int i = 0; i < sqlColumnList2.size(); i++) {
			name2 = sqlColumnList2.get(i).getColomnName();
			// System.out.println("2\t" + name2 + ":" + clauses[1]);
			if (name2.equals(clauses[1])) {
				column2 = sqlColumnList2.get(i).getL();
				break;
			}
		}

		// exchange variable name to make r1 represent the one contains less
		// result
		if (column1.size() > column2.size()) {
			tempResult = r1;
			r1 = r2;
			r2 = tempResult;

			sqlColumnList1 = r1.getL();
			sqlColumnList2 = r2.getL();

			tempColumn = column1;
			column1 = column2;
			column2 = tempColumn;

			attributeSize1 = sqlColumnList1.size();
			attributeSize2 = sqlColumnList2.size();
		}

		// System.out.println("column1: " + column1.size());
		// construct hash map, map from key to index
		for (int i = 0; i < column1.size(); i++) {
			value1 = column1.get(i);
			if (hashMap.containsKey(value1))
				hashMap.get(value1).add(new Integer(i));
			else {
				vector = new Vector<Integer>();
				vector.add(new Integer(i));
				hashMap.put(value1, vector);
			}
		}
		// System.out.println("hash map:" + hashMap.size());

		// initialize result
		for (int i = 0; i < attributeSize1; i++) {
			name1 = sqlColumnList1.get(i).getColomnName();
			sqlColumn = new SqlColomn(name1);
			sqlColumnList.add(sqlColumn);
		}
		for (int i = 0; i < attributeSize2; i++) {
			name2 = sqlColumnList2.get(i).getColomnName();
			sqlColumn = new SqlColomn(name2);
			sqlColumnList.add(sqlColumn);
		}
		r.setL(sqlColumnList);

		// join
		for (int i = 0; i < column2.size(); i++) {
			value2 = column2.get(i);
			if (hashMap.containsKey(value2)) {
				vector = hashMap.get(value2);
				for (int j = 0; j < vector.size(); j++) {
					resultVector1.add(vector.elementAt(j));
					resultVector2.add(new Integer(i));
				}
			}
		}

		for (int i = 0; i < attributeSize1; i++) {
			column = sqlColumnList1.get(i).getL();
			for (int j = 0; j < resultVector1.size(); j++) {
				index1 = resultVector1.get(j);
				value1 = column.get(index1);
				r.getL().get(i).getL().add(value1);
			}
		}
		for (int i = 0; i < attributeSize2; i++) {
			column = sqlColumnList2.get(i).getL();
			for (int j = 0; j < resultVector2.size(); j++) {
				index2 = resultVector2.get(j);
				value2 = column.get(index2);
				r.getL().get(i + attributeSize1).getL().add(value2);
			}
		}

		/*
		 * for(int i=0; i<10; i++) { for(int j=0;
		 * j<attributeSize1+attributeSize2; j++)
		 * System.out.print(r.getL().get(j).getL().get(i) + "\t");
		 * System.out.println(); }
		 */
		
		for(int i = 0; i < r.getL().size(); i++) {
			r.getName().add(r.getL().get(i).getColomnName());
		}
		result.add(r);
		long time2 = System.currentTimeMillis();
		System.out.println("---------finish join---------" + (time2 - time1) + " site " + this.site + " result " + (result.size() - 1) +  " num " + getNum(result.size() - 1));
		return result.size() - 1;

	}
}
