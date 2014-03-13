package rem;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;


import scn.Host;

public class HostServer extends UnicastRemoteObject implements HostInterface {
	Registry registry; 
	int port = 1729;
	Host host;
	String my_name, their_address, their_name;
	boolean has_connected = false;
	
	public HostServer(Host host) throws RemoteException {
		super();
		registry = LocateRegistry.createRegistry(port);
		registry.rebind("host_server", this);
		this.host = host;
		this.my_name = host.getPlayerName();
	}
	
	public String getMyName() {
		return this.my_name;
	}
	
	public String getTheirName() {
		return this.their_name;
	}
	
	public String getTheirAddress() {
		return this.their_address;
	}
	
	public boolean hasConnected() {
		return has_connected;
	}

	@Override
	public String connect(String address, String their_name) throws RemoteException {
		this.their_address = address;
		this.their_name = their_name;
		has_connected = true;
		return my_name;
	}
}