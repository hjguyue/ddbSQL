package org.db.ddbserver;

public class GDD {
	String[] publisher = {"publisher.id", "publisher.name", "publisher.nation"};
	String[] book = {"book.id", "book.title", "book.authors", "book.publisher_id", "book.copies"};
	String[] customer = {"customer.id", "customer.name", "customer.rank"};
	String[] customer_rank = {"customer.id", "customer.rank"};
	String[] customer_name = {"customer.id", "customer.name"};
	String[] orders = {"orders.customer_id", "orders.book_id", "orders.quantity"};
	
	String username = "root";
	String password = "";
	String[] host = {"this is not used",
			"jdbc:mysql://localhost:3306/ddb1?useUnicode=true&characterEncoding=utf8",
			"jdbc:mysql://localhost:3306/ddb2?useUnicode=true&characterEncoding=utf8",
			"jdbc:mysql://localhost:3306/ddb3?useUnicode=true&characterEncoding=utf8",
			"jdbc:mysql://localhost:3306/ddb4?useUnicode=true&characterEncoding=utf8"
			};
	
	public String getKey(String s) {
		if(s.equals("publisher"))
			return "publisher.id";
		if(s.equals("book"))
			return "book.id&book.publisher_id";
		if(s.equals("customer"))
			return "customer.id";
		if(s.equals("orders"))
			return "orders.customer_id&orders.book_id";
		return null;
	}
	
	public int getSite(String table, String num) {
		if(table.equals("publisher")) {
			if(num.equals("1"))
				return 1;
			if(num.equals("2"))
				return 2;
			if(num.equals("3"))
				return 3;
			if(num.equals("4"))
				return 4;
		}
		if(table.equals("book")) {
			if(num.equals("1"))
				return 1;
			if(num.equals("2"))
				return 2;
			if(num.equals("3"))
				return 3;
		}
		if(table.equals("customer")) {
			if(num.equals("1"))
				return 1;
			if(num.equals("2"))
				return 2;
		}
		if(table.equals("orders")) {
			if(num.equals("1"))
				return 1;
			if(num.equals("2"))
				return 2;
			if(num.equals("3"))
				return 3;
			if(num.equals("4"))
				return 4;
		}	
		
		return 0;
	}

	public String[] getAllColumn(String s) {
		// TODO Auto-generated method stub
		if(s.equals("publisher"))
			return publisher;
		if(s.equals("book"))
			return book;
		if(s.equals("customer"))
			return customer;
		if(s.equals("customer_name"))
			return customer_name;
		if(s.equals("customer_rank"))
			return customer_rank;
		if(s.equals("orders"))
			return orders;
		return null;
	}
	
	
	   public String ordersCustomerId(int id, String f) {
	       if(f.equals(">")) {
	           if(id < 307000) {
	               return "1&2&3&4";
	           } else {
	               return "3&4";
	           }
	       } else if(f.equals("=")) {
	           if(id < 307000) {
	               return "1&2";
	           } else {
	               return "3&4";
	           }
	       } else if(f.equals("<")) {
	           if(id <= 307000) {
	               return "1&2";
	           } else {
	               return "1&2&3&4";
	           }
	       } else if(f.equals("<=")) {
	           if(id < 307000) {
	               return "1&2";
	           } else {
	               return "1&2&3&4";
	           }
	       } else if(f.equals(">=")) {
	           if(id < 307000) {
	               return "1&2&3&4";
	           } else {
	               return "3&4";
	           }
	       }

	       return "";
	   }

	   public String ordersBookId(int id, String f) {
	       if(f.equals(">")) {
	           if(id < 215000) {
	               return "1&2&3&4";
	           } else {
	               return "2&4";
	           }
	       } else if(f.equals("=")) {
	           if(id < 215000) {
	               return "1&3";
	           } else {
	               return "2&4";
	           }
	       } else if(f.equals("<")) {
	           if(id <= 215000) {
	               return "1&3";
	           } else {
	               return "1&2&3&4";
	           }
	       } else if(f.equals("<=")) {
	           if(id < 215000) {
	               return "1&3";
	           } else {
	               return "1&2&3&4";
	           }
	       } else if(f.equals(">=")) {
	           if(id < 215000) {
	               return "1&2&3&4";
	           } else {
	               return "2&4";
	           }
	       }

	       return "";
	   }

	   public String bookId(int id, String f) {
	       if(f.equals(">")) {
	           if(id < 205000) {
	               return "1&2&3";
	           }
	           else if(id < 210000){
	               return "2&3";
	           }
	           else {
	               return "3";
	           }
	       } else if(f.equals("=")) {
	           if(id < 205000) {
	               return "1";
	           }
	           else if(id < 210000){
	               return "2";
	           }
	           else {
	               return "3";
	           }
	       } else if(f.equals("<")) {
	           if(id <= 205000) {
	               return "1";
	           }
	           else if(id <= 210000){
	               return "1&2";
	           }
	           else {
	               return "1&2&3";
	           }
	       } else if(f.equals(">=")) {
	           if(id < 205000) {
	               return "1&2&3";
	           }
	           else if(id < 210000){
	               return "2&3";
	           }
	           else {
	               return "3";
	           }
	       } else if(f.equals("<=")) {
	           if(id < 205000) {
	               return "1";
	           }
	           else if(id < 210000){
	               return "1&2";
	           }
	           else {
	               return "1&2&3";
	           }
	       }

	       return "";
	   }

	   public String publisherNation(String nation) {
	       if(nation.equals("prc")) {
	           return "1&3";
	       }
	       else if(nation.equals("usa")) {
	           return "2&4";
	       }
	       else {
	           return "";
	       }
	   }


	   public String publisherId(int id, String f) {
	       if(f.equals(">")) {
	           if(id < 104000) {
	               return "1&2&3&4";
	           }
	           else {
	               return "3&4";
	           }
	       }
	       else if(f.equals("=")) {
	           if(id < 104000) {
	               return "1&2";
	           }
	           else {
	               return "3&4";
	           }
	       }
	       else if(f.equals("<")) {
	           if(id <= 104000) {
	               return "1&2";
	           }
	           else {
	               return "1&2&3&4";
	           }
	       }
	       else if(f.equals(">=")) {
	           if(id < 104000) {
	               return "1&2&3&4";
	           }
	           else {
	               return "1&2";
	           }
	       }
	       else if(f.equals("<=")) {
	           if(id < 104000) {
	               return "1&2";
	           }
	           else {
	               return "1&2&3&4";
	           }
	       }

	       return "";
	   }
}
