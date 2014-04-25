package rem;

import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import scn.Multiplayer;

import cls.Aircraft;
import cls.FlightPlan;
import cls.Lives;
import cls.PerformanceBar;
import cls.Waypoint;

@SuppressWarnings("serial")
public class MultiplayerServer extends UnicastRemoteObject implements MultiplayerInterface {
	String opponent_address;
	Registry registry;
	MultiplayerInterface multiplayer_interface;
	Registry their_registry;
	int opponent_port;
	
	Multiplayer game;
	
	public ArrayList<Aircraft> aircraft_queue = new ArrayList<Aircraft>();
	
	boolean left;

	public MultiplayerServer(Multiplayer game_screen, boolean left, String opponent_address, String registry_name, int my_port, int opponent_port) throws RemoteException {
		super();
		this.left = left;
		game = game_screen;
		
		try {
			registry = LocateRegistry.createRegistry(my_port);
		} catch (ExportException e) {
			registry = LocateRegistry.getRegistry(my_port);
		}
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
	
	public void close() {
		try {
			UnicastRemoteObject.unexportObject(registry, true);
		} catch (NoSuchObjectException e) {
			e.printStackTrace();
		}
	}
	
	// Notifying each game of a new aircraft
	
	// Server sending an aircraft
	public void sendAddAircraft(boolean from_airport, String name, int speed, int origin_waypoints_index, int destination_waypoints_index, int preferred_altitude_index) {
		try {
			multiplayer_interface.addAircraft(from_airport, name, speed, origin_waypoints_index, destination_waypoints_index, preferred_altitude_index);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	// Receiving aircraft from other server
	@Override
	public void addAircraft(boolean from_airport, String name, int speed, int origin_waypoints_index, int destination_waypoints_index, int preferred_altitude_index) {	
		if (left) {
			Waypoint origin_point;
			if (from_airport) {
				origin_point = game.right_airport; 
			} else {
				origin_point = game.right_entryexit_waypoints[origin_waypoints_index];
			}
			
			Waypoint destination_point = game.right_entryexit_waypoints[destination_waypoints_index];
			
			aircraft_queue.add(new Aircraft(name, game.aircraft_image, speed, 1, new FlightPlan(origin_point, 
					destination_point, game.right_waypoints, game.right_holding_waypoints, game.right_airport_takeoff_waypoint), preferred_altitude_index));
		} else {
			Waypoint origin_point;
			if (from_airport) {
				origin_point = game.left_airport; 
			} else {
				origin_point = game.left_entryexit_waypoints[origin_waypoints_index];
			}
			
			Waypoint destination_point = game.left_entryexit_waypoints[destination_waypoints_index];
			
			//TODO improve synchronisation methods
			aircraft_queue.add(new Aircraft(name, game.aircraft_image, speed, 1, new FlightPlan(origin_point, 
					destination_point, game.left_waypoints, game.left_holding_waypoints, game.left_airport_takeoff_waypoint), preferred_altitude_index));
		}
	}

	// Notifying each game about a chnage in the performance bar
	
	public void sendChangePerformance(int value) {
		try {
			multiplayer_interface.changePerformance(value);
		} catch (RemoteException e){
			e.printStackTrace();
		}
	}
	
	@Override
	public void changePerformance(int value) throws RemoteException {
		PerformanceBar their_performance = left ? game.right_performance : game.left_performance;
		if (value >= their_performance.getMax()) {
			their_performance.setToMax();
		}
		their_performance.changeValueBy(value);
	}

	//Notifying each game about a change to lives
	
	public void sendRemoveLife() {
		try {
			multiplayer_interface.removeLife();
		} catch (RemoteException e){
			e.printStackTrace();
		}
	}
	
	@Override
	public void removeLife() throws RemoteException {
		Lives their_lives = left ? game.right_lives : game.left_lives;
		their_lives.decrement();
		if (their_lives.getLives() == 0) {
			game.gameOver(true);
		}
	}

	public void sendSelected(String name) {
		try {
			multiplayer_interface.selected(name);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void selected(String name) throws RemoteException {
		for (Aircraft plane : game.aircraft) {
			if (plane.getName().equals(name)) {
				game.their_selected = plane;
				break;
			}
		}
	}

	public void sendTurnLeft(double dt) {
		try {
			multiplayer_interface.turnleft(dt);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void turnleft(double dt) throws RemoteException {
		game.their_selected.turnLeft(dt);
	}

	public void sendTurnRight(double dt) {
		try {
			multiplayer_interface.turnRight(dt);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void turnRight(double dt) throws RemoteException {
		game.their_selected.turnRight(dt);
	}

	public void sendChangeAltitude(boolean ascend) {
		try {
			multiplayer_interface.changeAltitude(ascend);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void changeAltitude(boolean ascend) throws RemoteException {
		if (ascend) {
			game.their_selected.increaseTargetAltitude();
		} else {
			game.their_selected.decreaseTargetAltitude();
		}
	}

	public void sendAlterPath(int selected_pathpoint, int waypoint_index) {
		try {
			multiplayer_interface.alterPath(selected_pathpoint, waypoint_index);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void alterPath(int selected_pathpoint, int waypoint_index) throws RemoteException {
		Waypoint[] their_waypoints = left ? game.right_waypoints : game.left_waypoints;
		game.their_selected.alterPath(selected_pathpoint, their_waypoints[waypoint_index]);
	}
	
	public void sendToggleManualControl() {
		try {
			multiplayer_interface.toggleManualControl();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void toggleManualControl() {
		game.their_selected.toggleManualControl();
	}

	public void sendRemoveAircraft(String name) {
		try {
			multiplayer_interface.removeAircraft(name);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void removeAircraft(String name) throws RemoteException {
		for (int i = 0; i < game.aircraft.size(); i++) {
			if (game.aircraft.get(i).getName().equals(name)) {
				if (game.aircraft.get(i).equals(game.selected_aircraft))
					game.deselectAircraft();
				game.aircraft.remove(i);
				break;
			}
		}
		
	}
}