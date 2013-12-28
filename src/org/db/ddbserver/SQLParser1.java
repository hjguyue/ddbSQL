package org.db.ddbserver;

public class SQLParser1 {
	
	/**
	 * 
	 * @param sql
	 */
	public SQLParser1() {
		
	}
	
	/**
	 * doParse the sql, get the select, from, where sentences out of sql.
	 * @param sql the sentence we need to analyze
	 * @return the seperate part
	 */
	public String[] parseSql(String sql) {
		String[] ss = new String[3];
		String select = "", from = "", where = "";
		int select_pos = -1, from_pos = -1, where_pos = -1;
		
		//lower case, easier to deal with
		sql = sql.toLowerCase();
		//get the position of different parts
		if(sql.contains("select"))
			select_pos = sql.indexOf("select");
		if(sql.contains("from"))
			from_pos = sql.indexOf("from");
		if(sql.contains("where"))
			where_pos = sql.indexOf("where");
		
		//get the sql part
		if(select_pos != -1 && from_pos != -1)
			select = sql.substring(select_pos + 6, from_pos);
		if(from_pos != -1 && where_pos != -1)
			from = sql.substring(from_pos + 4, where_pos);
		else
			from = sql.substring(from_pos + 4);
		if(where_pos != -1)
			where = sql.substring(where_pos + 5);
		//System.out.print(arg0)
		
		ss[0] = select;
		ss[1] = from;
		ss[2] = where;
 		return ss;
 		//return sql + "\n" + select + "\n" + from + "\n" + where + "\n";
	}
}
