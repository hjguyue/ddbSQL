package org.db.ddbserver;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

public class SqlResult implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7417871626714039826L;
	private int result_id;
	private int site;
	private String table;
	private ArrayList<String> name;
	private ArrayList<SqlColomn> l;
	
	public SqlResult() {
		l = new ArrayList<SqlColomn>();
		name = new ArrayList<String>();
	}
	
	public ArrayList<String> getName() {
		return name;
	}

	public void setName(ArrayList<String> name) {
		this.name = name;
	}

	public int getResult_id() {
		return result_id;
	}

	public void setResult_id(int result_id) {
		this.result_id = result_id;
	}

	public int getSite() {
		return site;
	}

	public void setSite(int site) {
		this.site = site;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public ArrayList<SqlColomn> getL() {
		return l;
	}

	public void setL(ArrayList<SqlColomn> l) {
		this.l = l;
	}
	
	
}
