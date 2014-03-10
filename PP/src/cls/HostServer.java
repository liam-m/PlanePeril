package cls;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import scn.Host;

public class HostServer extends UnicastRemoteObject implements HostInterface {
	Registry registry; 
	int port = 1729;
	Host host;
	String my_name;
	
	public HostServer(Host host, String my_name) throws RemoteException {
		super();
		registry = LocateRegistry.createRegistry(port);
		registry.rebind("host_server", this);
		this.host = host;
		this.my_name = my_name;
	}

	@Override
	public String connect(String address, String their_name) throws RemoteException {
		host.connected(address, their_name);
		return my_name;
	}
}