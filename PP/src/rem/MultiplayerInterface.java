package rem;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MultiplayerInterface extends Remote {
	void addAircraft(int speed, int origin_waypoints_index, int destination_waypoints_index, int[] intermediate_waypoints_indexes, int take_off_waypoint_index) throws RemoteException;
}