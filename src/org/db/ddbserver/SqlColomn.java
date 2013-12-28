package org.db.ddbserver;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

public class SqlColomn implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6433489564220329260L;
	private String ColomnName;
	private ArrayList<String> l;
	
	public SqlColomn(String name) {
		l = new ArrayList<String>();
		ColomnName = name;
	}

	public String getColomnName() {
		return ColomnName;
	}

	public void setColomnName(String colomnName) {
		ColomnName = colomnName;
	}

	public ArrayList<String> getL() {
		return l;
	}

	public void setL(ArrayList<String> l) {
		this.l = l;
	}
	
	
}
