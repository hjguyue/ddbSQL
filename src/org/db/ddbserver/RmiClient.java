package org.db.ddbserver;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RmiClient extends Remote{
	/**
	 * select in a certain site
	 *
	 * @param query 
	 * 				the query string
	 * 				[0] table
	 * 				[1] where clause
	 * @return
	 * 				the int that the new query result in the site
	 * @throws RemoteException
	 */
	public int select(String[] query) throws RemoteException;
	
	/**
	 * get the result from a certain site
	 * 
	 * @param id
	 * 				the temp result in the list of that site
	 * @return
	 * 				the SqlResult
	 * @throws RemoteException
	 */
	public SqlResult getTableById(int id) throws RemoteException;
	
	/**
	 * ask a certain site to get from site and ask the id result
	 * 
	 * @param site
	 * 				the site i want to ask result
	 * @param id
	 * 				the result i want to have
	 * @return
	 * 				the result in the temperory site
	 * @throws RemoteException
	 */
	public int getFromSite(int site, int id,int len) throws RemoteException;
	
	/**
	 * print the result for test use
	 * 
	 * @param id
	 * 				the result i want to use
	 * @return 
	 * @throws RemoteException
	 */
	public void printResult(int id) throws RemoteException;
	
	/**
	 * union the two results which are already in the temporary site;
	 * @param id1
	 * @param id2
	 * @return
	 * @throws RemoteException
	 */
	public int union(int id1, int id2) throws RemoteException;
	
	/**
	 * project the colomn we want
	 * 
	 * @param id
	 * 				the id we need to project
	 * @param query
	 * 				the string including the colomn names
	 * @return
	 * @throws RemoteException
	 */
	public int project(int id, String s) throws RemoteException;
	
	/**
	 * join the two table
	 * @param id1
	 * 				the first table
	 * @param id2
	 * 				the second table
	 * @param s
	 * 				the id = id
	 * @return
	 * @throws RemoteException
	 */
	public int hash_join(int id1, int id2, String s) throws RemoteException;
	
}
