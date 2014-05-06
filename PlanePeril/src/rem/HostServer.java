package rem;

import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;
import java.rmi.server.UnicastRemoteObject;

@SuppressWarnings("serial")
public class HostServer extends UnicastRemoteObject implements HostInterface {
	Registry registry; 
	int port = 1729;
	String my_name, their_address, their_name;
	boolean has_connected_1 = false; // Initial connection
	boolean has_connected_2 = false; // Got difficulty
	private int difficulty = 0; // Default easy
	
	public HostServer(String my_name) throws RemoteException {
		super();
		this.my_name = my_name;
		try {
			registry = LocateRegistry.createRegistry(port);
		} catch (ExportException e) {
			registry = LocateRegistry.getRegistry(port);
		}
		registry.rebind("host_server", this);
	}
	
	public String getTheirName() {
		return this.their_name;
	}
	
	public String getTheirAddress() {
		return this.their_address;
	}
	
	public boolean hasConnected() {
		return has_connected_1 && has_connected_2;
	}

	@Override
	public String connect(String address, String their_name) throws RemoteException {
		this.their_address = address;
		this.their_name = their_name;
		has_connected_1 = true;
		return my_name;
	}
	
	public void close() {
		try {
			UnicastRemoteObject.unexportObject(registry, true);
		} catch (NoSuchObjectException e) {
			e.printStackTrace();
		}
	}
	
	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}

	@Override
	public int getDifficulty() throws RemoteException {
		this.has_connected_2 = true;
		return difficulty;
	}
}