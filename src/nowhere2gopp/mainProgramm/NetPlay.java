package nowhere2gopp.mainProgramm;

import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.registry.*;

import nowhere2gopp.player.Player_Son;
import nowhere2gopp.player.Player_Remote;

/**
 * Project 5i)
 * NetPlay server and client methods
 * @author Mengru.Ji
 *
 */
public class NetPlay {
	
	
	
	/**
	 * constructor
	 */
	public NetPlay() {
		
	}
	
	
	
	/**
	 * offer as a server
	 * @param p offer local object
	 * @param host ipv4 address
	 * @param port default port for LAN is 1099 (windows)
	 * @param name mark of the object
	 * @throws RemoteException Exception in RMI
	 * @throws MalformedURLException a kind of IOException
	 */
	public void offer(Player_Remote p, String host, String port, String name) throws RemoteException, MalformedURLException {
		System.setProperty("java.rmi.server.hostname", host);
		Registry registry = LocateRegistry.createRegistry(Integer.parseInt(port));
		registry.rebind(name, p);
		System.out.println("Player(" + name + ") ready" );
	}
	
	
	
	/**
	 * find as client
	 * @param host ipv4 address
	 * @param port default port for LAN is 1099 (windows)
	 * @param name mark of the object
	 * @return Interface
	 * @throws MalformedURLException a kind of IOException
	 * @throws RemoteException Exception in RMI
	 * @throws NotBoundException look up failed or no binding
	 */
	public Player_Son find(String host, String port, String name) throws MalformedURLException, RemoteException, NotBoundException {
		Player_Son p = null;
		p = (Player_Son) Naming.lookup("rmi://" + host + ":" + "/" + name);
		System.out.println("Player(" + name + ") finded");
		return p;
	}

	
	
}