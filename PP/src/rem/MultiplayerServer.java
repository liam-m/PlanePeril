package rem;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class MultiplayerServer extends UnicastRemoteObject implements MultiplayerInterface {
	String opponent_address;
	Registry registry;
	MultiplayerInterface multiplayer_interface;
	Registry their_registry;
	int opponent_port;

	public MultiplayerServer(String opponent_address, String registry_name, int my_port, int opponent_port) throws RemoteException {
		super();
		registry = LocateRegistry.createRegistry(my_port);
		registry.rebind(registry_name, this);
		this.opponent_address = opponent_address;
		this.opponent_port = opponent_port;
	}
	
	public void connect(String registry_name) {
		try {
			their_registry = LocateRegistry.getRegistry(opponent_address, opponent_port);
			multiplayer_interface = (MultiplayerInterface)(their_registry.lookup(registry_name));
		} catch (NotBoundException | RemoteException e) {
			e.printStackTrace();
		}
	}
	
	public void sendAircraft() {
		try {
			multiplayer_interface.addAircraft(true);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void addAircraft(boolean x) {
		
	}
}