package org.db.ddbserver;

/**
 * the sql sentences
 * @author Administrator
 *
 */
public class SqlSentence {
	private String sql;
	
	private String select;
	
	private String from;
	
	private String where;
	
	private String[] select_list;

	private String[] from_list;
	
	private String[] where_list;
	
	private SQLParser1 sqlParse1 = new SQLParser1();
	
	public String[] getSelect_list() {
		return select_list;
	}

	public void setSelect_list(String[] select_list) {
		this.select_list = select_list;
	}

	public String[] getFrom_list() {
		return from_list;
	}

	public void setFrom_list(String[] from_list) {
		this.from_list = from_list;
	}

	public String[] getWhere_list() {
		return where_list;
	}

	public void setWhere_list(String[] where_list) {
		this.where_list = where_list;
	}

	public SqlSentence(String sql) {
		String[] ss = sqlParse1.parseSql(sql);
		
		//get all the part
		select = ss[0];	
		from = ss[1];
		where = ss[2];

		//to list
		select_list = select.split(",");
		from_list = from.split(",");
		where_list = where.split("and");
		
		for(int i = 0; i < select_list.length; i++) {
			select_list[i] = cleanString(select_list[i]);
		}
		
		for(int i = 0; i < from_list.length; i++) {
			from_list[i] = cleanString(from_list[i]);
		}
		
		/*
		 * attention: @@ where clause can be nothing "" so you need to test it before use.
		 */
		for(int i = 0; i < where_list.length; i++) {
			where_list[i] = cleanString(where_list[i]);
		}
		
		//System.out.print(select + "\n" + from + "\n" + where + "\n");
		/*System.out.println();
		
		System.out.println("***************");
		System.out.println("select");
		for(int i = 0; i < select_list.length; i++) {
			System.out.print(select_list[i] + " ");
		}
		System.out.println("\nfrom");
		for(int i = 0; i < from_list.length; i++) {
			System.out.print(from_list[i] + " ");
		}
		System.out.println("\nwhere");
		for(int i = 0; i < where_list.length; i++) {
			System.out.print(where_list[i] + " ");
		}*/
	}
	
	private String cleanString(String s) {
		String ss = "";
		for(int i = 0; i < s.length(); i++) {
			if(s.charAt(i) == ' ' || s.charAt(i) == '\t') {
				continue;
			}
			ss = ss + s.charAt(i);
		}
		
		return ss;
	}
}
