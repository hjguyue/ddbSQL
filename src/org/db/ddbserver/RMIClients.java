package org.db.ddbserver;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIClients {
	public RMIClients() {

	}

	public static RmiClient[] getRMIClient() {
		Registry[] registry1 = new Registry[4];
		RmiClient[] clients = new RmiClient[5];

		try {
			registry1[0] = LocateRegistry.getRegistry(20001);
			registry1[1] = LocateRegistry.getRegistry(20002);
			registry1[2] = LocateRegistry.getRegistry(20003);
			registry1[3] = LocateRegistry.getRegistry(20004);
			clients[0] = null;
			clients[1] = (RmiClient) registry1[0].lookup("s1");
			clients[2] = (RmiClient) registry1[1].lookup("s2");
			clients[3] = (RmiClient) registry1[2].lookup("s3");
			clients[4] = (RmiClient) registry1[3].lookup("s4");
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return clients;
	}
}
