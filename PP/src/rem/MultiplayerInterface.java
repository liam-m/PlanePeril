package rem;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MultiplayerInterface extends Remote {
	void addAircraft(boolean from_airport, int speed, int origin_waypoints_index, int destination_waypoints_index, int preferred_altitude_index) throws RemoteException;
}