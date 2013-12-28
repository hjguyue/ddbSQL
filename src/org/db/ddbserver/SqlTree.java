package org.db.ddbserver;

import java.rmi.RemoteException;
import java.util.LinkedList;

/**
 * a sql construct tree
 * 
 * @author Administrator
 * 
 */
public class SqlTree {
	private SqlTreeNode root;
	private LinkedList<SqlTreeNode> leaf;
	private LinkedList<SqlTreeNode> temp_join;
	private GDD gdd = new GDD();

	// static int num
	public SqlTree() {
		root = new SqlTreeNode();
		root.setType("root");
		leaf = new LinkedList<SqlTreeNode>();
		temp_join = new LinkedList<SqlTreeNode>();
	}

	public void modifySqlTree() {

	}

	/**
	 * build the tree
	 * 
	 * @param s
	 */
	public void buildTree(SqlSentence s) {
		// a lot of sentences
		String[] select_list = s.getSelect_list();
		String[] from_list = s.getFrom_list();
		String[] where_list = s.getWhere_list();

		// a lot of constructions
		// we don't need to use project, we can use select_list
		// such as book.title
		LinkedList<String> project = new LinkedList<String>();
		LinkedList<String[]> join = new LinkedList<String[]>();
		LinkedList<String[]> select = new LinkedList<String[]>();

		// all the leaves, deal with from clause first
		for (int i = 0; i < from_list.length; i++) {
			SqlTreeNode node = new SqlTreeNode();
			node.setLeaf(true);
			node.setDetail(from_list[i]);
			node.setType("table");
			node.setTablename(from_list[i]);
			leaf.add(node);
			// System.out.println(node.getDetail());
		}

		// make join and select constructions
		for (int j = 0; j < where_list.length; j++) {
			String t = where_list[j];

			/**
			 * if where == "" then continue
			 * 
			 * @@@@@@@@attention !!!!!!!!attention
			 */
			if (t == "")
				continue;
			String ss[] = makeJoinSelect(t);
			if (ss[1] == "join")
				join.add(ss);
			else
				select.add(ss);
		}

		// make project constructions, although it is useless
		for (int i = 0; i < select_list.length; i++) {
			/**
			 * if * change to the whole colomn
			 */
			if (select_list[i].equals("*")) {
				for (int j = 0; j < leaf.size(); j++) {
					String[] ss = gdd.getAllColumn(leaf.get(i).getTablename());
					for (int k = 0; k < ss.length; k++) {
						project.add(ss[k]);
					}
				}
				break;
			}
			if (select_list[i].contains(".")) {
				project.add(select_list[i]);
			} else {
				/**
				 * if there is only one leaf and the select clause is like
				 * title, we need to change it into book.title.
				 */
				project.add(leaf.get(0).getTablename() + "." + select_list[i]);
			}
		}

		/**
		 * select
		 */
		/*
		 * for(int i = 0; i < select.size(); i++) { String[] ss = select.get(i);
		 * SqlTreeNode temp = new SqlTreeNode(); temp.setType(ss[1]);
		 * temp.setDetail(ss[2] + ss[3] + ss[4]); String selectTable =
		 * this.getSelectTable(ss[2]); SqlTreeNode left; left =
		 * findInLeaf(selectTable); left.setParent(temp); temp.setLeft(left); }
		 */
		for (int i = 0; i < select.size(); i++) {

			String[] ss = select.get(i);
			// System.out.println("$$$$$$$$$$$ " + ss[1] + " " + ss[2] + " " +
			// ss[3]);
			String selectTable = this.getSelectTable(ss[2]);
			SqlTreeNode temp = null;
			for (int j = 0; j < leaf.size(); j++) {
				if (leaf.get(j).getTablename().equals(selectTable)) {
					temp = leaf.get(j);
					break;
				}
			}

			if (temp.getDetail().contains("select")) {
				temp.setDetail(temp.getDetail() + "&" + ss[1] + " " + ss[2]
						+ ss[3] + ss[4]);
			} else {
				temp.setDetail(ss[1] + " " + ss[2] + ss[3] + ss[4]);
			}
			temp.setType("select");//

			// temp.setConstruction("c_select from table " + temp.getTablename()
			// + " " + ss[2] + ss[3] + ss[4]);
			// SqlTreeNode left;
			// left = findInLeaf(selectTable);
			// left.setParent(temp);
			// temp.setLeft(left);
		}

		/**
		 * project
		 */
		for (int i = 0; i < leaf.size(); i++) {
			String detail;
			// to eliminate the possiblity of id and id repeated
			if (leaf.size() == 1
					|| isInProject(gdd.getKey(leaf.get(i).getTablename()),
							project)) {
				detail = "";
			} else {
				/**
				 * & replace \n
				 */
				detail = gdd.getKey(leaf.get(i).getTablename()) + "&";
			}
			for (int j = i; j < project.size(); j++) {
				String ss = project.get(j);
				String projectTable = this.getProjectTable(ss);
				if (projectTable.equals(leaf.get(i).getTablename())) {
					/**
					 * & replace \n
					 */
					detail = detail.concat(ss + "&");
				}
			}
			// if(detail.equals(gdd.getAllColumn(leaf.get(i).getTablename()))) {

			// todo
			// todo
			// }
			SqlTreeNode temp = new SqlTreeNode();
			temp.setType("project");
			temp.setDetail(detail.substring(0, detail.length() - 1));
			// temp.setConstruction("c_project " + detail.substring(0,
			// detail.length() - 1));
			SqlTreeNode left;
			left = findInLeaf(leaf.get(i).getTablename());
			left.setParent(temp);
			temp.setLeft(left);
		}

		/**
		 * join
		 */
		for (int i = 0; i < join.size(); i++) {
			String[] ss = getJoinTable(join.get(i));
			SqlTreeNode temp = new SqlTreeNode();
			temp.setType("join");
			temp.setDetail(join.get(i)[2] + join.get(i)[3] + join.get(i)[4]);
			// temp.setConstruction("c_join " + join.get(i)[2] + join.get(i)[3]
			// + join.get(i)[4]);
			SqlTreeNode left, right;
			// System.out.println(ss[0] + " " + ss[1]);
			left = findInLeaf(ss[0]);
			right = findInLeaf(ss[1]);
			left.setParent(temp);
			right.setParent(temp);
			temp.setLeft(left);
			temp.setRight(right);
		}

		// for(int i = 0; i < leaf.size(); i++) {
		// System.out.println(leaf.get(i).getDetail());
		// }
		/**
		 * customer vertical fragmentation
		 */
		for (int i = 0; i < leaf.size(); i++) {
			SqlTreeNode l = leaf.get(i);
			if (l.getTablename().equals("customer")) {
				// not have all
				if (!isInProject("customer.rank", project)
						&& !isInSelect("customer.rank", select)
						&& !isInProject("customer.name", project)
						&& !isInSelect("customer.name", select)) {
					if (l.getDetail().equals("customer")) {
						l.setType("table");
					} else {
						l.setType("select_rank");
					}
					l.setDetail(l.getDetail());
					l.setSite(2);
					break;
				}
				// have name
				if (!isInProject("customer.rank", project)
						&& !isInSelect("customer.rank", select)) {
					if (l.getDetail().equals("customer")) {
						l.setType("table");
					} else {
						l.setType("select_name");
					}
					l.setDetail(l.getDetail());
					l.setSite(1);
					break;
				}
				// have rank
				if (!isInProject("customer.name", project)
						&& !isInSelect("customer.name", select)) {
					if (l.getDetail().equals("customer")) {
						l.setType("table");
					} else {
						l.setType("select_rank");
					}
					l.setDetail(l.getDetail());
					l.setSite(2);
					break;
				}

				// have all

				// name
				SqlTreeNode left, right;
				left = new SqlTreeNode();
				left.setParent(l);
				l.setLeft(left);
				left.setLeaf(true);
				if (l.getDetail().equals("customer")) {
					left.setType("table");
				} else {
					left.setType("select_name");
				}
				left.setTablename("customer");
				left.setDetail(this.getName(l.getDetail()));
				if (left.getDetail().equals("")) {
					left.setType("table");
					left.setDetail("customer_name");
				}
				left.setSite(1);

				// rank
				right = new SqlTreeNode();
				right.setParent(l);
				l.setRight(right);
				right.setLeaf(true);
				if (l.getDetail().equals("customer")) {
					right.setType("table");
				} else {
					right.setType("select_rank");
				}
				right.setTablename("customer");
				right.setDetail(this.getRank(l.getDetail()));
				if (right.getDetail().equals("")) {
					right.setType("table");
					left.setDetail("customer_rank");
				}
				right.setSite(2);

				l.setType("join");
				l.setDetail("customer.id=customer.id");
				l.setLeaf(false);
				break;
			}
		}

		String book = "1&2&3", publisher = "1&2&3&4", orders = "1&2&3&4";
		/**
		 * get all the select which has the same table together
		 */
		for (int i = 0; i < select.size(); i++) {
			String[] ss = select.get(i);
			if (ss[2].equals("book.id") || ss[2].equals("orders.book_id")) {
				book = handleInter(book,
						gdd.bookId(Integer.valueOf(ss[4]), ss[3]));
				orders = handleInter(orders,
						gdd.ordersBookId(Integer.valueOf(ss[4]), ss[3]));
			} else if (ss[2].equals("publisher.id")) {
				publisher = handleInter(publisher,
						gdd.publisherId(Integer.valueOf(ss[4]), ss[3]));
			} else if (ss[2].equals("publisher.nation")) {
				publisher = handleInter(publisher, gdd.publisherNation(ss[4]
						.substring(1, ss[4].length() - 1)));
			} else if (ss[2].equals("customer.id")
					|| ss[2].equals("orders.customer_id")) {
				orders = handleInter(orders,
						gdd.ordersCustomerId(Integer.valueOf(ss[4]), ss[3]));
			}
		}

		// System.out.println("zzzz: book " + book);
		// System.out.println("zzzz: publisher " + publisher);
		// System.out.println("zzzz: orders " + orders);

		String[] xbook = book.split("&");
		String[] xorders = orders.split("&");
		String[] xpublisher = publisher.split("&");
		int cpublisher = -1;
		int corders = -1;
		int cbook = -1;
		/**
		 * Union
		 */
		for (int i = 0; i < leaf.size(); i++) {
			SqlTreeNode l = leaf.get(i);
			if (l.getTablename().equals("customer")) {
				continue;
			} else if (l.getTablename().equals("publisher")) {
				cpublisher = publisher.split("&").length;
				// l.setDetail(l.getDetail() + " " + "publisher " +
				// xpublisher[cpublisher - 1]);
			} else if (l.getTablename().equals("orders")) {
				corders = orders.split("&").length;
				// l.setDetail(l.getDetail() + " " + "orders " + xorders[corders
				// - 1]);
			} else if (l.getTablename().equals("book")) {
				cbook = book.split("&").length;
				// l.setDetail(l.getDetail() + " " + "book " + xbook[cbook -
				// 1]);
			}
		}
		// System.out.println("xxxx: book " + cbook);
		// System.out.println("xxxx: publisher " + cpublisher);
		// System.out.println("xxxx: orders " + corders);

		SqlTreeNode toBeCopied = findInLeaf(from_list[0]);
		// this.printTree(toBeCopied);

		if (cpublisher != -1) {
			for (int i = 0; i < cpublisher; i++) {
				if (cbook != -1) {
					for (int j = 0; j < cbook; j++) {
						if (corders != -1) {
							for (int k = 0; k < corders; k++) {

								// ///////
								SqlTreeNode u = new SqlTreeNode();
								u.setType("union");
								// u.setDetail("j, i, k");
								SqlTreeNode l = findInLeaf(from_list[0]);
								SqlTreeNode r = this.duplicateSqlTree(
										toBeCopied, xbook[j], xpublisher[i],
										xorders[k]);
								u.setRight(r);
								u.setLeft(l);
								l.setParent(u);
								r.setParent(u);
							}
						} else {
							// ///////
							SqlTreeNode u = new SqlTreeNode();
							u.setType("union");
							// u.setDetail("j, i, -1");
							SqlTreeNode l = findInLeaf(from_list[0]);
							SqlTreeNode r = this.duplicateSqlTree(toBeCopied,
									xbook[j], xpublisher[i], "-1");
							u.setRight(r);
							u.setLeft(l);
							l.setParent(u);
							r.setParent(u);

						}
					}
				} else {
					if (corders != -1) {
						for (int k = 0; k < corders; k++) {

							// ///////
							SqlTreeNode u = new SqlTreeNode();
							u.setType("union");
							// u.setDetail("-1, i, k");
							SqlTreeNode l = findInLeaf(from_list[0]);
							SqlTreeNode r = this.duplicateSqlTree(toBeCopied,
									"-1", xpublisher[i], xorders[k]);
							u.setRight(r);
							u.setLeft(l);
							l.setParent(u);
							r.setParent(u);
						}
					} else {
						// ///////
						SqlTreeNode u = new SqlTreeNode();
						u.setType("union");
						// u.setDetail("-1, i, -1");
						SqlTreeNode l = findInLeaf(from_list[0]);
						SqlTreeNode r = this.duplicateSqlTree(toBeCopied, "-1",
								xpublisher[i], "-1");
						u.setRight(r);
						u.setLeft(l);
						l.setParent(u);
						r.setParent(u);
					}
				}
			}
		} else {
			if (cbook != -1) {
				for (int j = 0; j < cbook; j++) {
					if (corders != -1) {
						for (int k = 0; k < corders; k++) {

							// ///////
							SqlTreeNode u = new SqlTreeNode();
							u.setType("union");
							// u.setDetail("j, -1, k");
							SqlTreeNode l = findInLeaf(from_list[0]);
							SqlTreeNode r = this.duplicateSqlTree(toBeCopied,
									xbook[j], "-1", xorders[k]);
							u.setRight(r);
							u.setLeft(l);
							l.setParent(u);
							r.setParent(u);
						}
					} else {
						// ///////
						SqlTreeNode u = new SqlTreeNode();
						u.setType("union");
						// u.setDetail("j, -1, -1");
						SqlTreeNode l = findInLeaf(from_list[0]);
						SqlTreeNode r = this.duplicateSqlTree(toBeCopied,
								xbook[j], "-1", "-1");
						u.setRight(r);
						u.setLeft(l);
						l.setParent(u);
						r.setParent(u);
					}

				}
			} else {
				if (corders != -1) {
					for (int k = 0; k < corders; k++) {

						// ///////
						SqlTreeNode u = new SqlTreeNode();
						u.setType("union");
						// u.setDetail("-1, -1, k");
						SqlTreeNode l = findInLeaf(from_list[0]);
						SqlTreeNode r = this.duplicateSqlTree(toBeCopied, "-1",
								"-1", xorders[k]);
						u.setRight(r);
						u.setLeft(l);
						l.setParent(u);
						r.setParent(u);
					}
				} else {
					// ///////
					SqlTreeNode u = new SqlTreeNode();
					u.setType("union");
					// u.setDetail("-1, -1, -1");
					SqlTreeNode l = findInLeaf(from_list[0]);
					SqlTreeNode r = this.duplicateSqlTree(toBeCopied, "-1",
							"-1", "-1");
					u.setRight(r);
					u.setLeft(l);
					l.setParent(u);
					r.setParent(u);
				}
			}
		}

		/**
		 * more project and it is the final project
		 */
		// if(leaf.size() > 1) {
		String detail = "";
		for (int j = 0; j < project.size(); j++) {
			String ss = project.get(j);
			// String projectTable = this.getProjectTable(ss);

			/**
			 * & replace \n
			 */
			detail = detail.concat(ss + "&");
		}
		SqlTreeNode temp = new SqlTreeNode();
		temp.setType("project");
		temp.setDetail(detail.substring(0, detail.length() - 1));
		SqlTreeNode left;
		left = findInLeaf(leaf.get(0).getTablename());
		left.setParent(temp);
		temp.setLeft(left);
		// ???????????????????temp.setParent(root);
		// }
		root.setLeft(temp);
		// root.setLeft(findInLeaf(from_list[0]));
		root.setType("root");
		deleteFirstUnion();
		// changeTableToSelect(root);
	}

	/**
	 * select_int customer.rank=1&select_int customer.rank=2
	 * 
	 * @param s
	 * @return
	 */
	public String getRank(String s) {
		// System.out.println(s);
		String c = "";
		String[] ss = s.split("&");
		for (int i = 0; i < ss.length; i++) {
			if (ss[i].contains("rank")) {
				c = c + ss[i] + "&";
			}
		}
		if (!c.equals("")) {
			c.substring(0, c.length() - 1);
		}
		// System.out.println("c " + c);
		return c;
	}

	/**
	 * select_int customer.rank=1&select_int customer.rank=2
	 * 
	 * @param s
	 * @return
	 */
	public String getName(String s) {
		// System.out.println(s);
		String c = "";
		String[] ss = s.split("&");
		for (int i = 0; i < ss.length; i++) {
			if (ss[i].contains("name")) {
				c = c + ss[i] + "&";
			}
		}
		if (!c.equals("")) {
			c.substring(0, c.length() - 1);
		}
		// System.out.println("c " + c);
		return c;
	}

	public void deleteFirstUnion() {
		SqlTreeNode node = leaf.get(0);
		while (!node.getType().equals("union")) {
			node = node.getParent();
		}
		if (node != null) {
			if (node.getParent() == null) {
				root = node.getRight();
			} else {
				node.getRight().setParent(node.getParent());
				node.getParent().setLeft(node.getRight());
			}
		}

	}

	/**
	 * 
	 * @param node
	 * @param book
	 * @param publisher
	 * @param orders
	 * @return
	 */
	public SqlTreeNode duplicateSqlTree(SqlTreeNode node, String book,
			String publisher, String orders) {
		if (node == null)
			return null;
		SqlTreeNode t = new SqlTreeNode(node);

		if (node.getType().equals("select") || node.getType().equals("table")) {

			if (t.getTablename().equals("book")) {
				// t.setDetail(t.getDetail() + " from " +
				// "book                      " + book);
				t.setSite(Integer.parseInt(book));
			}
			if (t.getTablename().equals("publisher")) {
				// t.setDetail(t.getDetail() + " from " +
				// "publisher                 " + publisher);
				t.setSite(Integer.parseInt(publisher));
			}
			if (t.getTablename().equals("orders")) {
				// t.setDetail(t.getDetail() + " from " +
				// "orders                    " + orders);
				t.setSite(Integer.parseInt(orders));
			}
		}
		SqlTreeNode left, right;
		left = duplicateSqlTree(node.getLeft(), book, publisher, orders);
		right = duplicateSqlTree(node.getRight(), book, publisher, orders);
		t.setLeft(left);
		t.setRight(right);
		if (left != null)
			left.setParent(t);
		if (right != null)
			right.setParent(t);
		return t;
	}

	private void changeTableToSelect(SqlTreeNode node) {
		if (node == null)
			return;
		if (node.getType().equals("table")) {
			node.setType("select_all");//
			System.out.println("hi:1");
			System.out.println(node.getDetail());
			System.out.println("hi:");
			String ss[];
			if (node.getDetail().equals("customer_rank")
					|| node.getDetail().equals("customer_name")) {
				ss = gdd.getAllColumn(node.getDetail());
			} else {
				ss = gdd.getAllColumn(node.getDetail().split(" ")[0]);
			}
			// System.out.println("@@@@@@@@@@@@@@ " + node.getDetail());
			String s = "";
			for (int i = 0; i < ss.length; i++) {
				s = s + "&" + ss[i];
			}
			node.setDetail(s);
		}
		changeTableToSelect(node.getLeft());
		changeTableToSelect(node.getRight());
	}

	public String handleInter(String r, String s) {
		String[] rr = r.split("&");
		String[] ss = s.split("&");
		String result = "";
		for (int i = 0; i < rr.length; i++) {
			for (int j = 0; j < ss.length; j++) {
				if (rr[i].equals(ss[j])) {
					result = result + rr[i] + "&";
					break;
				}
			}
		}
		if (result == "")
			return "";
		return result.substring(0, result.length() - 1);
	}

	/**
	 * 
	 * @param s
	 *            the string of project such as book.title or title
	 * @param l
	 *            all the project colomn from select clause
	 * @return
	 */
	private boolean isInProject(String s, LinkedList<String> l) {
		for (int i = 0; i < l.size(); i++) {
			if (s.equals(l.get(i)))
				return true;
		}
		return false;
	}

	/**
	 * 
	 * @param s
	 *            the string of project such as customer.rank = 1
	 * @param l
	 *            all the project colomn from where clause
	 * @return
	 */
	private boolean isInSelect(String s, LinkedList<String[]> l) {
		for (int i = 0; i < l.size(); i++) {
			if (s.equals(l.get(i)[2])) {
				return true;
			}
		}
		return false;
	}

	public SqlTreeNode getRoot() {
		return root;
	}

	public void setRoot(SqlTreeNode root) {
		this.root = root;
	}

	public LinkedList<SqlTreeNode> getLeaf() {
		return leaf;
	}

	public void setLeaf(LinkedList<SqlTreeNode> leaf) {
		this.leaf = leaf;
	}

	public LinkedList<SqlTreeNode> getTemp_join() {
		return temp_join;
	}

	public void setTemp_join(LinkedList<SqlTreeNode> temp_join) {
		this.temp_join = temp_join;
	}

	public void excuteTree(SqlTreeNode node) {
		if (node == null) {
			return;
		}
		RmiClient[] clients = RMIClients.getRMIClient();
		if (node.getType().equals("root")) {
			System.out.println("---------root---------");
			this.excuteTree(node.getLeft());
			node.setGlobal_id(node.getLeft().getGlobal_id());
			node.setSite(node.getLeft().getSite());

			try {
				clients[node.getSite()].printResult(node.getLeft()
						.getGlobal_id());
				System.out.println("---------finish_all---------");
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (node.getType().equals("select_all")) {
			this.excuteTree(node.getLeft());
			// System.out.println("---------select_all---------");
			String command = "";
			String[] ss = new String[2];
			ss[0] = node.getTablename();
			ss[1] = command;
			// System.out.println("command                    " + ss[0] + " " +
			// ss[1]);
			try {
				int id = clients[node.getSite()].select(ss);
				node.setGlobal_id(id);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// for customer
		if (node.getType().equals("table")) {
			this.excuteTree(node.getLeft());
			// System.out.println("---------table---------");
			String command = "";

			String[] ss = new String[2];
			ss[0] = node.getTablename();
			ss[1] = command;
			// System.out.println("command                    " + ss[0] + " " +
			// ss[1]);
			try {
				int id = clients[node.getSite()].select(ss);
				node.setGlobal_id(id);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (node.getType().equals("select")
				|| node.getType().equals("select_name")
				|| node.getType().equals("select_rank")) {
			this.excuteTree(node.getLeft());
			// System.out.println("---------select---------");
			String command = " where ";
			String[] ss = node.getDetail().split("&");
			for (int i = 0; i < ss.length; i++) {
				System.out.println("command " + ss[i]);
				command = command + ss[i].split(" ")[1] + " and ";
			}
			command = command.substring(0, command.length() - 4);
			ss = new String[2];
			ss[0] = node.getTablename();
			ss[1] = command;
			// System.out.print("command                    " + ss[0] + " " +
			// ss[1]);
			try {
				int id = clients[node.getSite()].select(ss);
				node.setGlobal_id(id);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(node.getType().equals("union")) {
			this.excuteTree(node.getLeft());
			this.excuteTree(node.getRight());
			//System.out.println("---------union---------");
			String temps=node.getDetail();
			String[] tempss=temps.split("&");
			int totallen=0;
			for(int i=0;i<tempss.length;i++){
				if(tempss[i].equals("book.id")){
					totallen+=10;
				}else if(tempss[i].equals("book.title")){
					totallen+=80;
				}else if(tempss[i].equals("book.authors")){
					totallen+=80;
				}else if(tempss[i].equals("book.publisher_id")){
					totallen+=10;
				}else if(tempss[i].equals("book.copies")){
					totallen+=10;
				}else if(tempss[i].equals("customer.id")){
					totallen+=10;
				}else if(tempss[i].equals("customer.name")){
					totallen+=80;
				}else if(tempss[i].equals("orders.customer_id")){
					totallen+=10;
				}else if(tempss[i].equals("orders.book_id")){
					totallen+=10;
				}else if(tempss[i].equals("orders.quantity")){
					totallen+=10;
				}else if(tempss[i].equals("publisher.id")){
					totallen+=10;
				}else if(tempss[i].equals("publisher.name")){
					totallen+=80;
				}else if(tempss[i].equals("publisher.nation")){
					totallen+=20;
				}
			}
			if(totallen==0){
				totallen=21;
			}
			node.setSite(node.getLeft().getSite());
			try {
				int id1 = clients[node.getSite()].getFromSite(node.getRight().getSite(), node.getRight().getGlobal_id(),totallen);
				int id = clients[node.getSite()].union(node.getLeft().getGlobal_id(), id1);
				node.setGlobal_id(id);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}






		if(node.getType().equals("join")) {
			this.excuteTree(node.getLeft());
			this.excuteTree(node.getRight());
			//System.out.println("---------join---------");
			String temps=node.getDetail();
			String[] tempss=temps.split("=");
			int totallen=0;
			//for(int i=0;i<tempss.length;i++){
			int i=1;
				if(tempss[i].equals("book.id")){
					totallen+=10;
				}else if(tempss[i].equals("book.title")){
					totallen+=80;
				}else if(tempss[i].equals("book.authors")){
					totallen+=80;
				}else if(tempss[i].equals("book.publisher_id")){
					totallen+=10;
				}else if(tempss[i].equals("book.copies")){
					totallen+=10;
				}else if(tempss[i].equals("customer.id")){
					totallen+=10;
				}else if(tempss[i].equals("customer.name")){
					totallen+=80;
				}else if(tempss[i].equals("orders.customer_id")){
					totallen+=10;
				}else if(tempss[i].equals("orders.book_id")){
					totallen+=10;
				}else if(tempss[i].equals("orders.quantity")){
					totallen+=10;
				}else if(tempss[i].equals("publisher.id")){
					totallen+=10;
				}else if(tempss[i].equals("publisher.name")){
					totallen+=80;
				}else if(tempss[i].equals("publisher.nation")){
					totallen+=20;
				}
			//}
			node.setSite(node.getLeft().getSite());
			try {
				int id1 = clients[node.getSite()].getFromSite(node.getRight()
						.getSite(), node.getRight().getGlobal_id(),totallen);
				int id = clients[node.getSite()].hash_join(node.getLeft()
						.getGlobal_id(), id1, node.getDetail());
				node.setGlobal_id(id);
				
				/*if(customer_id == -1 && node.isCustomerJoin()) {
					customer_id = node.getGlobal_id();
					customer_site = node.getSite();
					return;
				}*/
				//System.out.println("---------join finish---------");
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (node.getType().equals("project")) {
			this.excuteTree(node.getLeft());
			// System.out.println("---------project---------");
			node.setSite(node.getLeft().getSite());
			node.setGlobal_id(node.getLeft().getGlobal_id());
			try {
				int id = clients[node.getSite()].project(node.getGlobal_id(),
						node.getDetail());
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return;
	}

	public String replace(String str) {
		String select = "σ", project = "Π", union = "∪(union)", join = "⋈";
		String publisher = "Publisher", Customer = "Customer", Book = "Book", Orders = "Orders", Root = "Root";
		if (str.toLowerCase().equals("join"))
			return join;
		if (str.toLowerCase().equals("select"))
			return select;
		if (str.toLowerCase().equals("project"))
			return project;
		if (str.toLowerCase().equals("union"))
			return union;
		if (str.toLowerCase().equals("publisher"))
			return publisher;
		if (str.toLowerCase().equals("customer"))
			return Customer;
		if (str.toLowerCase().equals("book"))
			return Book;
		if (str.toLowerCase().equals("orders"))
			return Orders;
		if (str.toLowerCase() == "root")
			return Root;
		else
			return "";
	}

	public String printCommand(SqlTreeNode node, int n, String tree) {
		if (node == null)
			return "";
		if (node.getLeft() == null && node.getRight() == null) {
			if (node.getTablename().trim().equals("")
					&& node.getType().trim().equals("")) {
				return "";
			} else {
				if (node.getTablename().trim().equals("")) {
					//tree += "<li>" + replace(node.getType()) +"("+node.getDetail()+")"+ "</li>";
					if(!(node.getType().equals("union")||node.getType().toLowerCase().equals("root")))
						tree += "<li>" + replace(node.getType()) +"("+node.getDetail()+")";
					else {
						tree += "<li>" + replace(node.getType()) ;
					}
				} else {
					tree += "<li>" + replace(node.getTablename()) +"-S"+node.getSite()+ "</li>";
				}
			}
		} else {
			if (node.getTablename().trim().equals("")
					&& node.getType().trim().equals("")) {
				return "";
			} else {
				if (node.getTablename().trim().equals("")) {
					if(!(node.getType().equals("union")||node.getType().toLowerCase().equals("root")))
						tree += "<li>" + replace(node.getType()) +"("+node.getDetail()+")";
					else {
						tree += "<li>" + replace(node.getType()) ;
					}
					//System.out.println( "<li>" + replace(node.getType()));
				} else {
					tree += "<li>" + replace(node.getTablename());
					// System.out.println("<li>" + replace(node.getTablename())+"-S"+node.getSite());
				}
			}
			tree += "<ul>";
			if (node.getLeft() != null) {
				if(!printCommand(node.getLeft(), ++n, "").equals(""))
					tree +=  printCommand(node.getLeft(), ++n, "");
			}
			if (node.getRight() != null) {
				if(!printCommand(node.getRight(), ++n, "").equals(""))
					tree +=  printCommand(node.getRight(), ++n, "");
			}
			tree += "</ul>";
			tree += "</li>";
		}
		return tree;
		// //////////////////////////////////////////////////////////////////////////////////
		/*
		 * if(node == null) return; for(int j=0;j<n;j++) System.out.print("__");
		 * if(node.getTablename().trim().equals("")){
		 * System.out.print("["+node.getGlobal_id
		 * ()+":"+replace(node.getType())+"]"); } else {
		 * System.out.println("["+node
		 * .getGlobal_id()+":"+replace(node.getTablename
		 * ())+"-S"+node.getSite()+"]"); }
		 * if(!(node.getLeft()==null&&node.getRight()==null))
		 * System.out.print("-->"); if(node.getLeft()!=null) {
		 * if(node.getTablename().trim().equals("")){
		 * System.out.print("["+node.getLeft
		 * ().getGlobal_id()+":"+replace(node.getLeft().getType())+"]"); } else
		 * {
		 * System.out.println("["+node.getLeft().getGlobal_id()+":"+replace(node
		 * .getLeft().getTablename()) +"-S"+node.getLeft().getSite()+"]"); }
		 * 
		 * } if(node.getRight()!=null) {
		 * if(node.getTablename().trim().equals("")){
		 * System.out.print("["+node.getRight
		 * ().getGlobal_id()+":"+replace(node.getRight().getType())+"]"); } else
		 * {
		 * System.out.println("["+node.getRight().getGlobal_id()+":"+replace(node
		 * .getRight().getTablename()) +"-S"+node.getRight().getSite()+"]"); } }
		 * System.out.println(); printCommand(node.getLeft(),++n);
		 * printCommand(node.getRight(),++n);
		 */
		// System.out.println(node.getGlobal_id() + " *** " +
		// node.getTablename() + " *** " + node.getType() + " *** " +
		// node.getDetail() + " *** " + node.getSite());
	}

	private int global_id = 1;

	public void setGlobalId(SqlTreeNode node) {
		if (node == null)
			return;
		setGlobalId(node.getLeft());
		setGlobalId(node.getRight());
		node.setGlobal_id(global_id);
		global_id++;
	}

	public void printTree(SqlTreeNode node) {
		if (node == null)
			return;
		System.out.println("**************");
		System.out.println(node.getType() + "\n" + node.getDetail());
		printTree(node.getLeft());
		printTree(node.getRight());
	}

	public void printTree1(SqlTreeNode node, int num) {
		if (node != null) {
			printTree1(node.getRight(), num + 7);
			for (int i = 0; i < num; i++) {
				System.out.print(" ");
			}
			System.out.println(node.getType());
			for (int i = 0; i < num; i++) {
				System.out.print(" ");
			}
			System.out.println(node.getDetail());
			printTree1(node.getLeft(), num + 7);
		}
	}

	/**
	 * get the leaf and the root of that subtree
	 * 
	 * @param tablename
	 * @return
	 */
	private SqlTreeNode findInLeaf(String tablename) {
		for (int i = 0; i < leaf.size(); i++) {
			if (leaf.get(i).getTablename().equals(tablename))
				return getSubRoot(leaf.get(i));
		}
		return null;
	}

	/**
	 * get top root of the subtree, using by findInLeaf
	 * 
	 * @param node
	 * @return
	 */
	private SqlTreeNode getSubRoot(SqlTreeNode node) {
		while (node.getParent() != null) {
			node = node.getParent();
		}

		return node;
	}

	/**
	 * return the table name
	 * 
	 * book.id orders.book_id we return book & orders
	 * 
	 * @param s
	 * @return
	 */
	private String[] getJoinTable(String[] s) {
		String[] ss = new String[2];
		ss[0] = s[2].substring(0, s[2].indexOf("."));
		ss[1] = s[4].substring(0, s[4].indexOf("."));
		return ss;
	}

	/**
	 * return table name
	 * 
	 * book.id or id we return both book
	 * 
	 * @param s
	 * @return
	 */
	private String getSelectTable(String s) {
		String ss;
		if (s.contains(".")) {
			ss = s.substring(0, s.indexOf("."));
		} else {
			ss = leaf.get(0).getTablename();
		}

		return ss;
	}

	/**
	 * return table name
	 * 
	 * book.id or id we return both book
	 * 
	 * @param s
	 * @return
	 */
	private String getProjectTable(String s) {
		String ss;
		if (s.contains(".")) {
			ss = s.substring(0, s.indexOf("."));
		} else {
			ss = leaf.get(0).getTablename();
		}

		return ss;
	}

	/**
	 * analyze where clause and get the constructs out of the sentences.
	 * select_int: id = 100 or id > 10 select_char: nation = 'usa' join: id = id
	 * 
	 * @param s
	 * @return ss[] 0 sql: book.id=orders.book_id 1 join 2 book.id 3 = 4
	 *         orders.book_id 5 *************
	 */
	private String[] makeJoinSelect(String s) {
		String ss[] = new String[6];
		ss[0] = "sql: " + s;
		ss[5] = "*************";
		if (s.contains("=")) {
			ss[2] = s.substring(0, s.indexOf("="));
			ss[3] = "=";
			ss[4] = s.substring(s.indexOf("=") + 1);
			if (ss[4].charAt(0) == '\'') {
				ss[1] = "select_char";
			} else if (ss[4].charAt(0) <= '9' && ss[4].charAt(0) >= '0') {
				ss[1] = "select_int";
			} else {
				ss[1] = "join";
			}
		}
		if (s.contains(">")) {
			ss[2] = s.substring(0, s.indexOf(">"));
			ss[3] = ">";
			ss[4] = s.substring(s.indexOf(">") + 1);
			ss[1] = "select_int";
		}
		if (s.contains("<")) {
			ss[2] = s.substring(0, s.indexOf("<"));
			ss[3] = "<";
			ss[4] = s.substring(s.indexOf("<") + 1);
			ss[1] = "select_int";
		}

		for (int i = 0; i < 6; i++) {
			// System.out.println(ss[i]);
		}

		if (!ss[2].contains(".")) {
			ss[2] = leaf.get(0).getTablename() + "." + ss[2];
		}

		return ss;
	}
}
