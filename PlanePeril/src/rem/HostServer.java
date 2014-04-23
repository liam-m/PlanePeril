package rem;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

@SuppressWarnings("serial")
public class HostServer extends UnicastRemoteObject implements HostInterface {
	Registry registry; 
	int port = 1729;
	String my_name, their_address, their_name;
	boolean has_connected = false;
	
	public HostServer(String my_name) throws RemoteException {
		super();
		this.my_name = my_name;
		registry = LocateRegistry.createRegistry(port);
		registry.rebind("host_server", this);
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