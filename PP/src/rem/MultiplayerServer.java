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
	
	// Initial connection
	
	public void connect(String registry_name) {
		try {
			their_registry = LocateRegistry.getRegistry(opponent_address, opponent_port);
			multiplayer_interface = (MultiplayerInterface)(their_registry.lookup(registry_name));
		} catch (NotBoundException | RemoteException e) {
			e.printStackTrace();
		}
	}
	
	// Notifying each game of a new aircraft
	
	// Server sending an aircraft
	public void sendAircraft(int speed, int origin_waypoints_index, int destination_waypoints_index, int[] intermediate_waypoints_indexes, int take_off_waypoint_index) {
		try {
			multiplayer_interface.addAircraft(speed, origin_waypoints_index, destination_waypoints_index, intermediate_waypoints_indexes, take_off_waypoint_index);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	// Receiving aircraft from other server
	@Override
	public void addAircraft(int speed, int origin_waypoints_index, int destination_waypoints_index, int[] intermediate_waypoints_indexes, int take_off_waypoint_index) {
		
	}
}