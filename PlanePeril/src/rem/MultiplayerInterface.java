package rem;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MultiplayerInterface extends Remote {
	void addAircraft(boolean from_airport, String name, int speed, int origin_waypoints_index, int destination_waypoints_index, int preferred_altitude_index, boolean waiting_to_be_handed) throws RemoteException;
	
	void changePerformance(int value) throws RemoteException;
	void removeLife() throws RemoteException;
	
	void selected(String name) throws RemoteException;
	
	void turnleft(double dt) throws RemoteException;
	void turnRight(double dt) throws RemoteException;
	void changeAltitude(boolean ascend) throws RemoteException;
	void alterPath(int selectedPathpoint, int waypoint_index) throws RemoteException;

	void toggleManualControl() throws RemoteException;
	
	void removeAircraft(String name) throws RemoteException;
	
	void handOver(String name) throws RemoteException;
}