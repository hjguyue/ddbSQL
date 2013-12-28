package org.db.ddbserver;

import java.util.LinkedList;

/**
 * tree node
 * @author Administrator
 *
 */
public class SqlTreeNode {
	private int global_id;
	private String type;
	private String detail;
	private String tablename;
	private boolean isLeaf;
	private SqlTreeNode parent;
	private SqlTreeNode left;
	private SqlTreeNode right;
	private String construction;
	private LinkedList<String> commands;
	private int site;
	
	public SqlTreeNode() {
		site = 0;
		global_id = 0;
		type = "";
		detail = "";
		tablename = "";
		isLeaf = true;
		parent = null;
		left = null;
		right = null;
		construction = "";
		commands = new LinkedList<String>();
	}

	public SqlTreeNode(SqlTreeNode t) {
		site = t.site;
		global_id = t.global_id;
		type = t.type;
		detail = t.detail;
		tablename = t.tablename;
		isLeaf = t.isLeaf;
		parent = t.parent;
		left = t.left;
		right = t.right;
		construction = t.construction;
		commands = t.commands;
	}
	
	public int getSite() {
		return site;
	}

	public void setSite(int site) {
		this.site = site;
	}

	public LinkedList<String> getCommands() {
		return commands;
	}

	public void setCommands(LinkedList<String> commands) {
		this.commands = commands;
	}

	public String getConstruction() {
		return construction;
	}

	public void setConstruction(String construction) {
		this.construction = construction;
	}

	public int getGlobal_id() {
		return global_id;
	}

	public void setGlobal_id(int global_id) {
		this.global_id = global_id;
	}

	public String getTablename() {
		return tablename;
	}

	public void setTablename(String tablename) {
		this.tablename = tablename;
	}



	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isLeaf() {
		return isLeaf;
	}

	public void setLeaf(boolean isLeaf) {
		this.isLeaf = isLeaf;
	}

	public SqlTreeNode getParent() {
		return parent;
	}

	public void setParent(SqlTreeNode parent) {
		this.parent = parent;
	}

	public SqlTreeNode getLeft() {
		return left;
	}

	public void setLeft(SqlTreeNode left) {
		this.left = left;
	}

	public SqlTreeNode getRight() {
		return right;
	}

	public void setRight(SqlTreeNode right) {
		this.right = right;
	}
	
}
